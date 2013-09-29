package com.andrei.aroadz0.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.andrei.aroadz0.R;



	
	 
	public class FragmentTab_Profile extends SherlockFragment {

		private final String LOG_TAG = "zTabProfile";
	 
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        // Get the view from fragmenttab1.xml
	        View view = inflater.inflate(R.layout.fragment_profile, container, false);
	        
	        Log.d(LOG_TAG, "onCreateView()");
	        return view;
	    }
	 
	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        setUserVisibleHint(true);
	    }
	 
	}
