package com.capsule.shellfies.Fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;

import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.capsule.shellfies.R;
import com.capsule.shellfies.Helpers.Api;
import com.capsule.shellfies.Helpers.Constants;
import com.capsule.shellfies.Helpers.Helper;
import com.capsule.shellfies.Helpers.Keys;
import com.capsule.shellfies.Helpers.UIHelper;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.gc.materialdesign.widgets.SnackBar;

public class FragmentUpload extends BaseFragmentShellfies {
	@InjectView(R.id.imgUpload)
	private ImageView	imgUpload;

	private final int	API_PHOTO_UPLOAD	= 1, API_ALBUM_CREATE = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_upload, container, false);

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setHasOptionsMenu(true);
		getActionbar().setTitle(R.string.title_upload);
		getActionbar().setDisplayHomeAsUpEnabled(true);
		getHome().showTopBar();
		getHome().hideFooter();

		loadImage();
	}

	// ================================================================================
	// Menu functions
	// ================================================================================
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_done, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_done:
				uploadImage();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	// ================================================================================
	// Load image functions
	// ================================================================================
	public void loadImage() {
		imgUpload.getLayoutParams().height = imgUpload.getLayoutParams().width;
		UIHelper.processImage(getActivity(), imgUpload);
	}

	// ================================================================================
	// Upload functions
	// ================================================================================
	public void uploadImage() {
		if (Helper.isEmpty(getPref().getFacebookAlbumId())) {
			callApi(API_ALBUM_CREATE);
		} else {
			try {
				// Get image path
				String url = Environment.getExternalStorageDirectory() + "/"
						+ Constants.TEMP_PHOTO_FILE;
				File avatar = new File(url);
				Uri uri = Uri.fromFile(avatar);
				InputStream iStream = getActivity().getContentResolver().openInputStream(uri);

				// Get parameters
				Bundle params = new Bundle();
				params.putByteArray("photo", getBytes(iStream));

				Session session = Session.openActiveSessionFromCache(getActivity());
				if (session != null) {
					final ProgressDialog pDialog = ProgressDialog.show(getActivity(), "",
							getString(R.string.msg_upload_photo));

					new Request(
							session,
							getPref().getFacebookAlbumId() + "/photos",
							params,
							HttpMethod.POST,
							new Request.Callback() {
								@Override
								public void onCompleted(Response response) {
									pDialog.dismiss();
									displaySnackbar();
									getHome().setActionChange(true);
									getBaseActivity().onBackPressed();
								}
							}).executeAsync();
				}
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void displaySnackbar() {
		SnackBar snackBar = new SnackBar(getActivity(), getString(R.string.msg_upload_success),
				getString(android.R.string.ok), null);
		snackBar.setColorButton(R.color.shellfies_base_color);
		snackBar.show();
	}

	public byte[] getBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}
		return byteBuffer.toByteArray();
	}

	public void createAlbum() {
		Bundle params = new Bundle();
		params.putString("name", getString(R.string.app_name));
		params.putString("message", "Album description");
		params.putString("privacy", "{value:\"EVERYONE\"}");

		Session session = Session.openActiveSessionFromCache(getActivity());
		if (session != null) {
			final ProgressDialog pDialog = ProgressDialog.show(getActivity(), "",
					getString(R.string.msg_create_album));

			new Request(
					Session.getActiveSession(),
					Api.FACEBOOK_ALBUM,
					params,
					HttpMethod.POST,
					new Request.Callback() {
						@Override
						public void onCompleted(com.facebook.Response response) {
							pDialog.dismiss();
							try {
								String albumId = response.getGraphObject().getInnerJSONObject()
										.getString(Keys.ID);
								if (!Helper.isEmpty(albumId)) {
									getPref().setFacebookAlbumId(albumId);

									callApi(API_PHOTO_UPLOAD);
								}
							}
							catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}).executeAsync();
		}
	}

	// ================================================================================
	// API functions
	// ================================================================================
	public void callApi(int tag) {
		switch (tag) {
			case API_ALBUM_CREATE:
				createAlbum();
				break;

			case API_PHOTO_UPLOAD:
				uploadImage();
				break;
		}
	}
}
