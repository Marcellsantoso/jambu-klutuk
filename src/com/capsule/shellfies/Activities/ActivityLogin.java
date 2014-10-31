package com.capsule.shellfies.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.capsule.shellfies.R;
import com.capsule.shellfies.Fragments.FragmentLogin;

public class ActivityLogin extends BaseActivityShellfies {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// User already logged in
		if (getPref().getUserId() > 0)
			changeActivity();

		setContentView(R.layout.activity_login);
		setContainerId(R.id.container);
		setFragment(new FragmentLogin());
	}

	public void changeActivity() {
		startActivity(new Intent(ActivityLogin.this, ActivityHome.class));
		this.finish();
	}

}
