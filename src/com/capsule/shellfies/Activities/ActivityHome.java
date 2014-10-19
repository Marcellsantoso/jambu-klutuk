package com.capsule.shellfies.Activities;

import java.util.Calendar;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.capsule.shellfies.R;
import com.capsule.shellfies.Fragments.FragmentGrid;
import com.capsule.shellfies.Helpers.Constants;

public class ActivityHome extends BaseActivityShellfies {
	@InjectView(R.id.llFooter)
	private LinearLayout llFooter;
	
	private long footerDelay = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_home);

		setContainerId(R.id.flFragments);

		setFragment(new FragmentGrid());
	}

	public void hideFooter() {		
		if (llFooter.getVisibility() != View.GONE && isDelayFinished()) {
			Animation slide = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.slide_down);
			llFooter.startAnimation(slide);
			llFooter.setVisibility(View.GONE);
		}
	}
	
	public void showFooter(){
		if (llFooter.getVisibility() != View.VISIBLE && isDelayFinished()) {
			Animation slide = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.slide_up);
			llFooter.startAnimation(slide);
			llFooter.setVisibility(View.VISIBLE);
		}
	}
	
	public boolean isDelayFinished(){
		long curTime = Calendar.getInstance().getTimeInMillis();
		long diffTime = curTime - footerDelay;
		
		this.footerDelay = curTime;
		
		return diffTime > Constants.ACTIONBAR_DELAY;
	}
}
