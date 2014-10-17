package com.capsule.shellfies.Activities;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.capsule.shellfies.R;
import com.capsule.shellfies.Fragments.FragmentGrid;

public class ActivityHome extends BaseActivityShellfies{
	@InjectView (R.id.llFooter) private LinearLayout llFooter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		setContainerId(R.id.flFragments);
		
		setFragment(new FragmentGrid());
	}
	
}
