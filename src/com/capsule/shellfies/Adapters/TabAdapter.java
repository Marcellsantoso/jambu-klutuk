package com.capsule.shellfies.Adapters;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

public class TabAdapter
	extends FragmentStatePagerAdapter {

	ArrayList<Fragment> alFrag;

	public TabAdapter(FragmentManager fm, ArrayList<Fragment> alFrag) {
		super(fm);
		this.alFrag = alFrag;
	}

	@Override
	public Fragment getItem(int arg0) {
		return alFrag.get(arg0);
	}

	@Override
	public int getCount() {
		return alFrag.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

}
