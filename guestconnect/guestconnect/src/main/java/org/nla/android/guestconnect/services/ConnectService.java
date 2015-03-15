package org.nla.android.guestconnect.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpStatus;
import org.nla.android.guestconnect.R;
import org.nla.android.guestconnect.activities.MainActivity;
import org.nla.android.guestconnect.app.App;
import org.nla.android.guestconnect.authentication.AuthenticationDetails;
import org.nla.android.guestconnect.authentication.AuthenticationFailedException;
import org.nla.android.guestconnect.authentication.AuthenticationHelperFactory;
import org.nla.android.guestconnect.authentication.BadPasswordOrLoginException;
import org.nla.android.guestconnect.common.Constants;
import org.nla.android.guestconnect.common.DatabaseHelper;
import org.nla.android.guestconnect.common.Event;
import org.nla.android.guestconnect.common.Event.EventSource;
import org.nla.android.guestconnect.common.Event.EventType;
import org.nla.android.guestconnect.common.NetworkHelper;
import org.nla.android.guestconnect.common.PreferenceConstants;
import org.nla.android.guestconnect.common.TrackingHelper;
import org.nla.android.guestconnect.common.TrackingHelper.TrackAction;
import org.nla.android.guestconnect.common.TrackingHelper.TrackCategory;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

public class ConnectService extends IntentService {

	public static boolean SEQUENCE_RUNNING = false;

	private static final int NOTIFICATION_ID = 1498853;

	private NotificationManager mNotificationManager;

	public ConnectService() {
		super("org.nla.android.guestconnect.services.ConnectService");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		this.mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/**
	 * Builds URL to use for internet connection check.
	 * 
	 * @return
	 */
	private URL getTestURL() {
		String internetTestUrl = PreferenceManager.getDefaultSharedPreferences(this).getString(
				PreferenceConstants.INTERNET_TEST_URL, "www.google.com");

		URL toReturn = null;
		try {
			toReturn = new URL("http://" + internetTestUrl);
		} catch (MalformedURLException mue) {
			try {
				toReturn = new URL("http://www.google.com");
			} catch (MalformedURLException mue2) {
				// should never happen...
				throw new IllegalStateException(
						"MalformedURLException was thrown on http://www.google.com");
			}
		}
		return toReturn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

		// check whether user wants autoconnect
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				PreferenceConstants.AUTO_START_APP_IF_CONNECTED_TO_GUEST, true)) {
			return;
		}

		if (NetworkHelper.isConnectedToGuestWifi(this) && !SEQUENCE_RUNNING) {

			Intent resultIntent = new Intent(this, MainActivity.class);
			PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(
					this.getApplicationContext());

			SEQUENCE_RUNNING = true;

			// check if device is connected to the internet if not in debug mode
			// if in debug mode, act as if not connected
			boolean isConnectedToInternet = false;
			if (App.getApp().isDebugMode()) {
				// simulates network call
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				isConnectedToInternet = false;
			} else {
				try {
					HttpURLConnection cnx = (HttpURLConnection) this.getTestURL().openConnection();
					cnx.setConnectTimeout(5000);
					cnx.setReadTimeout(3000);
					cnx.setDefaultUseCaches(false);
					cnx.connect();
					isConnectedToInternet = cnx.getResponseCode() == HttpStatus.SC_OK;
				} catch (IOException e) { // unknown host
					isConnectedToInternet = false;
				}
			}

			// not connected, attempt to authenticate...
			if (!isConnectedToInternet) {

				// no passaxa is stored, requires user to open app
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
				if (!(preferences.contains(PreferenceConstants.EMAIL) && preferences
						.contains(PreferenceConstants.PASSWORD))) {

					String notificationTitle = this
							.getString(R.string.notif_title_credentials_required);
					String notificationContent = this
							.getString(R.string.notif_content_credentials_required);

					SEQUENCE_RUNNING = false;
					notificationCompatBuilder.setContentText(notificationContent)
							.setTicker(notificationTitle).setContentTitle(notificationTitle)
							.setDefaults(Notification.DEFAULT_SOUND)
							.setSmallIcon(R.drawable.ic_notification)
							.setContentIntent(resultPendingIntent).setAutoCancel(true);

					this.mNotificationManager.notify(NOTIFICATION_ID,
							notificationCompatBuilder.build());
				}

				// authenticate
				else {
					DatabaseHelper dbHelper = new DatabaseHelper(this);
					Event eventToStore = new Event();
					eventToStore.setCreationTs(System.currentTimeMillis());
					eventToStore.setEventSource(EventSource.SVC);
					try {
						String email = preferences.getString(PreferenceConstants.EMAIL, null);
						String password = preferences.getString(PreferenceConstants.PASSWORD, null);

						AuthenticationDetails authenticationDetails = AuthenticationHelperFactory
								.buildAuthenticationHelper().authenticate(email, password);
						App.getApp().setAuthenticationDetails(authenticationDetails);

						eventToStore.setEventType(EventType.CONNEXION_SUCCEEDED);
						eventToStore.setContent(Constants.GSON.toJson(authenticationDetails));

						String notificationTitle = this.getString(R.string.notif_title_connected);
						String notificationContent = this
								.getString(R.string.notif_content_connected);

						notificationCompatBuilder.setContentText(notificationContent)
								.setTicker(notificationTitle).setContentTitle(notificationTitle)
								.setDefaults(Notification.DEFAULT_SOUND)
								.setSmallIcon(R.drawable.ic_notification)
								.setContentIntent(resultPendingIntent).setAutoCancel(true);

						NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
						inboxStyle.setBigContentTitle(notificationContent);
						inboxStyle.addLine(this.getString(R.string.notif_detail_login,
								authenticationDetails.getEmail()));
						inboxStyle.addLine(this.getString(R.string.notif_detail_profile,
								authenticationDetails.getServices()));
						inboxStyle.addLine(this.getString(R.string.notif_detail_ip_address,
								authenticationDetails.getIpAddress()));
						notificationCompatBuilder.setStyle(inboxStyle);
					} catch (BadPasswordOrLoginException bpole) {
						eventToStore.setEventType(EventType.CONNEXION_FAILED_INCORRECT_PASSAXA);

						String notificationTitle = this
								.getString(R.string.notif_title_connexion_error);
						String notificationContent = this
								.getString(R.string.notif_content_incorrect_passaxa);

						notificationCompatBuilder.setContentText(notificationContent)
								.setTicker(notificationTitle)
								.setDefaults(Notification.DEFAULT_SOUND)
								.setContentTitle(notificationTitle)
								.setSmallIcon(R.drawable.ic_notification)
								.setContentIntent(resultPendingIntent).setAutoCancel(true);
					} catch (AuthenticationFailedException afe) {
						eventToStore.setEventType(EventType.CONNEXION_FAILED);
						eventToStore.setContent(afe.getMessage());

						String notificationTitle = this
								.getString(R.string.notif_title_connexion_error);

						notificationCompatBuilder.setContentText(afe.getMessage())
								.setTicker(notificationTitle)
								.setDefaults(Notification.DEFAULT_SOUND)
								.setContentTitle(notificationTitle)
								.setSmallIcon(R.drawable.ic_notification)
								.setContentIntent(resultPendingIntent).setAutoCancel(true);
					} finally {
						dbHelper.storeEvent(eventToStore);
						SEQUENCE_RUNNING = false;
						this.mNotificationManager.notify(NOTIFICATION_ID,
								notificationCompatBuilder.build());
						TrackingHelper.trackEvent(TrackCategory.ConnectService,TrackAction.AttemptToConnect);
					}
				}
			}
		}
	}
}