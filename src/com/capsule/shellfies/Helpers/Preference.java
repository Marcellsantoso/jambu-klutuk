package com.capsule.shellfies.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

	private final String		PREF_NAME					= "sscpref";
	private final String		IS_FIRST_LOAD				= "isFirstLoad";
	private final String		USER_ID						= "userId";
	private final String		FACEBOOK_ID					= "facebookId";
	private final String		FACEBOOK_SESSION			= "facebookSession";
	private final String		FACEBOOK_ALBUM_SHELLFIES	= "facebookShellfiesAlbum";

	private Context				context;
	private static Preference	pref;

	public static Preference getInstance(Context context) {
		if (pref == null) {
			pref = new Preference(context);
		}

		return pref;
	}

	private Preference(Context context) {
		this.context = context;
	}

	private SharedPreferences getPreference() {
		return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
	}

	private SharedPreferences.Editor editPreference() {
		return getPreference().edit();
	}

	public void setFirstAppLoad(boolean isFirstAppLoad) {
		editPreference().putBoolean(IS_FIRST_LOAD, isFirstAppLoad).commit();
	}

	public boolean isFirstAppLoad() {
		return getPreference().getBoolean(IS_FIRST_LOAD, true);
	}

	public int getUserId() {
		return getPreference().getInt(USER_ID, 0);
	}

	public void setUserId(int userId) {
		editPreference().putInt(USER_ID, userId).commit();
	}

	public void clear() {
		editPreference().clear().commit();
	}

	public String getFacebookId() {
		return getPreference().getString(FACEBOOK_ID, "");
	}

	public void setFacebookId(String facebookId) {
		editPreference().putString(FACEBOOK_ID, facebookId).commit();
	}

	public void setFacebookSession(String accessToken) {
		editPreference().putString(FACEBOOK_SESSION, accessToken).commit();
	}

	public String getFacebookSession() {
		return getPreference().getString(FACEBOOK_SESSION, "");
	}

	public void setFacebookAlbumId(String albumId) {
		editPreference().putString(FACEBOOK_ALBUM_SHELLFIES, albumId).commit();
	}

	public String getFacebookAlbumId() {
		return getPreference().getString(FACEBOOK_ALBUM_SHELLFIES, "");
	}
}
