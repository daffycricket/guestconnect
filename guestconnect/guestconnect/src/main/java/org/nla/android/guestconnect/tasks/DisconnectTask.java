package org.nla.android.guestconnect.tasks;

import org.nla.android.guestconnect.R;
import org.nla.android.guestconnect.authentication.AuthenticationHelperFactory;
import org.nla.android.guestconnect.tasks.AsyncActionResult.ResultState;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Async task for handling disconnexion.
 */
public class DisconnectTask extends AsyncTask<String, Void, AsyncActionResult> {

	private final Activity mActivity;

	private final IAsyncCallback<AsyncActionResult> mCallBack;

	private ProgressDialog mProgressDialog = null;

	public DisconnectTask(Activity activity, IAsyncCallback<AsyncActionResult> callBack) {
		this.mCallBack = callBack;
		this.mActivity = activity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected AsyncActionResult doInBackground(String... params) {
		if (params == null || params.length != 2) {
			throw new IllegalArgumentException(
					"Incorrect params, you should provide email and passwordDigest");
		}
		final String email = params[0];
		final String passwordDigest = params[1];

		final AsyncActionResult result = new AsyncActionResult();
		try {
			boolean isDisconnected = AuthenticationHelperFactory.buildAuthenticationHelper()
					.disconnect(email, passwordDigest);
			result.setStatus(isDisconnected ? ResultState.ACTION_SUCCEEDED
					: ResultState.ACTION_FAILED);
		} catch (Exception e) {
			result.setStatus(ResultState.ACTION_SUCCEEDED);
			result.setException(e);
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
		this.mProgressDialog.setMessage(this.mActivity.getString(R.string.msg_disconnecting));
		this.mProgressDialog.show();
	}
}