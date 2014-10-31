package com.capsule.shellfies.Fragments;

import java.util.List;
import java.util.Map;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.capsule.shellfies.R;
import com.capsule.shellfies.Activities.ActivityHome;
import com.capsule.shellfies.Helpers.Constants;
import com.capsule.shellfies.Helpers.Helper;
import com.capsule.shellfies.Helpers.Keys;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.iapps.libs.views.ButtonIcon;

public class FragmentLogin extends BaseFragmentShellfies {
	@InjectView(R.id.btnLogin)
	private ButtonIcon				btnLogin;
	@InjectView(R.id.pb)
	private ProgressBar				pb;

	private Session.StatusCallback	statusCallback			= new SessionStatusCallback();
	private boolean					isFBAsyncRunning		= false;

	private final int				TAG_LOGIN				= 1;

	private static final String[]	FACEBOOK_PERMISSIONS	= {
			"user_photos", "user_birthday", "email"
															};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_login, container, false);

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();

		// Indicate that this app is not loaded for the first time
		getPref().setFirstAppLoad(false);
	}

	public void init() {
		btnLogin.setTag(TAG_LOGIN);
		btnLogin.setOnClickListener(ListenerClick);
	}

	public void changeActivity() {
		startActivity(new Intent(getActivity(), ActivityHome.class));
		getActivity().finish();
	}

	// ================================================================================
	// Facebook Functions
	// ================================================================================
	private void fbLogin() {
		Session session = new Session.Builder(getActivity()).setApplicationId(
				getResources().getString(R.string.fb_app_id)).build();
		Session.setActiveSession(session);
		newLoginWithFB();
	}

	private void newLoginWithFB() {
		Session session = Session.getActiveSession();

		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this)
					.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK)
					.setCallback(statusCallback)
					.setPermissions(FACEBOOK_PERMISSIONS));
		}
		else {
			Session.openActiveSession(getActivity(), true, statusCallback);
		}
	}

	private void onSessionStateChange(SessionState state, Exception exception) {
		Session session = Session.getActiveSession();
		// Check if the session is open
		if (state.isOpened()) {
			if (isFBAsyncRunning == false) {
				getPref().setFacebookSession(session.getAccessToken());
				Request req = new Request(session, "me");
				FacebookRequestAsyncTask fb = new FacebookRequestAsyncTask(req);
				fb.execute();
			}
			else {
				// it is already running
			}

		}
		else if (session.getState() == SessionState.OPENING) {
			session.addCallback(statusCallback);

		}
		else if (state == SessionState.CLOSED_LOGIN_FAILED) {
			showFBError();
		}
	}

	private void showFBError() {
		Helper.showAlert(getActivity(), R.string.err_title, R.string.err_facebook);
		Session.getActiveSession().closeAndClearTokenInformation();
		Session session = new Session.Builder(getActivity()).setApplicationId(
				getResources().getString(R.string.fb_app_id)).build();
		Session.setActiveSession(session);
	}

	/**
	 * Callback from facebook authorization
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK
				&& requestCode == Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE && data != null) {
			Session.getActiveSession().addCallback(statusCallback);
			Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode,
					data);
		}
		else if (Session.getActiveSession().getState() == SessionState.OPENING && data != null) {
			Session.getActiveSession().addCallback(statusCallback);
			Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode,
					data);
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {

		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(state, exception);
		}
	}

	private class FacebookRequestAsyncTask
			extends RequestAsyncTask {

		public FacebookRequestAsyncTask(Request request) {
			super(request);
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			Helper.visibleView(pb);
		}

		@Override
		public void onPostExecute(List<Response> result) {
			Helper.goneView(pb);
			if (result != null && result.size() > 0) {
				try {
					Response m = result.get(0);
					Map<String, Object> map = m.getGraphObject().asMap();

					String fbId = map.get(Keys.ID).toString();
					getPref().setFacebookId(fbId);
					// call login webservice

					Log.d(Constants.LOG, "success login");

					// Save user_id
					getPref().setUserId(11);

					changeActivity();
				}
				catch (NullPointerException e) {
					showFBError();
					e.printStackTrace();
				}
				catch (IndexOutOfBoundsException e) {
					showFBError();
					e.printStackTrace();
				}

			}
			else {
				Helper.showInternetError(getActivity());
			}
		}
	}

	// ================================================================================
	// Listeners
	// ================================================================================
	View.OnClickListener	ListenerClick	= new OnClickListener() {

												@Override
												public void onClick(View v) {
													switch ((Integer) v.getTag()) {
														case TAG_LOGIN:
															fbLogin();
															break;
													}
												}
											};
}
