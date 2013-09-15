package com.example.aroadz0;

import java.util.Observable;


import be.andrei.aroadz.utils.Config;
import be.andrei.aroadz.utils.GUI;
import be.andrei.aroadz.utils.Toasts;
import be.andrei.aroadz.R;




import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.SystemClock;

public class GPS extends Observable implements LocationListener, GpsStatus.Listener{
	private static Location location = null;
	private	Location mLastLocation;
	private	boolean isGPSFix;
	private	double mLastLocationMillis;
	private	LocationManager locationManager;
			
	public static boolean gps_speedcheck = false;
	public static boolean gps_enabled = false;
	public static boolean location_changed = false;
	public static boolean locationOK = false;

	
	public GPS() {
		super();
		
		locationManager = (LocationManager) Config.activity.getSystemService(Context.LOCATION_SERVICE);
		

		/* from: JavaDoc
		 * The frequency of notification may be controlled using the minTime and minDistance parameters.
		 * If minTime is greater than 0, the LocationManager could potentially rest for minTime milliseconds between location updates to conserve power.
		 * If minDistance is greater than 0, a location will only be broadcasted if the device moves by minDistance meters.
		 * To obtain notifications as frequently as possible, set both parameters to 0.
		 * 
		 */
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Config.GPS_UPDATE_TIME_INTERVAL, Config.GPS_UPDATE_DISTANCE, this);
		
		checkIfGpsIsEnabledOnStart();
	}
	
	private void checkIfGpsIsEnabledOnStart() {
		if(locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )){
			GUI.btn_gps_WAITING();
			//Toasts.gps_enabled();
		} else {
			GUI.btn_gps_OFF();
			//Toasts.gps_disabled();
		}
	}

	public Location getLocation(){
		if(location == null){
			return null;
		}

		return location;
	}
	
	public double[] getData(){
		if(location == null){
			double[] dgps = {0, 0, 0, 0, 0}; //long, lat, speed, accuracy, altitude
			return dgps;
		}
		
		double[] data= {location.getLongitude(), location.getLatitude(),  location.getSpeed(), location.getAccuracy(), location.getAltitude()};
		return data;
	}
	
	/*
	 *  A part of the algorithm.
	 *  Write only when a specific speed is reached write data to logfile
	 *  Could not be solved this way, because the speed changes only when new location are computed.
	 *  If location is not changed, then speed stay same as previous. Not going to zero when you stay at one place.
	 */
	public static boolean GpsHasSpeed(){
		if(location == null && gps_enabled){
			return false;
		}
		
		return location.getSpeed() >= Config.GPS_SPEED_THRESHOLD;		
	}
	
	public void onLocationChanged(Location loc) {		
		location = loc;		
		
		// used in onGpsStatusChanged 
	    mLastLocationMillis = SystemClock.elapsedRealtime();
	    mLastLocation = location;
		
		

		GUI.txt_gps_lat.setText(Config.activity.getString(R.string.txt_gps_lat) + " " + location.getLatitude());
		GUI.txt_gps_long.setText(Config.activity.getString(R.string.txt_gps_long) + " "	+ location.getLongitude());
		
		GUI.txt_speed.setText(Config.activity.getString(R.string.txt_speed) + String.format("%.2f", location.getSpeed()) + "[m/s]" );
		GUI.txt_accuracy.setText(Config.activity.getString(R.string.txt_accuracy) + " " + location.getAccuracy());
		
		if (GUI.btn_record.isChecked()) {
	        setChanged();
	        notifyObservers();
        }
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		/* This is called when the GPS status alters */
		
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
						Toasts.showError("GPS Status Changed: Out of Service");
						GUI.btn_gps_WAITING();
						break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE: // Every period of inactivity
						GUI.btn_gps_WAITING();
						Toasts.showError("GPS Status Changed: Temporarily Unavailable");
						break;
		case LocationProvider.AVAILABLE: // Each batch of locationupdates
						GUI.btn_gps_ON();
						//Toasts.showError("GPS Status Changed: Available");
						break;
		}
	}
	
	public void onProviderDisabled(String provider) {
		Toasts.gps_disabled();
		GUI.btn_gps_OFF();
		gps_enabled = false;
	}

	public void onProviderEnabled(String provider) {
		Toasts.gps_enabled();
		GUI.btn_gps_WAITING();
		gps_enabled = true;
	}


	
	public static void askToEnableGPS(){
		Intent gpsOptionsIntent = new Intent(  
			    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
				Config.activity.startActivity(gpsOptionsIntent);
	}


	// na te kijken
	@Override
	public void onGpsStatusChanged(int event) {
        boolean isGPSFix = false;
        switch (event) {
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                if (mLastLocation != null)
                	if ((SystemClock.elapsedRealtime() - mLastLocationMillis) < Config.GPS_UPDATE_TIME_INTERVAL * 2) {
                		isGPSFix = true;
                	}

                if (isGPSFix) { // A fix has been acquired.
                	GUI.btn_gps_ON();
                } else { // The fix has been lost.
                	GUI.btn_gps_WAITING();
                }

                break;

            case GpsStatus.GPS_EVENT_FIRST_FIX: // A fix has been acquired.
            	isGPSFix = true;
                break;
            case GpsStatus.GPS_EVENT_STARTED:
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                break;
        }
    }



	
	public void addMyObserver(DataCombiner dc){
		this.addObserver(dc);
	}

	
	
	
}