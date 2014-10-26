package com.capsule.shellfies.Helpers;

import android.util.Log;

import com.capsule.shellfies.Fragments.BaseFragmentShellfies;
import com.iapps.libs.helpers.BaseHelper;
import com.iapps.libs.objects.Response;

public class Helper extends BaseHelper {

	public static boolean isValidResponse(Response response, final BaseFragmentShellfies frag) {
		if (response != null) {
			// Don't do anything if activity is already destroyed
			if (frag.isDetached() || frag.getActivity() == null ||
					frag.getActivity().isFinishing() || frag.getActivity().isDestroyed())
				return false;

			if (Constants.IS_DEBUGGING)
				Log.d(Constants.LOG, response.getContent().toString());
		}
		else
			return false;

		return true;
	}
}
