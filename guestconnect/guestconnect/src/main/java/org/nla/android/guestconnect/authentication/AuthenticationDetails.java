package org.nla.android.guestconnect.authentication;

public class AuthenticationDetails {

	private boolean allowModPwdBySelf;

	private boolean autoDisconnect;

	private long connexionTime;

	private String email;

	private String incomingNetwork;

	private String incomingNetworkID;

	private String incomingVlan;

	private String incomingZone;

	private String incommingVlan;

	private String incommingZone;

	private long initTimeGMT;

	private String ipAddress;

	private String passwordDigest;

	private String profile;

	private boolean purchaseSummary;

	private String requestedURL;

	private String services;

	private String timezoneOffset;

	private String validity;

	public long getConnexionTime() {
		return this.connexionTime;
	}

	public String getEmail() {
		return this.email;
	}

	public String getIncomingNetwork() {
		return this.incomingNetwork;
	}

	public String getIncomingNetworkID() {
		return this.incomingNetworkID;
	}

	public String getIncomingVlan() {
		return this.incomingVlan;
	}

	public String getIncomingZone() {
		return this.incomingZone;
	}

	public String getIncommingVlan() {
		return this.incommingVlan;
	}

	public String getIncommingZone() {
		return this.incommingZone;
	}

	public long getInitTimeGMT() {
		return this.initTimeGMT;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public String getPasswordDigest() {
		return this.passwordDigest;
	}

	public String getProfile() {
		return this.profile;
	}

	public String getRequestedURL() {
		return this.requestedURL;
	}

	public String getServices() {
		return this.services;
	}

	public String getTimezoneOffset() {
		return this.timezoneOffset;
	}

	public String getValidity() {
		return this.validity;
	}

	public boolean isAllowModPwdBySelf() {
		return this.allowModPwdBySelf;
	}

	public boolean isAutoDisconnect() {
		return this.autoDisconnect;
	}

	public boolean isPurchaseSummary() {
		return this.purchaseSummary;
	}

	public void setAllowModPwdBySelf(boolean allowModPwdBySelf) {
		this.allowModPwdBySelf = allowModPwdBySelf;
	}

	public void setAutoDisconnect(boolean autoDisconnect) {
		this.autoDisconnect = autoDisconnect;
	}

	public void setConnexionTime(long connexionTime) {
		this.connexionTime = connexionTime;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setIncomingNetwork(String incomingNetwork) {
		this.incomingNetwork = incomingNetwork;
	}

	public void setIncomingNetworkID(String incomingNetworkID) {
		this.incomingNetworkID = incomingNetworkID;
	}

	public void setIncomingVlan(String incomingVlan) {
		this.incomingVlan = incomingVlan;
	}

	public void setIncomingZone(String incomingZone) {
		this.incomingZone = incomingZone;
	}

	public void setIncommingVlan(String incommingVlan) {
		this.incommingVlan = incommingVlan;
	}

	public void setIncommingZone(String incommingZone) {
		this.incommingZone = incommingZone;
	}

	public void setInitTimeGMT(long initTimeGMT) {
		this.initTimeGMT = initTimeGMT;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setPasswordDigest(String passwordDigest) {
		this.passwordDigest = passwordDigest;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public void setPurchaseSummary(boolean purchaseSummary) {
		this.purchaseSummary = purchaseSummary;
	}

	public void setRequestedURL(String requestedURL) {
		this.requestedURL = requestedURL;
	}

	public void setServices(String services) {
		this.services = services;
	}

	public void setTimezoneOffset(String timezoneOffset) {
		this.timezoneOffset = timezoneOffset;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AuthenticationDetails [allowModPwdBySelf=" + this.allowModPwdBySelf
				+ ", autoDisconnect=" + this.autoDisconnect + ", connexionTime="
				+ this.connexionTime + ", email=" + this.email + ", purchaseSummary="
				+ this.purchaseSummary + ", incomingNetwork=" + this.incomingNetwork
				+ ", incomingNetworkID=" + this.incomingNetworkID + ", incomingVlan="
				+ this.incomingVlan + ", incomingZone=" + this.incomingZone + ", incommingVlan="
				+ this.incommingVlan + ", incommingZone=" + this.incommingZone + ", initTimeGMT="
				+ this.initTimeGMT + ", ipAddress=" + this.ipAddress + ", passwordDigest="
				+ this.passwordDigest + ", profile=" + this.profile + ", requestedURL="
				+ this.requestedURL + ", services=" + this.services + ", timezoneOffset="
				+ this.timezoneOffset + ", validity=" + this.validity + "]";
	}
}