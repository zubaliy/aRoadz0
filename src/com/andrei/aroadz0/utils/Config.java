package com.andrei.aroadz0.utils;



import java.io.File;

import com.andrei.aroadz0.model.User;
import com.andrei.aroadz0.service.MyService;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;




public class Config {
	private final static String TAG = "zConfig"; 
	
	public final static String APP_NAME = "aRoadz";
	
	public final static String WORKFOLDER = new String("sdcard/!aRoadz/");
	//public final static String wf_detected = new String("sdcard/!aRoadz/Detected");
	
	public final static String URL_UPLOAD = "https://aroadz3.herokuapp.com/upload";
	public final static String URL_REGISTER_USER = "https://aroadz3.herokuapp.com/saveUser";
	public final static String url_upload_string = "http://aroadz3.herokuapp.com/aroadz/upload_string";
	
	public final static int GPS_SPEED_THRESHOLD = 2; // [ 1 m/s = 3.6 km/h ]
	public final static int GPS_UPDATE_TIME_INTERVAL = 0; // continu nieuwe positie opvragen
	public final static float GPS_UPDATE_DISTANCE = 0; // continu nieuwe positie opvragen
	public final static int SENSOR_DELAY = SensorManager.SENSOR_DELAY_GAME;
	
	
	/*
	 0.006493 km
	 0.006455 km
	 0.006362 km 
	 */
	
	public static boolean write = false;
	
	private static Context mContext = null;

	
	public static void init(Context context){
		mContext = context;
		
		createFolder();
	}
	
	public static Context getContext() {
		return mContext;
		
	}
	

	private static void createFolder(){
		
		File file = new File(WORKFOLDER);
		if ( !file.exists() ) {
			file.mkdirs();
			Log.d(TAG, "!aRoadz - folder created");

		} else {
			Log.d(TAG, "!aRoadz - folder exists");
		}
		
		
/*		file = new File(wf_detected);
		if ( !file.exists() ) {
			file.mkdirs();
		} else {
			Log.d(TAG, "Workfolder - created ");
		}*/
	}

	public static boolean isWrite() {
		// TODO Auto-generated method stub
		return write;
	}
	
	public static void startService() {
		mContext.startService( new Intent(mContext, MyService.class) );
	}
	public static void stopService() {
		mContext.stopService( new Intent(mContext, MyService.class) );
	}
	
	public static boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (MyService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	

}
