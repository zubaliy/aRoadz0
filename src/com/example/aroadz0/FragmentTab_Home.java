package com.example.aroadz0;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
/*
 * getActivity(). - Используется если нужно вызвать метод из MainActivity  
 * 
 */
 
	public class FragmentTab_Home extends SherlockFragment implements OnClickListener{
	 
	    private Button btn_menu;
	    private Button btnServiceStart;
	    private Button btnServiceStop;
	 
	    
	    final String LOG_TAG = "zService";

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	    	
	        // Get the view from fragment_home.xml
	        View view = inflater.inflate(R.layout.fragment_home, container, false);
	        
	        btn_menu = (Button) view.findViewById(R.id.button1);
	        btn_menu.setOnClickListener(this);
	        
	        btnServiceStart = (Button) view.findViewById(R.id.btnServiceStart);
	        btnServiceStart.setOnClickListener(this);
	        
	        btnServiceStop = (Button) view.findViewById(R.id.btnServiceStop);
	        btnServiceStop.setOnClickListener(this);
	        
	        
	        return view;
	    }
		
		@Override
		public void onClick(View v) {

			switch(v.getId()){
				case R.id.button1:
					btnNotification();
					break;
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
		
		private void ocServiceStart() {
			getActivity().startService(new Intent(getActivity(), MyService.class));
		}
		
		private void ocServiceStop() {
			getActivity().stopService(new Intent(getActivity(), MyService.class));
		}

		private void btnNotification() {
			btn_menu.setText("Notification");
			Notification();
			Log.d(LOG_TAG, "Notification()");
		}

		public void Notification() {
	       
	    }
		
	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        setUserVisibleHint(true);
	    }

		
	 
	}
