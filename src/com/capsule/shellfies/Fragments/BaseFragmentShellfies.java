package com.capsule.shellfies.Fragments;

import java.util.ArrayList;
import java.util.Calendar;

import roboguice.fragment.RoboFragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.View;

import com.capsule.shellfies.Activities.ActivityHome;
import com.capsule.shellfies.Activities.BaseActivityShellfies;
import com.capsule.shellfies.Helpers.Api;
import com.capsule.shellfies.Helpers.Constants;
import com.capsule.shellfies.Helpers.Converter;
import com.capsule.shellfies.Helpers.PopupImage;
import com.capsule.shellfies.Helpers.Preference;
import com.capsule.shellfies.Objects.BeanImage;

public class BaseFragmentShellfies extends RoboFragment {
	private long		actionbarDelay	= 0;	// In millis

	public Converter	converter;
	public Api			api;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initLoad();
	}

	// ================================================================================
	// Base Functions
	// ================================================================================
	public BaseActivityShellfies getBaseActivity() {
		return (BaseActivityShellfies) getActivity();
	}

	public ActivityHome getHome() {
		return (ActivityHome) getActivity();
	}

	// ================================================================================
	// Hide / Show ActionBar
	// ================================================================================
	public void displayActionBar(float curY, float prevY, boolean isHideTop, boolean isHideBot) {
		// Check delay
		long curTime = Calendar.getInstance().getTimeInMillis();
		long diffTime = curTime - actionbarDelay;

		if ((curY < prevY || curY == 0)
				&& diffTime > Constants.ACTIONBAR_DELAY) {
			// Scroll down
			getHome().showTopBar();
			getHome().showFooter();

			actionbarDelay = curTime;
		} else if ((curY > prevY || curY == 1)
				&& diffTime > Constants.ACTIONBAR_DELAY) {
			// Scroll up
			if (isHideTop)
				getHome().hideTopBar();

			if (isHideBot)
				getHome().hideFooter();

			actionbarDelay = curTime;
		}
	}

	// ================================================================================
	// Commonly used functions
	// ================================================================================
	public void showImageDetails(ArrayList<BeanImage> alImages, int position) {
		DialogFragment newFragment = new PopupImage(alImages, position);
		newFragment.show(getActivity().getSupportFragmentManager(), Constants.DIALOG);
	}

	public Preference getPref() {
		return getBaseActivity().getPref();
	}

	// ================================================================================
	// Initializer Function
	// ================================================================================
	public void initLoad() {
		this.converter = Converter.getInstance(getActivity());
		this.api = Api.getInstance(getActivity());
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

}
