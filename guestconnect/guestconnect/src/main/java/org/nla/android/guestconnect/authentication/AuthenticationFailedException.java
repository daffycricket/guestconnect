package org.nla.android.guestconnect.authentication;

@SuppressWarnings("serial")
public class AuthenticationFailedException extends Exception {

	public AuthenticationFailedException() {
		super();
	}

	public AuthenticationFailedException(String detailMessage) {
		super(detailMessage);
	}

	public AuthenticationFailedException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
}
