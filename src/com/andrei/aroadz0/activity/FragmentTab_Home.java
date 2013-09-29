package com.andrei.aroadz0.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;
/*
 * getActivity(). - Используется если нужно вызвать метод из MainActivity  
 * 
 */
import com.andrei.aroadz0.R;
import com.andrei.aroadz0.service.MyService;
import com.andrei.aroadz0.utils.Config;
import com.andrei.aroadz0.utils.Toasts;
 
	public class FragmentTab_Home extends SherlockFragment implements OnClickListener, OnCheckedChangeListener{
	 
		private final String LOG_TAG = "zHomeTab";
		
	    private Button btnServiceStart;
	    private Button btnServiceStop;
	    
	    private ToggleButton btnt_gps;
	    private ToggleButton btnt_write;
	 
	    private TextView console = null;
	    
	    
	    LocationManager locman = null;
	    

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			locman = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		}

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        // Get the view from fragment_home.xml
	        View view = inflater.inflate(R.layout.fragment_home, container, false);
	        

	        btnt_gps = (ToggleButton) view.findViewById(R.id.btnt_gps);
	        btnt_gps.setOnCheckedChangeListener(this);
	        
	        btnServiceStart = (Button) view.findViewById(R.id.btnServiceStart);
	        btnServiceStart.setOnClickListener(this);
	        
	        btnServiceStop = (Button) view.findViewById(R.id.btnServiceStop);
	        btnServiceStop.setOnClickListener(this);
	        
	        btnt_write = (ToggleButton) view.findViewById(R.id.btnt_write);
	        btnt_write.setChecked(Config.write);
	        btnt_write.setOnCheckedChangeListener(this);
	        
	        console = (TextView) view.findViewById(R.id.console);
	        console.setMovementMethod(new ScrollingMovementMethod());
	        
	        
	        
	        return view;
	    }
		
		private void setText(String s){
			console.setText(console.getText() + s + "\n");
		}
		
		
		@Override
		public void onClick(View v) {

			switch(v.getId()){
				case R.id.btnServiceStart:
					ocServiceStart();
					break;
				case R.id.btnServiceStop:
					ocServiceStop();
					break;
				default:
					break;
			}
		}
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			
			switch(buttonView.getId()){
				case R.id.btnt_gps:
						occBtntGps(isChecked);
						break;
				case R.id.btnt_write:
						occBtntWrite(isChecked);
						break;
				default:
						break;
			}
		}


		private void occBtntWrite(boolean isChecked) {

			Config.write = !Config.write;
			setText(""+Config.write);
			
			sendBR();
			
		}
		
		private void sendBR(){
			Intent intent = new Intent(MyService.BROADCAST_ACTION);
			intent.putExtra(MyService.PARAM_TASK, 1);
	        intent.putExtra(MyService.PARAM_DATA, Config.write);
	        getActivity().sendBroadcast(intent);
		}

		private void occBtntGps(boolean isChecked) {
			Toasts.showGreenMessage("KLIK");
			btnt_gps.setChecked(false);
			
		}

		private void ocServiceStart() {
			
			if(true){
				
				if(!(locman.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
					showSettingsAlert();
				} else {
					Intent i = new Intent(getActivity(), MyService.class);
					i.putExtra("write", Config.write);
					getActivity().startService(i);
				}
				
				
				sendBR();
		
			}
		}
		
		private void ocServiceStop() {
			getActivity().stopService(new Intent(getActivity(), MyService.class));
		}
		
	
		/**
	     * Function to show settings alert dialog
	     * */
	    public void showSettingsAlert(){
	        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
	      
	        // Setting Dialog Title
	        alertDialog.setTitle("GPS is settings");
	  
	        // Setting Dialog Message
	        alertDialog.setMessage("Do you want to go to settings menu?");
	  
	        // Setting Icon to Dialog
	        //alertDialog.setIcon(R.drawable.delete);
	  
	        // On pressing Settings button
	        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                intent.setFlags(Intent.FLAG_FROM_BACKGROUND);
	                startActivity(intent);
	            }
	        });
	  
	        // on pressing cancel button
	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            dialog.cancel();
	            }
	        });
	  
	        // Showing Alert Message
	        alertDialog.show();
	    }
		
	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        setUserVisibleHint(true);
	    }


	 
	}
