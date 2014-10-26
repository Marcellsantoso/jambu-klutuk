package com.capsule.shellfies.Helpers;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.capsule.shellfies.R;
import com.capsule.shellfies.Adapters.TabAdapter;
import com.capsule.shellfies.Fragments.FragmentImageDetails;
import com.capsule.shellfies.Objects.BeanImage;

public class PopupImage extends DialogFragment {
	private ViewPager				vp;
	private TabAdapter				adapter;
	private ArrayList<Fragment>		alFrag		= new ArrayList<Fragment>();
	private ArrayList<BeanImage>	alImages	= new ArrayList<BeanImage>();
	private int						position	= 0;

	public PopupImage(ArrayList<BeanImage> alImages, int position) {
		this.alImages = alImages;
		this.position = position;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.popup_image, container, false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		return v;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		vp = (ViewPager) view.findViewById(R.id.vpPopup);

		int screenMargin = (int) UIHelper
				.convertDpToPixel(
						(int) getActivity().getResources().getDimension(
								R.dimen.activity_horizontal_margin),
						getActivity());
		vp.getLayoutParams().height = UIHelper.getScreenWidth(getActivity()) - screenMargin;

		init();
	}

	@Override
	public void onStart() {
		super.onStart();

		// safety check
		if (getDialog() == null) {
			return;
		}

		// set the animations to use on showing and hiding the dialog
		getDialog().getWindow().setWindowAnimations(
				R.style.dialog_animation_fade);
	}

	public void init() {
		populateFragment();
		adapter = new TabAdapter(getChildFragmentManager(), alFrag);
		vp.setAdapter(adapter);
		vp.setCurrentItem(position);
	}

	public void populateFragment() {
		for (int i = 0; i < alImages.size(); i++) {
			FragmentImageDetails frag = new FragmentImageDetails(alImages.get(i).getUrl());
			alFrag.add(frag);
		}
	}
}
