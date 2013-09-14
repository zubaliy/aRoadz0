package com.example.aroadz0;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;


	 
	public class FragmentTab_About extends SherlockFragment  implements OnClickListener{
	 
	    private Button btnVisualise;

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {      
	        
	        // Get the view from fragment_about.xml
	        View view = inflater.inflate(R.layout.fragment_about, container, false);
	        
	        btnVisualise = (Button) view.findViewById(R.id.btnVisualise);
	    	btnVisualise.setOnClickListener(this);
	        
	        return view;
	    }
	 
	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        setUserVisibleHint(true);
	    }
	    
	    
		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.btnVisualise:
					btnVisualize();
					break;
				default:
					break;
			}
			
		}
		
		private void btnVisualize() {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://aroadz.herokuapp.com/visualise"));
			startActivity(intent);
			
		}
	 
	}
