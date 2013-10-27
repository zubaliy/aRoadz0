package com.andrei.aroadz0.controller;

import java.util.Observable;


import com.andrei.aroadz0.MainActivity;
import com.andrei.aroadz0.model.Data;
import com.andrei.aroadz0.model.User;
import com.andrei.aroadz0.utils.Config;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.util.Log;





public class Accelerometer extends Observable implements SensorEventListener {
	
	private static final String TAG = "zAcc";
	
	private Data mData = null;

    private SensorManager sensorManager = null;
    private Sensor acceleration, gravity, magneticfield = null; //accelerometer,gyroscope
     
	private float[] zrotationMatrix = new float[16];
	private float[] dlin_acc = new float[3]; 
	private float[] dlin_accR = new float[3]; 
	private float[] dgravity = new float[3];
	private float[] dmagfield = new float[3];
	


	public Accelerometer(Context context){
		mData = new Data();
		
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

		acceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		magneticfield = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		setUserDetails();

        //addListeners();
        Log.d(TAG, "Accelerometer()");
	}
	

	
	public void addListeners() {
			
			sensorManager.registerListener(this, acceleration, Config.SENSOR_DELAY );
	        sensorManager.registerListener(this, gravity, Config.SENSOR_DELAY );
	        sensorManager.registerListener(this, magneticfield, Config.SENSOR_DELAY );	
	        
	        Log.d(TAG, "addListeners() ");

	}
	
	public void removeListeners() {

			sensorManager.unregisterListener(this);
			
			Log.d(TAG, "removeListeners() ");

	}
	
    @Override
    public synchronized void onSensorChanged(SensorEvent event) {
    	
    	if(event.sensor.getType() == Sensor.TYPE_GRAVITY) {
    		dgravity = event.values.clone();
    	}
    	
    	if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
    		dmagfield = event.values.clone();
    	}
    	
    	if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
	        dlin_acc = event.values.clone();
    	
	        dlin_accR = z_computeOrientation(dlin_acc);
	    	
	    	mData.setAccxyzaR(dlin_accR);
	    	
	    	setChanged();
	        notifyObservers(mData);
	        Log.d(TAG, "onSensorChanged() " + dlin_accR[0]);       
    	}
    }
 
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Not interested in this event
    }
    
    private float[] z_computeOrientation(float[] dlin_acc) {
    	
    	if (SensorManager.getRotationMatrix(zrotationMatrix, null, dgravity, dmagfield) ){ 	}
     
    	android.opengl.Matrix.invertM(zrotationMatrix, 0, zrotationMatrix, 0);
    	
    	return remap(dlin_acc);
    	
    }
	
	private float[] remap(float data[]){	
		float dacc[] = new float [4];
		dacc[0] = data[0];
		dacc[1] = data[1];
		dacc[2] = data[2];
		dacc[3] = 0;
		
		float[] resultVec = new float [4];
		Matrix.multiplyMV(resultVec, 0, zrotationMatrix, 0, dacc, 0);
		
		return resultVec;
	}
	
	private void setUserDetails () {
		getAccDetails();
	}
	
	public String[] getAccDetails() {
		
	    User user = User.getInstance();
        user.setAcc_maximum_range(Float.toString(acceleration.getMaximumRange()));
        user.setAcc_resolution(Float.toString(acceleration.getResolution()));
        user.setAcc_min_delay(Float.toString(acceleration.getMinDelay())); 
        user.setAcc_power(Float.toString(acceleration.getPower()));
        user.setAcc_vendor(acceleration.getVendor());
        user.setAcc_version(Float.toString(acceleration.getVersion()));
        user.setAcc_type(Float.toString(acceleration.getType()));
        user.commit();
		return null;
	}
	

	
	@Override
	public void notifyObservers(Object data) {
		// TODO Auto-generated method stub
		super.notifyObservers(data);
	}
	
}