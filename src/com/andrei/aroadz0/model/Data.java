package com.andrei.aroadz0.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Data {
					
	private double timestamp = 0.0;
	private double longitude, latitude = 0.0;
	private double accuracy, speed, altitude = 0.0;
	private double accxaR, accyaR, acczaR = 0.0;
	
	private SimpleDateFormat format = new SimpleDateFormat("ddkkmmssSSS", Locale.UK);
	
	public void setData (double timestamp, double longitude, double latitude, double accuracy, double speed, double altitude, double accxaR, double accyaR, double acczaR ) {
		this.timestamp = timestamp;
		this.longitude =longitude;
		this.latitude = latitude;
		this.accuracy = accuracy;
		this.speed = speed;
		this.altitude = altitude;
		this.accxaR = accxaR;
		this.accyaR = accyaR;
		this.acczaR = acczaR;
	}
	
	public Data getData() {
		return this;
	}

	public double getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public void setAccxyzaR(float[] dlin_accR) {
		this.accxaR = dlin_accR[0];
		this.accyaR = dlin_accR[1];
		this.acczaR = dlin_accR[2];
	}

	public double getAccxaR() {
		return accxaR;
	}

	public void setAccxaR(double accxaR) {
		this.accxaR = accxaR;
	}

	public double getAccyaR() {
		return accyaR;
	}

	public void setAccyaR(double accyaR) {
		this.accyaR = accyaR;
	}

	public double getAcczaR() {
		return acczaR;
	}

	public void setAcczaR(double acczaR) {
		this.acczaR = acczaR;
	}


	@Override
	public String toString() { 
		//separator for the db is ";\t"
		String text = format.format(timestamp) 	
			+ ",\t" + longitude + ",\t" + latitude  
			+ ",\t" + accuracy + ",\t" + speed + ",\t" + altitude
			+ ",\t" + accxaR 	+ ",\t" + accyaR 	+ ",\t" + acczaR;
			
		return text;
	}

	
}
