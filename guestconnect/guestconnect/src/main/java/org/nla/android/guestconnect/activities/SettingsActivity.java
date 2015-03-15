package org.nla.android.guestconnect.activities;

import org.nla.android.guestconnect.common.TrackingHelper;
import org.nla.android.guestconnect.common.TrackingHelper.TrackScreen;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Display the fragment as the main content.
		this.getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new MainSettingsFragment()).commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		TrackingHelper.trackScreen(TrackScreen.Settings);
	}
}
