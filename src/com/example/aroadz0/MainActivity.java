package com.example.aroadz0;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
 
import android.os.Bundle;
import android.widget.Toast;
 

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;


public class MainActivity extends SherlockFragmentActivity {
 
	// Declare Variables
    private ActionBar mActionBar;
    private ViewPager mPager;
    private Tab tab;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_main);
        
     // Activate Navigation Mode Tabs
        mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
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
        tab = mActionBar.newTab().setText("Graph").setTabListener(tabListener);
        mActionBar.addTab(tab);
 
        // Create third Tab
        tab = mActionBar.newTab().setText("Map").setTabListener(tabListener);
        mActionBar.addTab(tab);
        
        // Create firth Tab
        tab = mActionBar.newTab().setText("Profile").setTabListener(tabListener);
        mActionBar.addTab(tab);
        
        // Create fifth Tab
        tab = mActionBar.newTab().setText("About").setTabListener(tabListener);
        mActionBar.addTab(tab);
 
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
 
        // First Menu Button
        menu.add("Help")
                .setOnMenuItemClickListener(this.HelpButtonClickListener)
                .setIcon(R.drawable.help_button) // Set the menu icon
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
 
        // Second Menu Button
        menu.add("Like")
                .setOnMenuItemClickListener(this.LikeButtonClickListener)
                .setIcon(R.drawable.like_button) // Set the menu icon
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        
 
        // Third Menu Button
        menu.add("Exit")
                .setOnMenuItemClickListener(this.ExitButtonClickListener)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
 
        return super.onCreateOptionsMenu(menu);
    }
 
    // Capture first menu button click
    OnMenuItemClickListener HelpButtonClickListener = new OnMenuItemClickListener() {
 
        public boolean onMenuItemClick(MenuItem item) {
 
            // Create a simple toast message
            Toast.makeText(MainActivity.this, "Help Button", Toast.LENGTH_SHORT)
                    .show();
 
            // Do something else
            return false;
        }
    };
 
    // Capture second menu button click
    OnMenuItemClickListener LikeButtonClickListener = new OnMenuItemClickListener() {
 
        public boolean onMenuItemClick(MenuItem item) {
            // Create a simple toast message
            Toast.makeText(MainActivity.this, "Like Button", Toast.LENGTH_SHORT)
                    .show();
 
            // Do something else
            return false;
        }
    };
 
    // Capture third menu button click
    OnMenuItemClickListener ExitButtonClickListener = new OnMenuItemClickListener() {
 
        public boolean onMenuItemClick(MenuItem item) {
            // Create a simple toast message
            Toast.makeText(MainActivity.this, "Exit Button", Toast.LENGTH_SHORT)
                    .show();
 
            // Do something else
            return false;
        }
    };
}