package com.example.aroadz0;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;



	
	 
	public class FragmentTab_Map extends SherlockFragment {
	 
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        // Get the view from fragmenttab1.xml
	        View view = inflater.inflate(R.layout.fragment_map, container, false);
	        return view;
	    }
	 
	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        setUserVisibleHint(true);
	    }
	 
	}
