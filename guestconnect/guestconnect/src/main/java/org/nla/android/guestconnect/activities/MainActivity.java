package org.nla.android.guestconnect.activities;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.regex.Pattern;

import org.nla.android.guestconnect.R;
import org.nla.android.guestconnect.app.App;
import org.nla.android.guestconnect.authentication.AuthenticationDetails;
import org.nla.android.guestconnect.authentication.BadPasswordOrLoginException;
import org.nla.android.guestconnect.common.Constants;
import org.nla.android.guestconnect.common.DatabaseHelper;
import org.nla.android.guestconnect.common.Event;
import org.nla.android.guestconnect.common.NetworkHelper;
import org.nla.android.guestconnect.common.PreferenceConstants;
import org.nla.android.guestconnect.common.TrackingHelper;
import org.nla.android.guestconnect.common.TrackingHelper.TrackAction;
import org.nla.android.guestconnect.common.TrackingHelper.TrackCategory;
import org.nla.android.guestconnect.common.TrackingHelper.TrackScreen;
import org.nla.android.guestconnect.spice.SpiceActivity;
import org.nla.android.guestconnect.tasks.AsyncActionResult;
import org.nla.android.guestconnect.tasks.AsyncActionResult.ResultState;
import org.nla.android.guestconnect.tasks.CheckInternetConnectionTask;
import org.nla.android.guestconnect.tasks.ConnectTask;
import org.nla.android.guestconnect.tasks.DisconnectTask;
import org.nla.android.guestconnect.tasks.IAsyncCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity {

	private static final Pattern RFC2822 = Pattern
			.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");

	@InjectView(R.id.btnDisconnect)
	Button mBtnDisconnect;

	@InjectView(R.id.chkTermsOfUse)
	CheckBox mChkTermsOfUse;

	@InjectView(R.id.edtAccess)
	EditText mEdtAccess;

	@InjectView(R.id.edtEmail)
	EditText mEdtEmail;

	@InjectView(R.id.edtIp)
	EditText mEdtIPAddress;

	@InjectView(R.id.edtPassword)
	EditText mEdtPassword;

	@InjectView(R.id.edtUniversalTime)
	EditText mEdtUniversalTime;

	@InjectView(R.id.layoutConnexionDetails)
	View mLayoutConnexionDetails;

	private boolean mIsConnected;

	/**
	 * Focus change listener for email and password.
	 */
	private final OnFocusChangeListener mOnFocusOnEmailOrPasswordChangedListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				MainActivity.this.storeEmailAndPassword();
			}
		}
	};

	/**
	 * Secured preferences to store credentials.
	 */
	private SharedPreferences mSecuredPreferences;

	/**
	 * Callback for authenticating to guest wifi.
	 */
	private final IAsyncCallback<AsyncActionResult> onAttemptToConnectCallback = new IAsyncCallback<AsyncActionResult>() {

		@Override
		public void execute(AsyncActionResult result) {
			MainActivity.this.onAttemptToConnect(result);
		}
	};

	/**
	 * Callback for disconnecting from guest wifi.
	 */
	private final IAsyncCallback<AsyncActionResult> onAttemptToDisconnectCallback = new IAsyncCallback<AsyncActionResult>() {

		@Override
		public void execute(AsyncActionResult result) {
			MainActivity.this.onAttemptToDisconnect(result);
		}
	};

	/**
	 * Callback for checking internet connection.
	 */
	private final IAsyncCallback<Boolean> onInternetConnexionCheckedCallback = new IAsyncCallback<Boolean>() {

		@Override
		public void execute(Boolean result) {
			MainActivity.this.onInternetConnexionChecked(result);
		}
	};

	@OnClick(R.id.btnConnect)
	public void onClickOnConnect(View view) {
		TrackingHelper.trackEvent(TrackCategory.Main,
				TrackAction.ClickOnConnect);
		this.hideKeyboard();
		if (this.mEdtEmail.getText().toString().trim().length() == 0
				|| this.mEdtPassword.getText().toString().trim().length() == 0) {
			Toast.makeText(this, R.string.msg_fill_required_fields,
					Toast.LENGTH_LONG).show();
			return;
		}

		if (!this.isEmailValid()) {
			Toast.makeText(this, R.string.msg_type_valid_email,
					Toast.LENGTH_LONG).show();
			return;
		}

		if (!NetworkHelper.isConnectedToGuestWifi(this)) {
			Toast.makeText(this, R.string.msg_not_connected_to_guest,
					Toast.LENGTH_LONG).show();
			return;
		}

		this.storeEmailAndPassword();
		new ConnectTask(this, this.onAttemptToConnectCallback).execute(
				this.mEdtEmail.getText().toString().trim(), this.mEdtPassword
						.getText().toString().trim());
	}

	@OnClick(R.id.btnDisconnect)
	public void onClickOnDisconnect(View view) {
		TrackingHelper.trackEvent(TrackCategory.Main,
				TrackAction.ClickOnDisconnect);
		if (this.mIsConnected) {
			if (NetworkHelper.isConnectedToGuestWifi(this)) {
				new DisconnectTask(this, this.onAttemptToDisconnectCallback)
						.execute(this.mEdtEmail.getText().toString().trim(),
								App.getApp().getAuthenticationDetails()
										.getPasswordDigest());
			} else {
				Toast.makeText(this, R.string.msg_not_connected_to_guest,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@OnClick(R.id.chkTermsOfUse)
	public void onClickOnTerms(View view) {
		this.mChkTermsOfUse.setChecked(true);
		TrackingHelper.trackEvent(TrackCategory.Main, TrackAction.ClickOnTerms);

		Intent intent = new Intent(this, TermsActivity.class);
		this.startActivity(intent);
		// for debug purposes
		// NotificationManager notificationManager = (NotificationManager) this
		// .getSystemService(Context.NOTIFICATION_SERVICE);
		//
		// NotificationCompat.Builder notificationCompatBuilder = new
		// NotificationCompat.Builder(this);
		//
		// String notificationTitle =
		// this.getString(R.string.notif_title_connected);
		// String notificationContent =
		// this.getString(R.string.notif_content_connected);
		//
		// notificationCompatBuilder.setContentText(notificationContent).setTicker(notificationTitle)
		// .setContentTitle(notificationTitle).setDefaults(Notification.DEFAULT_SOUND)
		// .setSmallIcon(R.drawable.ic_notification).setAutoCancel(true);
		//
		// NotificationCompat.InboxStyle inboxStyle = new
		// NotificationCompat.InboxStyle();
		// inboxStyle.setBigContentTitle("Connection successful!");
		// inboxStyle.addLine(this.getString(R.string.notif_detail_login,
		// "bill.balentine@xax.com"));
		// inboxStyle.addLine(this.getString(R.string.notif_detail_profile,
		// "Full_Access"));
		// inboxStyle.addLine(this.getString(R.string.notif_detail_ip_address,
		// "10.217.14.156"));
		// notificationCompatBuilder.setStyle(inboxStyle);
		//
		// notificationManager.notify(22442423,
		// notificationCompatBuilder.build());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Callback for handling internet connexion.
	 * 
	 * @param isConnected
	 */
	public void onInternetConnexionChecked(Boolean isConnected) {
		int idOfMessageToDisplay = R.string.msg_not_connected_to_internet;
		if (isConnected && NetworkHelper.isConnectedToGuestWifi(this)) {
			idOfMessageToDisplay = R.string.msg_connected_to_internet_through_guest;
		} else if (isConnected && !NetworkHelper.isConnectedToGuestWifi(this)) {
			idOfMessageToDisplay = R.string.msg_connected_to_internet_not_through_guest;
		}

		Toast.makeText(this, idOfMessageToDisplay, Toast.LENGTH_LONG).show();
		if (this.mIsConnected != isConnected) {
			this.mIsConnected = isConnected;
			this.updateConnectionState();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// check internet connexion
		case R.id.action_check_connect_item: {
			TrackingHelper.trackEvent(TrackCategory.Main,
					TrackAction.ClickOnCheckConnexion);

			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			String internetTestUrl = sharedPreferences.getString(
					PreferenceConstants.INTERNET_TEST_URL, "www.google.com");
			try {
				// checking access to url specified by user in preferences
				new CheckInternetConnectionTask(internetTestUrl, this,
						this.onInternetConnexionCheckedCallback).execute();
			} catch (MalformedURLException e) {

				// if user-specified url is incorrect, fallback to default
				// url
				new CheckInternetConnectionTask(this,
						this.onInternetConnexionCheckedCallback).execute();
			}
			return true;
		}

		// show about panel
		case R.id.action_about_item:
			TrackingHelper.trackEvent(TrackCategory.Main,
					TrackAction.ClickOnShowAboutMessage);
			this.showAboutMessage();
			return true;

			// show preference panel
		case R.id.action_settings:
			TrackingHelper.trackEvent(TrackCategory.Main,
					TrackAction.ClickOnShowSettings);
			this.showSettings();
			return true;

			// show history activity
		case R.id.action_history:
			TrackingHelper.trackEvent(TrackCategory.Main,
					TrackAction.ClickOnShowHistory);
			// this.startActivity(new Intent(this, HistoryActivity.class));
			this.startActivity(new Intent(this, SpiceActivity.class));
			return true;

			// what else...
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Hides the virtual keyboard.
	 */
	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.mEdtPassword.getWindowToken(), 0);
	}

	/**
	 * Checks email validity. Source: http://goo.gl/aYBzlS
	 * 
	 * @return
	 */
	private boolean isEmailValid() {
		return this.mEdtEmail.getText().toString().trim().length() != 0
				&& RFC2822.matcher(this.mEdtEmail.getText().toString().trim())
						.matches();
	}

	/**
	 * Connexion callback.
	 * 
	 * @param result
	 */
	private void onAttemptToConnect(AsyncActionResult result) {
		if (result.getStatus() == ResultState.ACTION_SUCCEEDED) {
			this.mIsConnected = true;
			Toast.makeText(this, R.string.msg_authentication_successful,
					Toast.LENGTH_LONG).show();
		} else {
			this.mIsConnected = false;
			if (result.getException() instanceof BadPasswordOrLoginException) {
				this.showAuthenticationError(this
						.getString(R.string.msg_incorrect_passaxa));
			} else {
				this.showAuthenticationError(result.getException().getMessage());
			}
		}

		this.updateConnectionState();
	}

	/**
	 * Disconnexion callback.
	 * 
	 * @param result
	 */
	private void onAttemptToDisconnect(AsyncActionResult result) {
		this.mIsConnected = false;
		App.getApp().setAuthenticationDetails(null);
		Toast.makeText(this, R.string.msg_disconnected, Toast.LENGTH_LONG)
				.show();
		this.updateConnectionState();
	}

	/**
	 * Show about page.
	 */
	private void showAboutMessage() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(R.string.title_about);
		alertDialogBuilder.setCancelable(false).setPositiveButton(
				R.string.btn_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		alertDialogBuilder.setMessage(this.getString(R.string.msg_about));
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	/**
	 * Shows the detailed authentication error message.
	 * 
	 * @param e
	 */
	private void showAuthenticationError(String errorMessage) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(R.string.title_authentication_result);
		alertDialogBuilder.setCancelable(false).setPositiveButton(
				R.string.btn_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		alertDialogBuilder.setMessage(this
				.getString(R.string.msg_authentication_failed) + errorMessage);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	/**
	 * Show the settings panel.
	 */
	private void showSettings() {
		Intent settingsIntent = new Intent(this, SettingsActivity.class);
		this.startActivity(settingsIntent);
	}

	/**
	 * Store email and password into preferences.
	 */
	private void storeEmailAndPassword() {
		Editor editor = this.mSecuredPreferences.edit();
		editor.putString(PreferenceConstants.EMAIL, this.mEdtEmail.getText()
				.toString().trim());
		editor.putString(PreferenceConstants.PASSWORD, this.mEdtPassword
				.getText().toString().trim());
		editor.commit();
	}

	/**
	 * Updates the views after connexion or disconnexion.
	 */
	private void updateConnectionState() {
		// update state only if app is really connected
		AuthenticationDetails authenticationDetails = null;
		if (this.mIsConnected) {
			authenticationDetails = App.getApp().getAuthenticationDetails();

			// in some cases, details can no longer be present in context
			// in that case, get last event from db
			if (authenticationDetails == null) {
				DatabaseHelper dbHelper = new DatabaseHelper(this);
				Event lastConnectionEvent = dbHelper
						.getLastConnexionSuccessfulEvent();

				// ensure at least a connection event exists, otherwise treat as
				// if app is not connected
				if (lastConnectionEvent != null) {
					authenticationDetails = Constants.GSON.fromJson(
							lastConnectionEvent.getContent(),
							AuthenticationDetails.class);
				} else {
					this.mIsConnected = false;
				}
			}
		}

		if (this.mIsConnected) {
			this.mLayoutConnexionDetails.setVisibility(View.VISIBLE);
			this.mBtnDisconnect.setEnabled(true);
			this.mEdtAccess.setText(authenticationDetails.getServices());
			this.mEdtIPAddress.setText(authenticationDetails.getIpAddress());
			Date connexionDate = new Date(
					authenticationDetails.getConnexionTime());
			this.mEdtUniversalTime.setText(Constants.PST_FORMAT
					.format(connexionDate));
		} else {
			this.mBtnDisconnect.setEnabled(false);
			this.mLayoutConnexionDetails.setVisibility(View.INVISIBLE);
			this.mEdtAccess.setText(null);
			this.mEdtIPAddress.setText(null);
			this.mEdtUniversalTime.setText(null);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		ButterKnife.inject(this);

		this.mIsConnected = false;
		this.mSecuredPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String email = this.mSecuredPreferences.getString(
				PreferenceConstants.EMAIL, null);
		String password = this.mSecuredPreferences.getString(
				PreferenceConstants.PASSWORD, null);

		this.mEdtEmail.setText(email);
		this.mEdtPassword.setText(password);

		this.mEdtEmail
				.setOnFocusChangeListener(this.mOnFocusOnEmailOrPasswordChangedListener);
		this.mEdtPassword
				.setOnFocusChangeListener(this.mOnFocusOnEmailOrPasswordChangedListener);

		View imgEyesOpen = this.findViewById(R.id.imgEyesOpen);
		imgEyesOpen.setOnTouchListener(new OnTouchListener() {

			// http://stackoverflow.com/questions/9307680/show-the-password-with-edittext
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					MainActivity.this.mEdtPassword
							.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
				} else {
					MainActivity.this.mEdtPassword.setInputType(129);
				}
				return true;
			}
		});

		this.onNewIntent(this.getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Bundle extras = intent.getExtras();
		if (extras != null) {
			if (extras.containsKey("TODO")) {
				this.showAuthenticationError(extras.getString("TODO"));
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (App.getApp().getAuthenticationDetails() != null) {
			this.mIsConnected = true;
			this.updateConnectionState();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		TrackingHelper.trackScreen(TrackScreen.Main);
	}
}