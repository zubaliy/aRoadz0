package com.andrei.aroadz0.controller;

import java.util.Observable;
import java.util.Observer;
import com.andrei.aroadz0.model.Data;
import android.content.Context;
import android.util.Log;

public class DataCombiner extends Observable implements Observer{
	
	private static final String TAG = "zDataCombiner";
	private Accelerometer acc = null;
	private Gps gps  = null;
	private	Data accData = null;
	private Data gpsData = null;
	private Data mData = null;
	



	/*
	 * Listen to the events fired from Accelerometer
	 * For each new event get new location from GPS
	 * Gps updates his location every 1 second
	 *
	 */


	public DataCombiner(Context context){
		accData = new Data();
		gpsData = new Data();
		mData = new Data();
		acc = new Accelerometer(context);
		gps  = new Gps(context);
		
	 	
	 	acc.addObserver(this);
	 	//gps.addMyObserver(this);	 	
	 	
	 	Log.d(TAG, "DataCombiner created");
	}


	@Override
	public void update(Observable observable, Object data) {
		// data comes from acc
		// get location from gps
		// and store acc-data and gps-data in Data
		accData = (Data) data;
		gpsData = gps.getData();
		
		mData.setTimestamp( System.currentTimeMillis() );
		mData.setLongitude( gpsData.getLongitude() );
		mData.setLatitude( gpsData.getLatitude() );
		mData.setAccuracy( gpsData.getAccuracy() );
		mData.setSpeed( gpsData.getSpeed() );
		mData.setAltitude( gpsData.getAltitude() );
		mData.setAccxaR( accData.getAccxaR() );
		mData.setAccyaR( accData.getAccyaR() );
		mData.setAcczaR( accData.getAcczaR() );
		
		//if (gps.checkIfGpsFix()){
		Log.d(TAG, "==================================" );
		Log.d(TAG, "Timestamp: " + mData.getTimestamp() );
		Log.d(TAG, "Latitude: " + mData.getLatitude() );
		Log.d(TAG, "Accuracy: " + mData.getAccuracy() );
		Log.d(TAG, "Speed: " + mData.getSpeed() );
		Log.d(TAG, "Altitude: " + mData.getAltitude() );


		setChanged();
        notifyObservers(this.mData);
	}
	
	@Override
	public void notifyObservers(Object data) {
		// 
		super.notifyObservers(data);
	}

	public void addListeners() { acc.addListeners(); }
	public void removeListeners() { acc.removeListeners(); }


	public boolean isGpsEnabled() {
		return gps.isGpsEnabled();
	}

}
