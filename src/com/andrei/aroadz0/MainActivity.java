package com.andrei.aroadz0;


import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
 
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.Settings;
 


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.andrei.aroadz0.activity.ViewPagerAdapter;
import com.andrei.aroadz0.controller.Gps;
import com.andrei.aroadz0.utils.Config;
import com.andrei.aroadz0.R;


public class MainActivity extends SherlockFragmentActivity {

	private final String LOG_TAG = "zMainActivity";
	
	// Declare Variables
    private ActionBar mActionBar;
    private ViewPager mPager;
    private Tab tab;
    private Menu menu;

	
	private BroadcastReceiver br = null;
	public final static String BROADCAST_ACTION = "com.andrei.aroadz0.service.MainActivity";

	public final static String PARAM_TASK = "task";
	public final static String PARAM_DATA = "data";

	private final int TASK1_CODE = 1;


 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_main);
       
        // Initialize and pass activity to Config
        Config.init(this);
        
        createBR();
 
        
        // Activate Navigation Mode Tabs
        mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
       
        
        // Locate ViewPager in activity_main.xml
        mPager = (ViewPager) findViewById(R.id.pager);
 
        // Activate Fragment Manager
        FragmentManager fm = getSupportFragmentManager();
 
        // Capture ViewPager page swipes
        ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Find the ViewPager Position
                mActionBar.setSelectedNavigationItem(position);
            }
        };
        
        mPager.setOnPageChangeListener(ViewPagerListener);
        // Locate the adapter class called ViewPagerAdapter.java
        ViewPagerAdapter viewpageradapter = new ViewPagerAdapter(fm);
        // Set the View Pager Adapter into ViewPager
        mPager.setAdapter(viewpageradapter);
        
        
        
        // Capture tab button clicks
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
 
            @Override
            public void onTabSelected(Tab tab, FragmentTransaction ft) {
                // Pass the position on tab click to ViewPager
            	
                mPager.setCurrentItem(tab.getPosition());
            }
 
            @Override
            public void onTabUnselected(Tab tab, FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }
 
            @Override
            public void onTabReselected(Tab tab, FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }
        };
 
        // Create first Tab
        tab = mActionBar.newTab().setText("Home").setTabListener(tabListener);
        mActionBar.addTab(tab);
 
        // Create second Tab
        tab = mActionBar.newTab().setText("Map").setTabListener(tabListener);
        mActionBar.addTab(tab);
 
        // Create third Tab
        tab = mActionBar.newTab().setText("Graph").setTabListener(tabListener);
        mActionBar.addTab(tab);
        
        // Create firth Tab
        tab = mActionBar.newTab().setText("Profile").setTabListener(tabListener);
        mActionBar.addTab(tab);
        
        // Create fifth Tab
//        tab = mActionBar.newTab().setText("About").setTabListener(tabListener);
//        mActionBar.addTab(tab);
        
        

        Log.d(LOG_TAG, "App started");
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	this.menu = menu;
    	
//        // First Menu Button
//        menu.add("Help")
//                .setOnMenuItemClickListener(this.HelpButtonClickListener)
//                .setIcon(R.drawable.help_button) // Set the menu icon
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

 
    	getSupportMenuInflater().inflate(R.menu.mymenu, menu);
    	
    	isGpsEnabled();
    	Log.d(LOG_TAG, "onCreateOptionsMenu()");
    	
        return super.onCreateOptionsMenu(menu);
    }
    
    public void mnHelp(MenuItem item) {
    	Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
    }
    public void mnLike(MenuItem item) {
    	Toast.makeText(this, "Like", Toast.LENGTH_SHORT).show();
    }
    public void mnGps(MenuItem item) {
    	
    	isGpsEnabled();
    	showGpsSettingsAlert();

    }
    public void mnExit(MenuItem item) {
    	Toast.makeText(this, "aRoadz has stopped", Toast.LENGTH_SHORT).show();
    	// Disappear from screen. Activity is still running. Same as pressing Home-button. 
    	finish();
    	// killing Activity-process. Service is still running
    	//System.exit(0);
    }
    


    private void createBR() {
		// создаем BroadcastReceiver
		br = new BroadcastReceiver() {
			// действия при получении сообщений
			public void onReceive(Context context, Intent intent) {
				int task = intent.getIntExtra(PARAM_TASK, 0);
				String data = intent.getStringExtra(PARAM_DATA);
				Log.d(LOG_TAG, "onReceive: task = " + task + ", data = " + data);

				// Ловим сообщения о старте задач
				switch (task) {
					case TASK1_CODE:
						showGpsSettingsAlert();
						break;
					default: 
						break;
				}
			}
		};

	    // создаем фильтр для BroadcastReceiver
	    IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
	    // регистрируем (включаем) BroadcastReceiver
	    registerReceiver(br, intFilt);

	}
    
	/**
     * Function to show settings alert dialog
     * */
    public void showGpsSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
      
        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");
  
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
    
    private boolean isGpsEnabled() {
    	boolean isGpsEnabled = ((LocationManager)getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
	    	if(menu != null){
		    	MenuItem item = (MenuItem) menu.findItem(R.id.menuGps);	
		    	if ( isGpsEnabled ) {
		    		item.setIcon(R.drawable.gps_on);	
		    	} else {
		    		item.setIcon(R.drawable.gps_off);
		    	}
	    	}
		return isGpsEnabled;
    }


	@Override
	protected void onStart() {
		Log.d(LOG_TAG, "onStart()");
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		Log.d(LOG_TAG, "onResume()");
		isGpsEnabled();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Log.d(LOG_TAG, "onPause()");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.d(LOG_TAG, "onStop()");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.d(LOG_TAG, "onDestroy()");
		super.onDestroy();
		
		unregisterReceiver(br);
		
	}
	
	
   
    
    
    

}