package com.capsule.shellfies.Activities;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.capsule.shellfies.Helpers.Constants;
import com.capsule.shellfies.Interfaces.IntContainer;
import com.iapps.libs.helpers.BaseUIHelper;
import com.verano.actionbar4guice.activity.RoboActionBarActivity;

public class BaseActivityShellfies extends RoboActionBarActivity
		implements IntContainer {
	private int	containerId;

	// ================================================================================
	// Fragment Functions
	// ================================================================================
	/**
	 * Add fragment on top of the current one
	 * 
	 * @param frag
	 */
	public void addFragment(Fragment frag) {
		if (this.containerId > 0) {
			getSupportFragmentManager().beginTransaction()
					.add(this.containerId, frag).addToBackStack(null).commit();

			getSupportActionBar().setDisplayHomeAsUpEnabled(false);

			// Hide keyboard by default
			BaseUIHelper.hideKeyboard(this);
		}
	}

	/**
	 * Change to new fragment
	 * 
	 * @param frag
	 */
	public void setFragment(Fragment frag) {
		if (this.containerId > 0) {
			getSupportFragmentManager().beginTransaction()
					.replace(this.containerId, frag).addToBackStack(null)
					.commit();

			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			setSupportProgressBarIndeterminateVisibility(false);

			// Hide keyboard by default when changing fragment
			BaseUIHelper.hideKeyboard(this);
		}
	}

	/**
	 * Clear all fragments
	 */
	public void clearFragment() {
		getSupportFragmentManager().popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	/**
	 * Remove top of fragments
	 */
	public void popBackstack() {
		getSupportFragmentManager().popBackStack();
	}

	/**
	 * MUST set if want to use fragment methods
	 */
	public void setContainerId(int containerId) {
		this.containerId = containerId;
	}

	// ================================================================================
	// Behavior Controller
	// ================================================================================
	/**
	 * Controls behavior of the back button
	 */
	@Override
	public void onBackPressed() {
		// Remove circle loading progress on top right corner
		setSupportProgressBarIndeterminateVisibility(false);

		// Only close apps when there's no backstack
		if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
			BaseActivityShellfies.this.finish();
		} else {
			super.onBackPressed();
		}
	}

	// ================================================================================
	// Hide / Show ActionBar functions
	// ================================================================================
	public void hideTopBar() {
		getSupportActionBar().hide();
	}

	public void showTopBar() {
		getSupportActionBar().show();
	}

	// ================================================================================
	// Commonly used functions
	// ================================================================================
	public void dismissDialog() {
		DialogFragment dialogFragment = (DialogFragment) getSupportFragmentManager()
				.findFragmentByTag(Constants.DIALOG);

		if (dialogFragment != null)
			dialogFragment.dismiss();
	}
}
