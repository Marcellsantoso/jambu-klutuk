package com.capsule.shellfies.Helpers;

import android.content.Context;

public class Api {
	@SuppressWarnings("unused")
	private Context	context;

	private Api(Context context) {
		this.context = context;
	}

	public static Api getInstance(Context context) {
		Api api = new Api(context);
		return api;
	}

	public String getGridAll() {
		return GRID_LOAD_ALL;
	}

	private final String		GRID_LOAD_ALL	= "http://api.dribbble.com/shots/popular";

	// ================================================================================
	// Public APIs
	// ================================================================================
	public static final String	FACEBOOK_BASE	= "https://graph.facebook.com/";
	public static final String	FACEBOOK_ALBUM	= "me/albums";
	public static final String	FACEBOOK_PHOTO	= "me/photos";
}
