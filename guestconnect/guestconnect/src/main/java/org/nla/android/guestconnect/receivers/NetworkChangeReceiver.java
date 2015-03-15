package org.nla.android.guestconnect.receivers;

import org.nla.android.guestconnect.services.ConnectService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeReceiver extends BroadcastReceiver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent serviceIntent = new Intent(context, ConnectService.class);
		context.startService(serviceIntent);
	}
}