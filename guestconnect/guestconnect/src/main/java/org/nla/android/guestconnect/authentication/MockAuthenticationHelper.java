package org.nla.android.guestconnect.authentication;

import java.util.UUID;

class MockAuthenticationHelper implements IAuthenticationHelper {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nla.android.axaguestconnect.common.IAuthenticationHelper#authenticate
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public AuthenticationDetails authenticate(String email, String password)
			throws AuthenticationFailedException {
		if (email == null) {
			throw new IllegalArgumentException("email is null");
		}
		if (password == null) {
			throw new IllegalArgumentException("password is null");
		}

		// simulates network call
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}

		final String correctEmail = "bob.mauranne@indo.fr";
		final String correctPassword = "missclarke";

		if (email.equals(correctEmail) && password.equals(correctPassword)) {
			AuthenticationDetails details = new AuthenticationDetails();
			details.setEmail(correctEmail);
			details.setIpAddress("192.5.101.6");
			details.setServices("Full_Access");
			details.setIncomingNetwork("MOCK");
			details.setIncomingNetworkID("MOCK");
			details.setConnexionTime(System.currentTimeMillis());
			details.setPasswordDigest(UUID.randomUUID().toString());

			return details;
		} else if (email.equals(correctEmail) && !password.equals(correctPassword)) {
			throw new BadPasswordOrLoginException();
		} else {
			throw new AuthenticationFailedException("(Un)expected mocked error! Try to log with "
					+ correctEmail + " and his girl :)");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nla.android.axaguestconnect.common.IAuthenticationHelper#disconnect
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public boolean disconnect(String email, String passwordDigest) {

		// simulates network call
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}

		return true;
	}
}
