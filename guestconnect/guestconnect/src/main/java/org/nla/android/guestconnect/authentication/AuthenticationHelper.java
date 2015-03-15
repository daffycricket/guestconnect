package org.nla.android.guestconnect.authentication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.nla.android.guestconnect.app.App;
import org.nla.android.guestconnect.common.Constants;

import com.google.gson.JsonObject;

class AuthenticationHelper implements IAuthenticationHelper {

	/**
	 * Url towards portal API.
	 */
	private static final String PORTAL_API_URL = "https://controller.mobile.lan/portal_api.php";

	/**
	 * Json property allowModPwdBySelf.
	 */
	private static final String STR_ALLOWMODPWDBYSELF = "allowModPwdBySelf";

	/**
	 * Authentication template string.
	 */
	private static final String STR_AUTHENTICATION_TEMPLATE = "action=authenticate&login={0}&password={1}&policy_accept=true";

	/**
	 * Json property autoDisconnect.
	 */
	private static final String STR_AUTODISCONNECT = "autoDisconnect";

	/**
	 * Json property code.
	 */
	private static final String STR_CODE = "code";

	/**
	 * Json property disconnect_success.
	 */
	private static final String STR_DISCONNECT_SUCCESS = "disconnect_success";

	/**
	 * Disconnect template string.
	 */
	private static final String STR_DISCONNECT_TEMPLATE = "action=disconnect&login={0}&password_digest={1}";

	/**
	 * Json property error.
	 */
	private static final String STR_ERROR = "error";

	/**
	 * String to search to identify if passaxa is incorrect.
	 */
	private static final String STR_ERROR_BAD_LOGIN = "error_logon_bad-login-or-password";

	/**
	 * Json property getPurchaseSummary.
	 */
	private static final String STR_GETPURCHASESUMMARY = "getPurchaseSummary";

	/**
	 * Json property incomingNetwork.
	 */
	private static final String STR_INCOMINGNETWORK = "incomingNetwork";

	/**
	 * Json property incomingNetworkID.
	 */
	private static final String STR_INCOMINGNETWORKID = "incomingNetworkID";

	/**
	 * Json property incomingVlan.
	 */
	private static final String STR_INCOMINGVLAN = "incomingVlan";

	/**
	 * Json property incomingZone.
	 */
	private static final String STR_INCOMINGZONE = "incomingZone";

	/**
	 * Json property incommingVlan (with 2 m).
	 */
	private static final String STR_INCOMMINGVLAN = "incommingVlan";

	/**
	 * Json property incommingZone (with 2 m).
	 */
	private static final String STR_INCOMMINGZONE = "incommingZone";

	/**
	 * Json property info.
	 */
	private static final String STR_INFO = "info";

	/**
	 * Json property initTimeGMT.
	 */
	private static final String STR_INITTIMEGMT = "initTimeGMT";

	/**
	 * Json property ipAddress.
	 */
	private static final String STR_IP_ADDRESS = "ipAddress";

	/**
	 * Json property login.
	 */
	private static final String STR_LOGIN = "login";

	/**
	 * Json property profile.
	 */
	private static final String STR_PROFILE = "profile";

	/**
	 * Json property passwordDigest.
	 */
	private static final String STR_PWD_DIGEST = "passwordDigest";

	/**
	 * Json property requestedURL.
	 */
	private static final String STR_REQUESTEDURL = "requestedURL";

	/**
	 * Json property services.
	 */
	private static final String STR_SERVICES = "services";

	/**
	 * Json property show: used for storing the actual value of purchase
	 * summary.
	 */
	private static final String STR_SHOW = "show";

	/**
	 * Json property incomingZone.
	 */
	private static final String STR_TIMEZONEOFFSET = "timezoneOffset";

	/**
	 * Json property user. Root property enclosing all the others.
	 */
	private static final String STR_USER = "user";

	/**
	 * Json property universalTime.
	 */
	private static final String STR_UTIME = "universalTime";

	/**
	 * Json property validity.
	 */
	private static final String STR_VALIDITY = "validity";

	/**
	 * Json property value: used for storing the actual value of any property.
	 */
	private static final String STR_VALUE = "value";

	/**
	 * Builds the authentication details object using the response from server.
	 * 
	 * @param authenticationDetails
	 * @return
	 */
	private static AuthenticationDetails buildAuthenticationDetails(JsonObject authenticationDetails) {
		AuthenticationDetails toReturn = new AuthenticationDetails();
		JsonObject userObject = authenticationDetails.get(STR_USER).getAsJsonObject();
		toReturn.setEmail(userObject.get(STR_LOGIN).getAsJsonObject().get(STR_VALUE).getAsString());
		toReturn.setIpAddress(userObject.get(STR_IP_ADDRESS).getAsJsonObject().get(STR_VALUE)
				.getAsString());
		toReturn.setPasswordDigest(userObject.get(STR_PWD_DIGEST).getAsJsonObject().get(STR_VALUE)
				.getAsString());
		toReturn.setProfile(userObject.get(STR_PROFILE).getAsJsonObject().get(STR_VALUE)
				.getAsString());
		toReturn.setServices(userObject.get(STR_SERVICES).getAsJsonObject().get(STR_VALUE)
				.getAsString());

		toReturn.setValidity(userObject.get(STR_VALIDITY).getAsJsonObject().get(STR_VALUE)
				.getAsString());

		toReturn.setTimezoneOffset(userObject.get(STR_TIMEZONEOFFSET).getAsJsonObject()
				.get(STR_VALUE).getAsString());

		toReturn.setRequestedURL(userObject.get(STR_REQUESTEDURL).getAsJsonObject().get(STR_VALUE)
				.getAsString());

		toReturn.setIncomingNetworkID(userObject.get(STR_INCOMINGNETWORKID).getAsJsonObject()
				.get(STR_VALUE).getAsString());

		toReturn.setIncomingNetwork(userObject.get(STR_INCOMINGNETWORK).getAsJsonObject()
				.get(STR_VALUE).getAsString());

		toReturn.setIncomingVlan(userObject.get(STR_INCOMINGVLAN).getAsJsonObject().get(STR_VALUE)
				.getAsString());

		toReturn.setIncomingZone(userObject.get(STR_INCOMINGZONE).getAsJsonObject().get(STR_VALUE)
				.getAsString());

		toReturn.setIncommingVlan(userObject.get(STR_INCOMMINGVLAN).getAsJsonObject()
				.get(STR_VALUE).getAsString());

		toReturn.setIncommingZone(userObject.get(STR_INCOMMINGZONE).getAsJsonObject()
				.get(STR_VALUE).getAsString());

		toReturn.setPurchaseSummary(userObject.get(STR_GETPURCHASESUMMARY).getAsJsonObject()
				.get(STR_SHOW).getAsBoolean());

		toReturn.setAllowModPwdBySelf(userObject.get(STR_ALLOWMODPWDBYSELF).getAsBoolean());

		toReturn.setAutoDisconnect(userObject.get(STR_AUTODISCONNECT).getAsJsonObject()
				.get(STR_VALUE).getAsBoolean());

		// need to multiply by 1000 coz' server returns seconds
		toReturn.setConnexionTime(Long.valueOf(userObject.get(STR_UTIME).getAsJsonObject()
				.get(STR_VALUE).getAsString()
				+ "000"));

		toReturn.setConnexionTime(Long.valueOf(userObject.get(STR_INITTIMEGMT).getAsJsonObject()
				.get(STR_VALUE).getAsString()
				+ "000"));

		return toReturn;
	}

	/**
	 * Creates a HTTP request.
	 * 
	 * @param content
	 *            The request body content.
	 * @param toPost
	 *            The body content to set
	 * @throws IOException
	 *             If an IO error occurs
	 */
	private static HttpResponse executeRequest(String content) throws IOException {
		HttpPost postRequest = new HttpPost(PORTAL_API_URL);
		postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
		postRequest.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		postRequest.setHeader("Origin", "https://controller.mobile.lan");
		postRequest.setHeader("X-Requested-With", "XMLHttpRequest");
		postRequest
				.setHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.137 Safari/537.36");
		postRequest.setHeader("Referer", "https://controller.mobile.lan/102/portal/");
		postRequest.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		postRequest.setHeader("Accept-Language", "fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4");
		postRequest.setEntity(new StringEntity(content));

		HttpClient httpClient = new DefaultHttpClient();
		return httpClient.execute(postRequest);
	}

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
	@Override
	public AuthenticationDetails authenticate(String email, String password)
			throws AuthenticationFailedException {
		if (email == null) {
			throw new IllegalArgumentException("email is null");
		}
		if (password == null) {
			throw new IllegalArgumentException("password is null");
		}

		JsonObject responseObject = null;
		String responseContent = null;
		HttpResponse response = null;

		try {
			String authenticationString = MessageFormat.format(STR_AUTHENTICATION_TEMPLATE,
					URLEncoder.encode(email, "UTF-8"), URLEncoder.encode(password, "UTF-8"));

			response = executeRequest(authenticationString);

			// http response not 200
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new AuthenticationFailedException("Http status: "
						+ response.getStatusLine().getStatusCode());
			}

			responseContent = EntityUtils.toString(response.getEntity());
			App.getApp().storeResponseContentIntoPreferences(responseContent);

			// bad login or pwd
			if (responseContent.contains(STR_ERROR_BAD_LOGIN)) {
				throw new BadPasswordOrLoginException();
			}

			// unexpected exception
			try {
				responseObject = (JsonObject) Constants.JSON_PARSER.parse(responseContent);
			} catch (Exception e) {
				throw new AuthenticationFailedException(e.getMessage(), e);
			}

			// error response
			if (responseObject.get(STR_ERROR) != null) {
				throw new AuthenticationFailedException(responseContent);
			}

		} catch (UnsupportedEncodingException uee) {
			throw new IllegalStateException(
					"UnsupportedEncodingException was thrown where it should not have: "
							+ uee.getMessage());
			// should never happen
		} catch (IOException ioe) {
			// network error
			throw new AuthenticationFailedException(ioe.getMessage(), ioe);
		}

		return buildAuthenticationDetails(responseObject);
	}

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
	@Override
	public boolean disconnect(String email, String passwordDigest) {
		if (email == null) {
			throw new IllegalArgumentException("email is null");
		}
		if (passwordDigest == null) {
			throw new IllegalArgumentException("passwordDigest is null");
		}

		boolean isDisconnected = false;
		String responseContent = null;
		HttpResponse response = null;
		try {
			String authenticationString = MessageFormat.format(STR_DISCONNECT_TEMPLATE,
					URLEncoder.encode(email, "UTF-8"), passwordDigest);
			response = executeRequest(authenticationString);
			responseContent = EntityUtils.toString(response.getEntity());

			JsonObject responseObject = (JsonObject) Constants.JSON_PARSER.parse(responseContent);
			JsonObject infoObject = responseObject.get(STR_INFO).getAsJsonObject();
			String code = infoObject.get(STR_CODE).getAsString();

			if (STR_DISCONNECT_SUCCESS.equals(code)) {
				isDisconnected = true;
			}
		} catch (UnsupportedEncodingException uee) {
			// if happens, consider disconnection failed
		} catch (Exception e) {
			// if happens, consider disconnection failed
		}

		return isDisconnected;
	}
}
