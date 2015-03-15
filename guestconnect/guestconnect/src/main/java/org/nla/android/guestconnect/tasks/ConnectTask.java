package org.nla.android.guestconnect.tasks;

import org.nla.android.guestconnect.R;
import org.nla.android.guestconnect.app.App;
import org.nla.android.guestconnect.authentication.AuthenticationDetails;
import org.nla.android.guestconnect.authentication.AuthenticationFailedException;
import org.nla.android.guestconnect.authentication.AuthenticationHelperFactory;
import org.nla.android.guestconnect.authentication.BadPasswordOrLoginException;
import org.nla.android.guestconnect.common.DatabaseHelper;
import org.nla.android.guestconnect.common.Event;
import org.nla.android.guestconnect.common.Event.EventSource;
import org.nla.android.guestconnect.common.Event.EventType;
import org.nla.android.guestconnect.tasks.AsyncActionResult.ResultState;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.gson.Gson;

/**
 * Async task for handling connexion.
 */
public class ConnectTask extends AsyncTask<String, Void, AsyncActionResult> {

	private final Activity mActivity;

	private final IAsyncCallback<AsyncActionResult> mCallBack;

	private ProgressDialog mProgressDialog = null;

	public ConnectTask(Activity activity, IAsyncCallback<AsyncActionResult> callBack) {
		this.mCallBack = callBack;
		this.mActivity = activity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	public AsyncActionResult doInBackground(String... params) {
		if (params == null || params.length != 2) {
			throw new IllegalArgumentException(
					"Incorrect params, you should provide email and password");
		}
		final String email = params[0];
		final String password = params[1];

		final AsyncActionResult result = new AsyncActionResult();
		DatabaseHelper dbHelper = new DatabaseHelper(this.mActivity);
		Event eventToStore = new Event();
		eventToStore.setCreationTs(System.currentTimeMillis());
		eventToStore.setEventSource(EventSource.UI);
		try {
			AuthenticationDetails authenticationDetails = AuthenticationHelperFactory
					.buildAuthenticationHelper().authenticate(email, password);
			App.getApp().setAuthenticationDetails(authenticationDetails);
			result.setStatus(ResultState.ACTION_SUCCEEDED);
			eventToStore.setEventType(EventType.CONNEXION_SUCCEEDED);
			eventToStore.setContent(new Gson().toJson(authenticationDetails));
		} catch (BadPasswordOrLoginException bpole) {
			eventToStore.setEventType(EventType.CONNEXION_FAILED_INCORRECT_PASSAXA);
			result.setStatus(ResultState.ACTION_FAILED);
			result.setException(bpole);
		} catch (AuthenticationFailedException afe) {
			eventToStore.setEventType(EventType.CONNEXION_FAILED);
			eventToStore.setContent(afe.getMessage());
			result.setStatus(ResultState.ACTION_FAILED);
			result.setException(afe);
		} finally {
			dbHelper.storeEvent(eventToStore);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(AsyncActionResult result) {
		// hides progress bar if showing
		if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
			this.mProgressDialog.dismiss();
		}
		// calls potential callback
		if (this.mCallBack != null && this.mActivity != null) {
			this.mCallBack.execute(result);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		this.mProgressDialog = new ProgressDialog(this.mActivity);
		this.mProgressDialog.setCancelable(false);
		this.mProgressDialog.setCanceledOnTouchOutside(false);
		this.mProgressDialog.setMessage(this.mActivity.getString(R.string.msg_authenticating));
		this.mProgressDialog.show();
	}
}