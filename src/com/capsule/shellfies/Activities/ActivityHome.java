package com.capsule.shellfies.Activities;

import java.util.Calendar;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.capricorn.RayMenu;
import com.capsule.shellfies.R;
import com.capsule.shellfies.Fragments.FragmentHome;
import com.capsule.shellfies.Fragments.FragmentProfile;
import com.capsule.shellfies.Fragments.FragmentUpload;
import com.capsule.shellfies.Helpers.Constants;
import com.capsule.shellfies.Helpers.Helper;

public class ActivityHome extends BaseActivityShellfies {
	@InjectView(R.id.llFooter)
	private LinearLayout	llFooter;
	@InjectView(R.id.menu)
	private RayMenu			menu;

	private long			footerDelay		= 0;
	private int[]			arrMenu			= {
			R.drawable.ic_profile, R.drawable.ic_upload,
			R.drawable.ic_timeline
											};
	private boolean			isActionChange	= false;

	private final int		TAG_PROFILE		= 0, TAG_UPLOAD = 1, TAG_TIMELINE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setContainerId(R.id.flFragments);

		// Default title is empty
		getSupportActionBar().setTitle("");

		initMenu();

		setFragment(new FragmentHome());
	}

	// ================================================================================
	// Footer functions
	// ================================================================================
	public void hideFooter() {
		if (llFooter.getVisibility() != View.GONE && isDelayFinished()) {
			Animation slide = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.slide_down);
			llFooter.startAnimation(slide);
			llFooter.setVisibility(View.GONE);
		}
	}

	public void showFooter() {
		if (llFooter.getVisibility() != View.VISIBLE && isDelayFinished()) {
			Animation slide = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.slide_up);
			llFooter.startAnimation(slide);
			llFooter.setVisibility(View.VISIBLE);
		}
	}

	public boolean isDelayFinished() {
		long curTime = Calendar.getInstance().getTimeInMillis();
		long diffTime = curTime - footerDelay;

		this.footerDelay = curTime;

		return diffTime > Constants.ACTIONBAR_DELAY;
	}

	// ================================================================================
	// Image functions
	// ================================================================================
	public void launchCamera() {
		Helper.pickImage(ActivityHome.this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == -1 && requestCode == Helper.REQUEST_GET_IMAGE_CODE) {
			setFragment(new FragmentUpload());
		}

	}

	// ================================================================================
	// ArcMenu functions
	// ================================================================================
	public void initMenu() {
		menu.getRayLayout().setChildSize((int) getResources().getDimension(R.dimen.menu_child));

		for (int i = 0; i < arrMenu.length; i++) {
			ImageView img = new ImageView(this);
			img.setImageResource(arrMenu[i]);

			menu.addItem(img, new ListenerMenu(i));
		}
	}

	public class ListenerMenu implements OnClickListener {

		int	position;

		public ListenerMenu(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (position) {
				case TAG_PROFILE:
					setFragment(new FragmentProfile("Marcel"));
					break;

				case TAG_UPLOAD:
					launchCamera();
					break;

				case TAG_TIMELINE:
					break;
			}
		}
	}

	// ================================================================================
	// ActionBar functions
	// ================================================================================
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	// ================================================================================
	// Callback functions
	// ================================================================================
	public boolean isActionChanged() {
		return this.isActionChange;
	}

	public void setActionChange(boolean isActionChange) {
		this.isActionChange = isActionChange;
	}
}