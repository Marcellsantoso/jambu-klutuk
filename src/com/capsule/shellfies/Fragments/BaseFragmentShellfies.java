package com.capsule.shellfies.Fragments;

import java.util.Calendar;

import com.capsule.shellfies.Activities.ActivityHome;
import com.capsule.shellfies.Activities.BaseActivityShellfies;
import com.capsule.shellfies.Helpers.ArtbookFeed;
import com.capsule.shellfies.Helpers.Constants;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

public class BaseFragmentShellfies extends RoboSherlockFragment{
	private long actionbarDelay = 0; // In millis
	
	//================================================================================
    // Base Functions
    //================================================================================
	public BaseActivityShellfies getBaseActivity(){
		return (BaseActivityShellfies) getActivity();
	}
	
	public ActivityHome getHome(){
		return (ActivityHome) getActivity();
	}
	
	//================================================================================
    // FreeFlow Functions
    //================================================================================
	public void onDataLoaded(ArtbookFeed feed){
		// Empty function
	};
	
	
	//================================================================================
    // Hide / Show ActionBar
    //================================================================================
	public void displayActionBar(float curY, float prevY){
		// Check delay
		long curTime = Calendar.getInstance().getTimeInMillis();
		long diffTime = curTime - actionbarDelay;
		
		if ((curY < prevY || curY == 0) && diffTime > Constants.ACTIONBAR_DELAY) {
			// Scroll down
			getHome().showTopBar();
			getHome().showFooter();
			
			actionbarDelay = curTime;
		} else if ((curY > prevY || curY == 1) && diffTime > Constants.ACTIONBAR_DELAY) {
			// Scroll up
			getHome().hideTopBar();
			getHome().hideFooter();
			
			actionbarDelay = curTime;
		}
	}
}
