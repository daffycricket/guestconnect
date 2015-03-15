package org.nla.android.guestconnect.authentication;


public interface IAuthenticationHelper {

	/**
	 * Attempts authentication over HTTP. It returns the password digest if
	 * authentication succeeds, or throws an AuthenticationFailedException
	 * otherwise.
	 * 
	 * @param email
	 *            The user email
	 * @param password
	 *            The user password
	 * @return The json object sent by server
	 * @throws AuthenticationFailedException
	 *             If anything fails
	 */
	public AuthenticationDetails authenticate(String email, String password)
			throws AuthenticationFailedException;

	/**
	 * Attempts disconnection over HTTP. Returns true if disconnection
	 * succeeded, false otherwise.
	 * 
	 * @param email
	 *            User email
	 * @param passwordDigest
	 *            Associated password digest
	 * @return true if disconnection succeeded, false otherwise.
	 */
	public boolean disconnect(String email, String passwordDigest);
}
