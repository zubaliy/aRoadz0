package com.example.aroadz0;

import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;

import android.annotation.SuppressLint;
import be.andrei.aroadz.R;
import be.andrei.aroadz.model.Counter;
import be.andrei.aroadz.model.Data;
import be.andrei.aroadz.utils.Config;
import be.andrei.aroadz.utils.GUI;
import be.andrei.aroadz.utils.Toasts;

public class DataCombiner extends Observable implements Observer{
	
	private Accelerometer acc = null;
	private	GPS gps  = null;
	private	Data data = null;
	private	SimpleDateFormat format;			
	private	String dateString;
	private	String dataline;
	private	String batteryLevel;
			
	
	@SuppressLint("SimpleDateFormat")
	public DataCombiner(){
			acc = Accelerometer.getInstance();
			gps  = new GPS();
			data = new Data();
			
			format = new SimpleDateFormat("ddkkmmssSSS");
		 	dateString = new String();
		 	
		 	dataline = new String();
		 	batteryLevel = new String();
		 	
		 	acc.addMyObserver(this);
		 	gps.addMyObserver(this);	 	
	}


	public Data getData(){	
		double[] dgps = gps.getData(); // long, lat, speed, accuracy, altitude
		float[] dacc = acc.getAcc(); // x, y, z, 
		float[] dlinacc = acc.getLinaccR(); //xa, ya, za
		float[] dgyro = acc.getGyro(); // x, y ,z of Gyroscope
		float[] daccR = acc.getAccR(); // raw accelerometer remapped
		float[] dlinaccR = acc.getLinaccR(); // linear acceleration remapped
		
		
		
		//dateString = format.format(dacc[6]);
		
		// Check final DATA STRUCTURE in class Data.java
		/* DATA STRUCTURE
		 *  time
		 *  lon, lat, 
		 *  accx, accy, accz, 				RAW Accelerometer
		 *  accxa, accya, accza, 			RAW Acceleration
		 *  speed, gps_accuracy, altitude
		 *  gyrox, goroy, gyroz				RAW
		 *  accxR, accyR, acczR				REMAPPED 
		 *  accxaR, accyaR, acczaR, 		REMAPPED
		 *  
		 */
		
		data.setData(acc.getPeriod(), 
					dgps[0], dgps[1], 
					dacc[0], dacc[1], dacc[2], 
					dlinacc[0], dlinacc[1], dlinacc[2], 
					daccR[0], daccR[1], daccR[2], 			
					dlinaccR[0], dlinaccR[1], dlinaccR[2],
					dgps[2], dgps[3], dgps[4], 				
					dgyro[0], dgyro[1], dgyro[2] 	
					);

		return data;
	}


	@Override
	public void update(Observable observable, Object data) {
		setChanged();
        notifyObservers();

		
	}
	


}
