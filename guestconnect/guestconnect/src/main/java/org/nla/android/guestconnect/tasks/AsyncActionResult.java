package org.nla.android.guestconnect.tasks;

/**
 * Class for handling async result.
 * 
 * @author Nico
 * 
 */
public class AsyncActionResult {

	public enum ResultState {
		ACTION_FAILED, ACTION_SUCCEEDED
	}

	private Exception exception;

	private ResultState status;

	public Exception getException() {
		return this.exception;
	}

	public ResultState getStatus() {
		return this.status;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public void setStatus(ResultState status) {
		this.status = status;
	}
}
