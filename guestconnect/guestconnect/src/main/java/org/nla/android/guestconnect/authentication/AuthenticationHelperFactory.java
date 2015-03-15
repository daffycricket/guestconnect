package org.nla.android.guestconnect.authentication;

import org.nla.android.guestconnect.app.App;

public class AuthenticationHelperFactory {

	public static IAuthenticationHelper buildAuthenticationHelper() {
		if (App.getApp().isDebugMode()) {
			return new MockAuthenticationHelper();
		} else {
			return new AuthenticationHelper();
		}
	}
}
