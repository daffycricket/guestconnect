package org.nla.android.guestconnect.activities;

import java.util.Date;
import java.util.List;

import org.nla.android.guestconnect.R;
import org.nla.android.guestconnect.authentication.AuthenticationDetails;
import org.nla.android.guestconnect.common.Constants;
import org.nla.android.guestconnect.common.DatabaseHelper;
import org.nla.android.guestconnect.common.Event;
import org.nla.android.guestconnect.common.Event.EventSource;
import org.nla.android.guestconnect.common.Event.EventType;
import org.nla.android.guestconnect.common.TrackingHelper;
import org.nla.android.guestconnect.common.TrackingHelper.TrackAction;
import org.nla.android.guestconnect.common.TrackingHelper.TrackCategory;
import org.nla.android.guestconnect.common.TrackingHelper.TrackScreen;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryActivity extends ListActivity {

	protected class EventAdapter extends ArrayAdapter<Event> {

		private final Context context;

		private final List<Event> events;

		protected EventAdapter(Context context, int layoutResourceId, List<Event> events) {
			super(HistoryActivity.this, layoutResourceId, events);
			this.context = context;
			this.events = events;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) this.context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				row = inflater.inflate(R.layout.row_event, parent, false);
			}

			TextView txtCreateTs = (TextView) row.findViewById(R.id.txtCreateTs);
			TextView txtContent = (TextView) row.findViewById(R.id.txtContent);

			Event event = this.events.get(position);

			txtCreateTs.setText(Constants.PST_FORMAT.format(new Date(event.getCreationTs())));
			txtContent.setText(this.buildContentText(event));
			row.setBackgroundColor(position % 2 == 0 ? Color.WHITE : Color.LTGRAY);

			return row;
		}

		private String buildContentText(Event event) {
			String toReturn = "";
			String logonType = HistoryActivity.this
					.getString(event.getEventSource() == EventSource.SVC ? R.string.msg_logon_type_auto
							: R.string.msg_logon_type_manual);

			// Incorrect PassAXA
			if (event.getEventType() == EventType.CONNEXION_FAILED_INCORRECT_PASSAXA) {
				toReturn = HistoryActivity.this.getString(
						R.string.msg_logon_failed_invalid_passaxa, logonType);
				// Unknown error
			} else if (event.getEventType() == EventType.CONNEXION_FAILED) {
				toReturn = HistoryActivity.this.getString(R.string.msg_logon_failed_with_details,
						logonType, event.getContent());
				// Success
			} else if (event.getEventType() == EventType.CONNEXION_SUCCEEDED) {
				AuthenticationDetails authenticationDetails = Constants.GSON.fromJson(
						event.getContent(), AuthenticationDetails.class);

				toReturn = HistoryActivity.this.getString(R.string.msg_logon_succeeded, logonType,
						authenticationDetails.getEmail(), authenticationDetails.getServices(),
						authenticationDetails.getIpAddress());
			}

			return toReturn;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_history);
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		this.setListAdapter(new EventAdapter(this, android.R.layout.simple_list_item_1, dbHelper
				.getEvents(null)));
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	// * android.view.View, int, long)
	// */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		final Event event = (Event) l.getItemAtPosition(position);

		if (event != null && event.getEventType() == EventType.CONNEXION_SUCCEEDED) {
			TrackingHelper.trackEvent(TrackCategory.History, TrackAction.ClickOnHistoryDetail);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle(R.string.title_authentication_details);
			alertDialogBuilder.setCancelable(false).setPositiveButton(R.string.btn_ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			alertDialogBuilder.setMessage(event.getContent() != null ? event.getContent()
					.toString() : event.toString());
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		TrackingHelper.trackScreen(TrackScreen.History);
	}
}
