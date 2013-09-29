package com.andrei.aroadz0.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.andrei.aroadz0.R;
import com.andrei.aroadz0.utils.Toasts;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


	 
	public class FragmentTab_Map extends SherlockFragment implements OnCheckedChangeListener {

		private final String LOG_TAG = "zTabMap";
		
		private View view = null;
		
	    private ToggleButton btnt_gps = null;

		private GoogleMap googleMap = null;



		
		
		
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        // Get the view from fragment_map.xml
       	        
	        if (view != null) {
	        	/*	problem solved
	        	 *  http://stackoverflow.com/a/14695397/1894473
	        	 */
	            ViewGroup parent = (ViewGroup) view.getParent();
	            if (parent != null)
	                parent.removeView(view);
	        }
	        
	        try {
	            view = inflater.inflate(R.layout.fragment_map, container, false);
	        } catch (InflateException e) {
	            /* map is already there, just return view as it is */
	        	Log.d(LOG_TAG, "InflateException");
	        }
	        

	        

           // Loading map
           initilizeMap();
	        
	        
	        return view;
	    }
		
	    private void initilizeMap() {
	    	if (googleMap  == null) {
	            googleMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	            googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
	            // check if map is created successfully or not
	            if (googleMap == null) {
	                Toast.makeText(getActivity().getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
	            }
	        }
			
		}

		@Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        setUserVisibleHint(true);
	    }
	    
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			
			switch(buttonView.getId()){
				case R.id.btnt_gps:
					occBtntGps(isChecked);
					break;
				default:
					break;
			}
		}

		private void occBtntGps(boolean isChecked) {
			Toasts.showGreenMessage("KLIK");
			btnt_gps.setChecked(false);
			
		}
		
		@Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        Log.d(LOG_TAG, "onDestroyView()");
		}
		
	 
	}
