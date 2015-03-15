package org.nla.android.guestconnect.app;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.nla.android.guestconnect.R;
import org.nla.android.guestconnect.authentication.AuthenticationDetails;
import org.nla.android.guestconnect.common.Constants;
import org.nla.android.guestconnect.common.PreferenceConstants;

import com.flurry.android.FlurryAgent;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

@ReportsCrashes(mailTo = "wifiguestconnect@gmail.com", customReportContent = {
		ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.BRAND,
		ReportField.PHONE_MODEL, ReportField.ANDROID_VERSION, ReportField.CUSTOM_DATA,
		ReportField.STACK_TRACE, ReportField.LOGCAT, ReportField.SHARED_PREFERENCES }, mode = ReportingInteractionMode.DIALOG, resToastText = R.string.crash_toast_text, resDialogText = R.string.crash_dialog_text, resDialogIcon = android.R.drawable.ic_dialog_info, resDialogTitle = R.string.crash_dialog_title, resDialogOkToast = R.string.crash_dialog_ok_toast)
public class App extends Application {

	private static final String FLURRY_KEY = "JZVZYJJ6N6Z2G5D8B4SP";
	
	private static App app;

	public static App getApp() {
		return app;
	}

	private AuthenticationDetails mAuthenticationDetails;

	public AuthenticationDetails getAuthenticationDetails() {
		if (this.mAuthenticationDetails == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			String lastAuthenticationDetails = preferences.getString(
					PreferenceConstants.LAST_CONNEXION, null);

			if (lastAuthenticationDetails != null) {
				this.mAuthenticationDetails = Constants.GSON.fromJson(lastAuthenticationDetails,
						AuthenticationDetails.class);
			}
		}
		return this.mAuthenticationDetails;
	}

	public boolean isDebugMode() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		return preferences.getBoolean(PreferenceConstants.IS_DEBUG_MODE, false);
	}

	public boolean isTrackingAllowed() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		return preferences.getBoolean(PreferenceConstants.ALLOW_ANALYTICS, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
		app = this;
		this.setupAnalytics();
	}

	public void setAuthenticationDetails(AuthenticationDetails authenticationDetails) {
		this.mAuthenticationDetails = authenticationDetails;

		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		if (authenticationDetails != null) {
			editor.putString(PreferenceConstants.LAST_CONNEXION,
					Constants.GSON.toJson(this.mAuthenticationDetails));
		} else {
			editor.remove(PreferenceConstants.LAST_CONNEXION);
		}
		editor.commit();
	}

	public void storeResponseContentIntoPreferences(String responseContent) {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		if (responseContent != null) {
			editor.putString(PreferenceConstants.RESPONSE_CONTENT, responseContent);
		}
		editor.commit();
	}

	private void setupAnalytics() {
		FlurryAgent.init(this, FLURRY_KEY);
	}
}
