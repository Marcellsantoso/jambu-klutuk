package com.capsule.shellfies.Helpers;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.capsule.shellfies.R;
import com.iapps.libs.helpers.BaseUIHelper;

public class PopupImage extends DialogFragment {

	public static PopupImage newInstance(String url) {
		PopupImage frag = new PopupImage();
		Bundle args = new Bundle();
		args.putString(Constants.BUNDLE_IMAGE_URL, url);
		frag.setArguments(args);

		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Create layout
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View layout = inflater.inflate(R.layout.popup_image, null);
		ImageView img = (ImageView) layout.findViewById(R.id.imgPopup);

		int screenWidth = UIHelper.getScreenWidth(getActivity());
		int screenMargin = (int) UIHelper
				.convertDpToPixel(
						(int) getActivity().getResources().getDimension(
								R.dimen.activity_horizontal_margin),
						getActivity()); // Count with margin as well, because the dialog width
										// already having horizontal margin
		img.getLayoutParams().width = screenWidth - screenMargin;
		img.getLayoutParams().height = screenWidth - screenMargin;

		// Load image from url
		BaseUIHelper.loadImage(getActivity(),
				getArguments().getString(Constants.BUNDLE_IMAGE_URL),
				img);

		Builder builder = new AlertDialog.Builder(getActivity())
				.setView(layout)
				.setCancelable(true);

		return builder.create();
	}
}
