package org.nla.android.guestconnect.common;

import java.util.Locale;

import org.nla.android.guestconnect.app.App;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkHelper {

	/**
	 * Indicates whether device is connected to GUEST WIFI.
	 * 
	 * @return
	 */
	public static boolean isConnectedToGuestWifi(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		boolean isGuest = false;
		if (networkInfo != null) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {

				// for debug purposes only
				if (App.getApp().isDebugMode()) {
					return true;
				}

				WifiManager wManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wInfo = wManager.getConnectionInfo();
				String ssid = wInfo.getSSID();
				isGuest = ssid != null && ssid.toLowerCase(Locale.FRANCE).contains("guest");
			}
		}
		return isGuest;
	}

	/**
	 * Indicates whether device is connected to GUEST WIFI.
	 * 
	 * @return
	 */
	public static boolean isConnectedToWifi(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		boolean isGuest = false;
		if (networkInfo != null) {
			boolean isConnected = networkInfo.getDetailedState() == DetailedState.CONNECTED;
			boolean isWifi = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;

			isGuest = isConnected && isWifi;
		}
		return isGuest;
	}
}