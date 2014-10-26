package com.capsule.shellfies.Activities;

import java.util.Calendar;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.capricorn.RayMenu;
import com.capsule.shellfies.R;
import com.capsule.shellfies.Fragments.FragmentGrid;
import com.capsule.shellfies.Helpers.Constants;

public class ActivityHome extends BaseActivityShellfies {
	@InjectView(R.id.llFooter)
	private LinearLayout	llFooter;
	@InjectView(R.id.menu)
	private RayMenu			menu;

	private long			footerDelay	= 0;
	private int[]			arrMenu		= {
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher
										};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		setContainerId(R.id.flFragments);
		initMenu();

		setFragment(new FragmentGrid(true, true));
	}

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
	// Menu functions
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

		}

	}
}