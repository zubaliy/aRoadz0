package com.andrei.aroadz0.model;

import java.util.Locale;

import com.andrei.aroadz0.MainActivity;
import com.andrei.aroadz0.utils.Config;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * @author Andriy
 *
 *	TODO: Save user information in the SQLite database
 *	http://developer.android.com/guide/topics/data/data-storage.html#db
 *
 */
public class User {
	
	private static User user;
	
	private boolean registered = false;
	public static final String _REGISTERED = "REGISTERED";
	
	public static final String _EMAIL = "email";
	public static final String _PASSWORD = "password";
	public static final String _NAME = "name";
	public static final String _PHONE_MODEL = "phone_model";
	public static final String _ANDROID_VERSION = "android_version";
	public static final String _ANDROID_LANGUAGE = "android_language";
	public static final String _ACC_MAXIMUM_RANGE = "acc_maximum_range";
	public static final String _ACC_RESOLUTION = "acc_resolution";
	public static final String _ACC_MIN_DELAY = "acc_min_delay";
	public static final String _ACC_POWER = "acc_power";
	public static final String _ACC_VENDOR = "acc_vendor";
	public static final String _ACC_VERSION = "acc_version";
	public static final String _ACC_TYPE = "acc_type";
	
	
	private String	email,
					password,
					name,
					phone_model,
					android_version,
					android_language;

	private String 	acc_maximum_range,
					acc_resolution,
					acc_min_delay, //the minimum delay allowed between two events in microsecond or zero if this sensor only returns a value when the data it's measuring changes.
					acc_power,
					acc_vendor,
					acc_version,
					acc_type;

	private static final String LOG_TAG = "zUser";
	

	
	private User() {
        SharedPreferences  sPref = Config.getContext().getSharedPreferences(Config.APP_NAME, Context.MODE_PRIVATE);

        // _NAME is empty only in the first time
        Log.d(LOG_TAG, "sPref - email: " + sPref.getString(User._EMAIL, ""));
        
        if (sPref.getString(User._EMAIL, "").isEmpty() || sPref.getString(User._EMAIL, "").equals("null")){
	        this.email = this.getDeviceEmail(Config.getContext());
	        this.name = this.email.split("@")[0];  
	        this.password = new String("null");
        } else {
        	this.email = sPref.getString(User._EMAIL, "");
 	        this.name = sPref.getString(User._NAME, "");  
 	        this.password = sPref.getString(User._PASSWORD, "");
        }
        
        if (sPref.getString(User._ACC_POWER, "").isEmpty()) {
        	this.phone_model = new String(android.os.Build.MODEL);
    		this.android_version = new String(android.os.Build.VERSION.RELEASE);
    		this.android_language = new String(Locale.getDefault().getLanguage());
    		
    		this.acc_maximum_range = new String("null");
    		this.acc_resolution = new String("null");
    		this.acc_min_delay = new String("null");
    		this.acc_power = new String("null"); 
        	
        } else {
        	this.phone_model = sPref.getString(User._PHONE_MODEL, "");
    		this.android_version = sPref.getString(User._ANDROID_VERSION, "");
    		this.android_language = sPref.getString(User._ANDROID_LANGUAGE, "");
    		
    		this.acc_maximum_range = sPref.getString(User._ACC_MAXIMUM_RANGE, "");
    		this.acc_resolution = sPref.getString(User._ACC_RESOLUTION, "");
    		this.acc_min_delay = sPref.getString(User._ACC_MIN_DELAY, "");
    		this.acc_power = sPref.getString(User._ACC_POWER, ""); 
    		this.acc_vendor = sPref.getString(User._ACC_VENDOR, ""); 
    		this.acc_type = sPref.getString(User._ACC_TYPE, ""); 
    		this.acc_version = sPref.getString(User._ACC_VERSION, ""); 	
        }
        
        	this.registered = Boolean.valueOf(sPref.getString(User._REGISTERED, "false"));
        	
        
		
/*		
 		this.email = new String("null");
		this.password = new String("null");
		this.name = new String("null");
		this.phone_model = new String(android.os.Build.MODEL);
		this.android_version = new String(android.os.Build.VERSION.RELEASE);
		this.android_language = new String(Locale.getDefault().getLanguage());
		
		this.acc_maximum_range = new String("null");
		this.acc_resolution = new String("null");
		this.acc_min_delay = new String("null");
		this.acc_power = new String("null"); 
*/
	
	}

	public static User getInstance() {
		if (user == null) {
			user = new User();
		}
		return user;
	}
	
	
	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean bool) {
		this.registered = bool;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone_model() {
		return phone_model;
	}

	public void setPhone_model(String phone_model) {
		this.phone_model = phone_model;
	}

	public String getAndroid_version() {
		return android_version;
	}

	public void setAndroid_version(String android_version) {
		this.android_version = android_version;
	}

	public String getAndroid_language() {
		return android_language;
	}

	public void setAndroid_language(String android_language) {
		this.android_language = android_language;
	}

	public String getAcc_maximum_range() {
		return acc_maximum_range;
	}

	public void setAcc_maximum_range(String acc_maximum_range) {
		this.acc_maximum_range = acc_maximum_range;
	}

	public String getAcc_resolution() {
		return acc_resolution;
	}

	public void setAcc_resolution(String acc_resolution) {
		this.acc_resolution = acc_resolution;
	}

	public String getAcc_min_delay() {
		return acc_min_delay;
	}

	public void setAcc_min_delay(String acc_min_delay) {
		this.acc_min_delay = acc_min_delay;
	}

	public String getAcc_power() {
		return acc_power;
	}

	public void setAcc_power(String acc_power) {
		this.acc_power = acc_power;
	}

	public String getAcc_vendor() {
		return acc_vendor;
	}

	public void setAcc_vendor(String acc_vendor) {
		this.acc_vendor = acc_vendor;
	}

	public String getAcc_version() {
		return acc_version;
	}

	public void setAcc_version(String acc_version) {
		this.acc_version = acc_version;
	}

	public String getAcc_type() {
		return acc_type;
	}

	public void setAcc_type(String acc_type) {
		this.acc_type = acc_type;
	}

	@Override
	public String toString() {
		String s = new String();
		s  = "Accelerometer: " 
		   + "Power: " + this.getAcc_power()
		   + "\nVendor: " + this.getAcc_vendor()
		   + "\nResolution: " + this.getAcc_resolution()
		   + "\nMin delay: " + this.getAcc_min_delay()
		   + "\nMax range: " + this.getAcc_maximum_range()
		   + "\nVersion: " + this.getAcc_version()
		   + "\tType: " + this.getAcc_type()		
		   + "\nAndroid Lang: " + this.getAndroid_language()
		   + "\tVersion: " + this.getAndroid_version()
		   + "\nPhone model: " + this.getPhone_model();

		return s;
	}
	
	
	public String getDeviceEmail(Context context){
		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts = accountManager.getAccountsByType("com.google");
	    Account account;
	    if (accounts.length > 0) {
	      account = accounts[0]; // get first account   
	      //Log.d(LOG_TAG, "email: " + account.type);
	      return account.name;
	      
	    } else {
	      return null;
	    }
	}

	public void commit() {
		Editor e = Config.getContext().getSharedPreferences(Config.APP_NAME ,Context.MODE_PRIVATE).edit();
		
		e.putString(_REGISTERED, String.valueOf(user.isRegistered()));
		
		e.putString(_EMAIL, user.getEmail());
		e.putString(_PASSWORD, user.getPassword());
		e.putString(_NAME, user.getName());
		e.putString(_PHONE_MODEL, user.getPhone_model());
		e.putString(_ANDROID_VERSION, user.getAndroid_version());
		e.putString(_ANDROID_LANGUAGE, user.getAndroid_language());

		e.putString(_ACC_MAXIMUM_RANGE, user.getAcc_maximum_range());
		e.putString(_ACC_RESOLUTION, user.getAcc_resolution());
		e.putString(_ACC_MIN_DELAY, user.getAcc_min_delay());
		e.putString(_ACC_POWER, user.getAcc_power());
		e.putString(_ACC_VENDOR, user.getAcc_vendor());
		e.putString(_ACC_VERSION, user.getAcc_version());
		e.putString(_ACC_TYPE, user.getAcc_type());
		
		e.commit();
	
	}


}
