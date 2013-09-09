package com.example.aroadz0;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;



	
	 
	public class FragmentTab_Home extends SherlockFragment {
	 
	    private Button btn_menu;

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	    	
	    	
	    	
	    	
	        // Get the view from fragmenttab1.xml
	        View view = inflater.inflate(R.layout.fragment_home, container, false);
	        
	        btn_menu = (Button) view.findViewById(R.id.button1);
	        btn_menu.setText("dfsd");
	        btn_menu.setOnClickListener(new OnClickListener() {
	    		public void onClick(View v) {
	    			
	    			
	    		}
	    	});
	        
	        return view;
	    }
	 
	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        setUserVisibleHint(true);
	    }
	 
	}
