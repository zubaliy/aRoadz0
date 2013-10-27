package com.andrei.aroadz0.utils;




import com.andrei.aroadz0.R;

import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class Toasts {
	private static Toast t;
	
	public static void gps_enabled(){
		Toast.makeText(Config.getContext(), R.string.btn_gps_ON, Toast.LENGTH_SHORT).show();	
	}
	public static void gps_disabled(){
		Toast.makeText(Config.getContext(), R.string.btn_gps_OFF, Toast.LENGTH_SHORT).show();
	}
	
	public static void showError(String s){
		t = Toast.makeText(Config.getContext(), s, Toast.LENGTH_LONG);
		t.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 100);
		TextView v = (TextView) t.getView().findViewById(android.R.id.message);
		v.setTextColor(Color.RED);
		
		t.show();
	}
	public static void showGreenMessage(String s) {
		t = Toast.makeText(Config.getContext(), s, Toast.LENGTH_LONG);
		TextView v = (TextView) t.getView().findViewById(android.R.id.message);
		v.setTextColor(Color.GREEN);
		t.show();
		
	}
	
	

}
