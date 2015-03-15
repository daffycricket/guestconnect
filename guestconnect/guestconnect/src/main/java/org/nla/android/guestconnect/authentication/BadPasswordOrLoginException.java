package org.nla.android.guestconnect.authentication;

@SuppressWarnings("serial")
public class BadPasswordOrLoginException extends AuthenticationFailedException {

	public BadPasswordOrLoginException() {
		super("BadPasswordOrLogin");
	}
}
