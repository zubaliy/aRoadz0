package com.example.aroadz0;

import java.util.Observable;



import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;





public class Accelerometer extends Observable implements SensorEventListener {
	
	private static final String TAG = "zAcc";
	
    private SensorManager sensorManager = null;
    private Sensor accelerometer, acceleration, gravity, gyroscope, magneticfield;
    private int SENSOR_DELAY = SensorManager.SENSOR_DELAY_FASTEST;
     
	private float[] zrotationMatrix = new float[16];
	private float[] dlin_acc = new float[3]; ;
	private float[] dgravity = new float[3];
	private float[] dmagfield = new float[3];
	

	public Accelerometer(Activity activity){

		sensorManager = (SensorManager) activity.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);

		acceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		magneticfield = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        addListeners();
	}
    
	private void addListeners() {
		sensorManager.registerListener(this, acceleration, SENSOR_DELAY );
        sensorManager.registerListener(this, gravity, SENSOR_DELAY );
        sensorManager.registerListener(this, magneticfield, SENSOR_DELAY );
		
	}
	
	public void removeListeners() {
		sensorManager.unregisterListener(this);
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
    	}
    	
    	z_computeOrientation();
    	
    	setChanged();
        notifyObservers();
    }
 
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Not interested in this event
    }
    
    private void z_computeOrientation() {
    	
    	if (SensorManager.getRotationMatrix(zrotationMatrix, null, dgravity, dmagfield) ){ 	}
     
    	android.opengl.Matrix.invertM(zrotationMatrix, 0, zrotationMatrix, 0);
    	
    	dlin_acc = remap(dlin_acc);
    	
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
	
	public void addMyObserver(Activity ac) {
		this.addObserver(ac);
	}
	
}