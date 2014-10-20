package com.capsule.shellfies.Fragments;

import java.util.Calendar;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;

import com.capsule.shellfies.Activities.ActivityHome;
import com.capsule.shellfies.Activities.BaseActivityShellfies;
import com.capsule.shellfies.Helpers.Api;
import com.capsule.shellfies.Helpers.Constants;
import com.capsule.shellfies.Helpers.Converter;
import com.capsule.shellfies.Helpers.PopupImage;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

public class BaseFragmentShellfies extends RoboSherlockFragment {
	private long				actionbarDelay	= 0;	// In millis

	public Converter			converter;
	public Api					api;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
	public void displayActionBar(float curY, float prevY) {
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
			getHome().hideTopBar();
			getHome().hideFooter();

			actionbarDelay = curTime;
		}
	}

	// ================================================================================
	// Commonly used functions
	// ================================================================================
	public void showImageDetails(String url) {
		DialogFragment newFragment = PopupImage.newInstance(url);
		newFragment.show(getFragmentManager(), "dialog");
	}

	public void initLoad() {
		this.converter = Converter.getInstance(getActivity());
		this.api = Api.getInstance(getActivity());
	}

	public PullToRefreshLayout getPullToRefresh(View view, int resId, OnRefreshListener listener) {
		PullToRefreshLayout mPullToRefresh = new PullToRefreshLayout(view.getContext());

		ActionBarPullToRefresh.from(getActivity()).insertLayoutInto((ViewGroup) view)
				.theseChildrenArePullable(resId)
				.listener(listener).setup(mPullToRefresh);

		return mPullToRefresh;
	}
}
