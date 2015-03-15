package org.nla.android.guestconnect.tasks;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.nla.android.guestconnect.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Async task for checking internet connexion.
 */
public class CheckInternetConnectionTask extends AsyncTask<Void, Void, Boolean> {

	private final Activity mActivity;

	private final IAsyncCallback<Boolean> mCallBack;

	private boolean mDefaultUrl = false;

	private ProgressDialog mProgressDialog = null;

	private final URL mUrl;

	public CheckInternetConnectionTask(Activity activity, IAsyncCallback<Boolean> callBack) {
		try {
			this.mUrl = new URL("http://www.google.com");
		} catch (MalformedURLException e) {
			// should never happen
			throw new IllegalStateException(
					"MalformedURLException was thrown on http://www.google.com");
		}
		this.mDefaultUrl = true;
		this.mCallBack = callBack;
		this.mActivity = activity;
	}

	public CheckInternetConnectionTask(String internetTestUrl, Activity activity,
			IAsyncCallback<Boolean> callBack) throws MalformedURLException {
		this.mUrl = new URL("http://" + internetTestUrl);
		this.mCallBack = callBack;
		this.mActivity = activity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected Boolean doInBackground(Void... arg0) {
		boolean isConnected = false;
		try {
			HttpURLConnection cnx = (HttpURLConnection) this.mUrl.openConnection();
			cnx.setConnectTimeout(5000);
			cnx.setReadTimeout(3000);
			cnx.setDefaultUseCaches(false);
			cnx.connect();
			isConnected = cnx.getResponseCode() == 200;
		} catch (Exception e) {
			isConnected = false;
		}
		return isConnected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Boolean isConnected) {
		// hides progress bar if showing
		if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
			this.mProgressDialog.dismiss();
		}

		// calls potential callback
		if (this.mCallBack != null && this.mActivity != null) {
			this.mCallBack.execute(isConnected);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		// TODO Localize properly
		String message = this.mDefaultUrl ? this.mActivity
				.getString(R.string.msg_check_internet_err_incorrect_url) : this.mActivity
				.getString(R.string.msg_check_internet) + " " + this.mUrl + " ...";

		this.mProgressDialog = new ProgressDialog(this.mActivity);
		this.mProgressDialog.setCancelable(false);
		this.mProgressDialog.setCanceledOnTouchOutside(false);
		this.mProgressDialog.setMessage(message);
		this.mProgressDialog.show();
	}
}
