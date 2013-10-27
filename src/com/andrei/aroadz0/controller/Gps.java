package com.andrei.aroadz0.controller;

import java.util.Observable;

import com.andrei.aroadz0.model.Data;
import com.andrei.aroadz0.utils.Config;


import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

public class Gps extends Observable implements LocationListener, GpsStatus.Listener {
	
	private final static String LOG_TAG = "zGps";
	private static Context context = null;
	
	private LocationManager locman = null; 
	private Location location = null;
	private Data mData = null;
	
	private boolean enabled = false;
	private boolean firstOnLocationChange = false; //First GpsFix

	private static boolean isGPSEnabled = false;
	
	
	public Gps(Context context) {
		Log.d(LOG_TAG, "GPS created");
		Gps.context = context;
		locman = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, Config.GPS_UPDATE_TIME_INTERVAL, Config.GPS_UPDATE_DISTANCE, this);
		
		
		mData = new Data();
		enabled = isGpsEnabled();
		
		Log.d(LOG_TAG, "GPS object created");

	}
	
	public boolean isGpsEnabled(){
		
		isGPSEnabled = locman.isProviderEnabled(LocationManager.GPS_PROVIDER);
		Log.d(LOG_TAG, "checkIfGpsEnable() " + isGPSEnabled);
		return isGPSEnabled;
	}
	
	public boolean checkIfGpsFix(){
		return firstOnLocationChange;
	}
	
	
	public Location getLocation(){

		isGPSEnabled  = locman.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		if (isGPSEnabled) {
            if (location == null) {
                locman.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        Config.GPS_UPDATE_TIME_INTERVAL,
                        Config.GPS_UPDATE_DISTANCE, this);
                Log.d(LOG_TAG, "GPS Enabled");
                if (locman != null) {
                    location = locman.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
        }
		return location;
	}
	
	public Data getData(){
		
		if (location != null) {			
			mData.setLongitude(location.getLongitude());
			mData.setLatitude(location.getLatitude());
			mData.setAccuracy(location.getAccuracy());
			mData.setSpeed(location.getSpeed());
			mData.setAltitude(location.getAltitude());
		} else {
			Log.d("zTest", "Gps: Location = NULL");
		}
		
		return mData;
	}

	@Override
	public void onLocationChanged(Location location) {
		// First location will be not null when first gpsfix will be obtained
		firstOnLocationChange = true;
		this.location = location;
		Log.d(LOG_TAG, "Location Changed");
		
		mData.setLongitude(location.getLongitude());
		mData.setLatitude(location.getLatitude());
		mData.setAccuracy(location.getAccuracy());
		mData.setSpeed(location.getSpeed());
		mData.setAltitude(location.getAltitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d(LOG_TAG, "GPS Disabled");
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(LOG_TAG, "GPS Enabled");
		
	}

	@Override
	public void onGpsStatusChanged(int event) {
		Log.d(LOG_TAG, "GPS Event Status Changed");
			switch(event) {
				case GpsStatus.GPS_EVENT_FIRST_FIX:				
									Log.d(LOG_TAG, "GPS Event Status " + "GPS_EVENT_FIRST_FIX");
									break;
				case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
									Log.d(LOG_TAG, "GPS Event Status " + "GPS_EVENT_SATELLITE_STATUS");
									break;
				case GpsStatus.GPS_EVENT_STARTED:
									Log.d(LOG_TAG, "GPS Event Status " + "GPS_EVENT_STARTED");
									break;
				case GpsStatus.GPS_EVENT_STOPPED:
									Log.d(LOG_TAG, "GPS Event Status " + "GPS_EVENT_STOPPED");
									break;
				default:			Log.d(LOG_TAG, "GPS Event Status " + "DEFAULT");
									break;
			}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d(LOG_TAG, "GPS Status Changed");
		Log.d(LOG_TAG, "Provider " + provider);
		String state = new String();
			switch(status) {
				case LocationProvider.AVAILABLE:				
									state = "AVAILABLE";
									break;
				case LocationProvider.OUT_OF_SERVICE:
									state = "OUT_OF_SERVICE";
									break;
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
									state = "TEMPORARILY_UNAVAILABLE";
									break;
				default:			state = "ERROR";
									break;
			}
		Log.d(LOG_TAG, "GPS Status " + state);
		
	}


	
	
	
	

	
	

	
	
	


	


}
