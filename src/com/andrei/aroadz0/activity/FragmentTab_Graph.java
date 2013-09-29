package com.andrei.aroadz0.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.andrei.aroadz0.controller.Accelerometer;
import com.andrei.aroadz0.controller.DataCombiner;
import com.andrei.aroadz0.model.Graph;
import com.andrei.aroadz0.R;


	 
	public class FragmentTab_Graph extends SherlockFragment {
		
		private final String LOG_TAG = "zGraph"; 
		
		private Accelerometer acc = null;
		private Graph graph = null;

		private View view;
		

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    	
	        // Get the view from fragment_graph.xml
	        view = inflater.inflate(R.layout.fragment_graph, container, false);
	        
    	
	        graph = new Graph(view);
   
	        acc = new Accelerometer(getActivity().getApplicationContext());
	        acc.addObserver(graph);
	        //datacomb = new DataCombiner(getActivity().getApplicationContext());
	        //datacomb.addObserver(graph);

	        Log.i(LOG_TAG, "onCreateView()");
	        return view;
	    }
	 


		@Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        setUserVisibleHint(true);
	    }
	    
	    @Override
	    public void onResume() {
	    	Log.i(LOG_TAG, "onResume()");
	    	acc.addListeners();
            super.onResume();
	    }
	    
	    @Override
	    public void onPause() {
	    	Log.i(LOG_TAG, "onPause()");
	    	acc.removeListeners();
	    	super.onPause();
	    }
	    
	    @Override
	    public void onStart() {
	    	Log.i(LOG_TAG, "onStart()");
	    	super.onStart();
	    }
	    
	    @Override
		public void onStop() {
            Log.i(LOG_TAG, "onStop()");
            super.onStop();
        }
 
        @Override
		public void onDestroy() {
            Log.i(LOG_TAG, "onDestroy()");
            super.onDestroy();
        }


}
