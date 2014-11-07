package com.capsule.shellfies.Activities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;

import com.capsule.shellfies.R;
import com.capsule.shellfies.Helpers.Constants;
import com.capsule.shellfies.Helpers.Preference;
import com.capsule.shellfies.Interfaces.IntContainer;
import com.facebook.AppEventsLogger;
import com.iapps.libs.helpers.BaseUIHelper;
import com.verano.actionbar4guice.activity.RoboActionBarActivity;

public class BaseActivityShellfies extends RoboActionBarActivity
		implements IntContainer {

	private Drawable	bgActionbar;
	private int			containerId;

	@Override
	protected void onStart() {
		super.onStart();

		// Facebook statistics
		AppEventsLogger.activateApp(this);

		if (Constants.IS_DEBUGGING)
			printHash();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		// Facebook statistics
		AppEventsLogger.deactivateApp(this);
	}

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
			// setSupportProgressBarIndeterminateVisibility(false);

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
			finish();
		} else {
			super.onBackPressed();
		}
	}

	// ================================================================================
	// Hide / Show ActionBar functions
	// ================================================================================
	public void hideTopBar() {
		if (bgActionbar == null)
			bgActionbar = getResources().getDrawable(R.drawable.bg_actionbar);

		bgActionbar.setAlpha(0);
		getSupportActionBar().setBackgroundDrawable(bgActionbar);
		getSupportActionBar().setTitle("");
	}

	public void showTopBar() {
		if (bgActionbar == null)
			bgActionbar = getResources().getDrawable(R.drawable.bg_actionbar);

		bgActionbar.setAlpha(255);
		getSupportActionBar().setBackgroundDrawable(bgActionbar);
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

	public Preference getPref() {
		return Preference.getInstance(this);
	}

	// ================================================================================
	// Credentials
	// ================================================================================
	public void printHash() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.capsule.shellfies", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d(Constants.LOG, "Hash : " + Base64.encodeToString(md.digest(), Base64.DEFAULT));

			}
		}
		catch (NameNotFoundException e) {

		}
		catch (NoSuchAlgorithmException e) {

		}
	}
}
