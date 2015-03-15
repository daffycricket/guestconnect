package org.nla.android.guestconnect.tasks;

/**
 * Callback of an AsyncTask
 * 
 * @param <T>
 */
public interface IAsyncCallback<T> {

	public void execute(T result);
}
