package com.capsule.shellfies.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capsule.shellfies.R;

public class FragmentHome extends BaseFragmentShellfies {

	private FragmentGrid	grid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_home, container, false);

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getHome().hideTopBar();
		getActionbar().setDisplayHomeAsUpEnabled(false);

		if (grid == null) {
			grid = new FragmentGrid(FragmentGrid.API_LOAD_IMAGES_TIMELINE);
		}

		setFragment(grid, R.id.flHome);
	}
}
