package org.nla.android.guestconnect.common;

import java.util.LinkedList;
import java.util.List;

import org.nla.android.guestconnect.common.Event.EventSource;
import org.nla.android.guestconnect.common.Event.EventType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "guestconnect";

	private static final int DATABASE_VERSION = 1;

	private static final String FIELD_CONTENT = "content";

	private static final String FIELD_EVENT_ID = "id";

	private static final String FIELD_EVENT_SOURCE = "event_source";

	private static final String FIELD_EVENT_TYPE = "event_type";

	private static final String FIELD_TS = "creation_ts";

	private static final String TABLE_EVENTS = "events";

	private static final String X_CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_EVENTS + " ("
			+ FIELD_EVENT_ID + " INTEGER PRIMARY KEY, " // id
			+ FIELD_TS + " INTEGER, " // timestamp
			+ FIELD_EVENT_TYPE + " TEXT, " // event type
			+ FIELD_EVENT_SOURCE + " TEXT, " // event source
			+ FIELD_CONTENT + " TEXT);"; // event content

	/**
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void deleteEvents() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EVENTS, null, null);
		db.close();
	}

	public List<Event> getEvents(EventType eventType) {
		List<Event> events = new LinkedList<Event>();

		String query = "SELECT * FROM "
				+ TABLE_EVENTS // select
				+ (eventType == null ? "" : " WHERE " + FIELD_EVENT_TYPE + "= '"
						+ eventType.toString() + "'") // where
				+ " ORDER BY " + FIELD_EVENT_ID + " DESC"; // order

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		Event event = null;
		if (cursor.moveToFirst()) {
			do {
				event = new Event();
				event.setId(Integer.parseInt(cursor.getString(0)));
				event.setCreationTs(Long.parseLong(cursor.getString(1)));
				event.setEventType(EventType.valueOf(cursor.getString(2)));
				event.setEventSource(EventSource.valueOf(cursor.getString(3)));
				event.setContent(cursor.getString(4));
				events.add(event);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		return events;
	}

	public Event getLastConnexionSuccessfulEvent() {
		String query = "SELECT * FROM " + TABLE_EVENTS // select
				+ " WHERE " + FIELD_EVENT_TYPE + "= '" + EventType.CONNEXION_SUCCEEDED + "'" // where
				+ " ORDER BY " + FIELD_EVENT_ID + " DESC"; // order

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		Event event = null;
		if (cursor.moveToFirst()) {
			event = new Event();
			event.setId(Integer.parseInt(cursor.getString(0)));
			event.setCreationTs(Long.parseLong(cursor.getString(1)));
			event.setEventType(EventType.valueOf(cursor.getString(2)));
			event.setEventSource(EventSource.valueOf(cursor.getString(3)));
			event.setContent(cursor.getString(3));
		}
		cursor.close();
		db.close();

		return event;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(X_CREATE_TABLE_EVENTS);
		} catch (SQLException e) {
			throw new IllegalStateException("Unable to create the Sqlite DB", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public void storeEvent(Event event) {
		SQLiteDatabase db = this.getWritableDatabase();
		event.setCreationTs(System.currentTimeMillis());

		ContentValues values = new ContentValues();
		values.put(FIELD_EVENT_TYPE, event.getEventType().toString());
		values.put(FIELD_CONTENT, event.getContent());
		values.put(FIELD_TS, event.getCreationTs());
		values.put(FIELD_EVENT_SOURCE, event.getEventSource().toString());

		long id = db.insert(TABLE_EVENTS, null, values);
		db.close();

		event.setId(id);
	}
}