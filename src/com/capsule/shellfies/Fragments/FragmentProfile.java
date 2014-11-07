package com.capsule.shellfies.Fragments;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.capsule.shellfies.R;
import com.iapps.libs.views.ImageViewLoader;

public class FragmentProfile extends BaseFragmentShellfies {
	@InjectView(R.id.imgProfile)
	private ImageViewLoader	imgProfile;
	@InjectView(R.id.tvName)
	private TextView		tvName;
	@InjectView(R.id.tvCountPhoto)
	private TextView		tvCountPhoto;
	@InjectView(R.id.tvCountFollow)
	private TextView		tvCountFollow;
	@InjectView(R.id.tvCountFollower)
	private TextView		tvCountFollower;
	@InjectView(R.id.flGrid)
	private FrameLayout		flGrid;
	@InjectView(R.id.llHeader)
	private LinearLayout	llHeader;

	private String			name;

	public FragmentProfile(String name) {
		this.name = name;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_profile, container, false);

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		init();
	}

	public void init() {
		imgProfile
				.loadImage(
						"https://scontent-a-nrt.xx.fbcdn.net/hphotos-xpa1/v/t1.0-9/1525618_10201224757476334_1642072316_n.jpg?oh=fef346408d5b9dae9cc97c95126d8fc3&oe=54F5F9CB",
						true);
		tvName.setText(name);
		tvCountFollow.setText("100");
		tvCountFollower.setText("91");
		tvCountPhoto.setText("11");

		getActionbar().setTitle(name);
		getActionbar().setDisplayHomeAsUpEnabled(true);
		getHome().showTopBar();

		// Add top margin to the header info
		LinearLayout.LayoutParams params = (LayoutParams) llHeader.getLayoutParams();
		params.topMargin = (int) (getHome().getSupportActionBar().getHeight() + getActivity()
				.getResources().getDimension(R.dimen.margin_small));
		llHeader.setLayoutParams(params);

		getActivity()
				.getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.flGrid,
						new FragmentGrid(FragmentGrid.API_LOAD_IMAGES_PROFILE))
				.commit();
	}
}
