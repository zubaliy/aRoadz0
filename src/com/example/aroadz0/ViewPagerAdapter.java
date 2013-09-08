package com.example.aroadz0;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class ViewPagerAdapter extends FragmentPagerAdapter {
 
    // Declare the number of ViewPager pages
    final int PAGE_COUNT = 5;
 
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int arg0) {
        switch (arg0) {
 
        // Open FragmentTab1.java
        case 0:
            FragmentTab_Home fragmenttab1 = new FragmentTab_Home();
            return fragmenttab1;
 
        // Open FragmentTab2.java
        case 1:
            FragmentTab_Graph fragmenttab2 = new FragmentTab_Graph();
            return fragmenttab2;
 
        // Open FragmentTab3.java
        case 2:
        	FragmentTab_Map fragmenttab3 = new FragmentTab_Map();
            return fragmenttab3;
        
	     // Open FragmentTab4.java
	    case 3:
	    	FragmentTab_Profile fragmenttab4 = new FragmentTab_Profile();
	        return fragmenttab4;
	    
		 // Open FragmentTab5.java
		case 4:
			FragmentTab_About fragmenttab5 = new FragmentTab_About();
		    return fragmenttab5;
		}
        return null;
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return PAGE_COUNT;
    }
 
}