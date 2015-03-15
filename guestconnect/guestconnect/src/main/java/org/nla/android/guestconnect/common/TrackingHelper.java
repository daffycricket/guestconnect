package org.nla.android.guestconnect.common;

import java.util.HashMap;
import java.util.Map;

import org.nla.android.guestconnect.app.App;

import com.flurry.android.FlurryAgent;

public class TrackingHelper {

	public enum TrackAction {
		AttemptToConnect, ChangeTestUrl, ClickOnCheckConnexion, ClickOnConnect, ClickOnDisconnect, ClickOnHistoryDetail, ClickOnShowAboutMessage, ClickOnShowHistory, ClickOnShowSettings, ClickOnTerms, DisableAnalytics,
	}

	public enum TrackCategory {
		ConnectService, History, Main, Settings, Terms
	}

	public enum TrackScreen {
		History, Main, Settings, Terms
	}

	public static void trackEvent(TrackCategory trackCategory, TrackAction trackAction) {
		trackEvent(trackCategory, trackAction, false);
	}

	public static void trackEvent(TrackCategory trackCategory, TrackAction trackAction,
			boolean isTrackingForced) {
		if (isTrackingForced || App.getApp().isTrackingAllowed()) {
			Map<String, String> eventParams = new HashMap<String, String>();
			eventParams.put("Action", trackAction.toString());
			FlurryAgent.logEvent(trackCategory.toString(), eventParams);
		}
	}

	public static void trackScreen(TrackScreen trackScreen) {
		if (App.getApp().isTrackingAllowed()) {
			FlurryAgent.logEvent(trackScreen.toString());
		}
	}
}
