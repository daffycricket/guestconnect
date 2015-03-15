package org.nla.android.guestconnect.common;

public class Event {

	public enum EventSource {
		SVC, // event originated from UI
		UI, // event originated from service
	}

	public enum EventType {
		CONNEXION_FAILED, // connexion failed due to incorrect credentials
		CONNEXION_FAILED_INCORRECT_PASSAXA, // connexion failed due to unknown
											// reason
		CONNEXION_SUCCEEDED, // connexion succedeed
	}

	private String content;

	private long creationTs;

	private EventSource eventSource;

	private EventType eventType;

	private long id;

	public String getContent() {
		return this.content;
	}

	public long getCreationTs() {
		return this.creationTs;
	}

	public EventSource getEventSource() {
		return this.eventSource;
	}

	public EventType getEventType() {
		return this.eventType;
	}

	public long getId() {
		return this.id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreationTs(long creationTs) {
		this.creationTs = creationTs;
	}

	public void setEventSource(EventSource eventSource) {
		this.eventSource = eventSource;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public void setId(long id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Event [content=" + this.content + ", creationTs=" + this.creationTs
				+ ", eventSource=" + this.eventSource + ", eventType=" + this.eventType + ", id="
				+ this.id + "]";
	}
}
