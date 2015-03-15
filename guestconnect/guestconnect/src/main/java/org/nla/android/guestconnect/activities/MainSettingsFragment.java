package org.nla.android.guestconnect.activities;

import org.nla.android.guestconnect.R;
import org.nla.android.guestconnect.common.PreferenceConstants;
import org.nla.android.guestconnect.common.TrackingHelper;
import org.nla.android.guestconnect.common.TrackingHelper.TrackAction;
import org.nla.android.guestconnect.common.TrackingHelper.TrackCategory;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * Main preference panel.
 * 
 * @author Nico
 * 
 */
public class MainSettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.prefs_main);

		SharedPreferences sharedPreferences = this.getPreferenceManager().getSharedPreferences();

		Preference internetTestUrlPref = this.findPreference(PreferenceConstants.INTERNET_TEST_URL);
		internetTestUrlPref.setSummary("http://"
				+ sharedPreferences.getString(PreferenceConstants.INTERNET_TEST_URL,
						"www.google.com"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		this.getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		this.getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#
	 * onSharedPreferenceChanged(android.content.SharedPreferences,
	 * java.lang.String)
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

		if (key.equals(PreferenceConstants.INTERNET_TEST_URL)) {
			Preference internetTestUrlPref = this.findPreference(key);
			internetTestUrlPref.setSummary("http://"
					+ sharedPreferences.getString(key, "www.google.com"));
			TrackingHelper.trackEvent(TrackCategory.Settings, TrackAction.ChangeTestUrl);
		}

		else if (key.equals(PreferenceConstants.ALLOW_ANALYTICS)) {
			CheckBoxPreference allowAnalyticsPref = (CheckBoxPreference) this.findPreference(key);
			if (!allowAnalyticsPref.isChecked()) {
				TrackingHelper.trackEvent(TrackCategory.Settings, TrackAction.DisableAnalytics,
						true);
			}
		}
	}
}
