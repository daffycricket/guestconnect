package org.nla.android.guestconnect.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class Constants {

	/**
	 * Global Gson.
	 */
	public static final Gson GSON = new Gson();

	/**
	 * Global Json parser.
	 */
	public static final JsonParser JSON_PARSER = new JsonParser();

	/**
	 * Date format for showing connexion time.
	 */
	public static final DateFormat PST_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
			Locale.getDefault());
	static {
		PST_FORMAT.setTimeZone(TimeZone.getDefault());
	}
}
