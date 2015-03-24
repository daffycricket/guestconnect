package org.nla.android.guestconnect.spice;

import org.nla.android.guestconnect.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.octo.android.robospice.JacksonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class SpiceActivity extends Activity {

	private SpiceManager spiceManager = new SpiceManager(
			JacksonGoogleHttpClientSpiceService.class);

	@InjectView(R.id.btnSearch)
	Button mBtnSearch;

	@InjectView(R.id.edtQuery)
	EditText mEdtQuery;

	// Classe interne de l'Activity
	private class QueryRequestListener implements RequestListener<Result> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			Toast.makeText(SpiceActivity.this,
					"Arghhh " + spiceException.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onRequestSuccess(Result result) {
			Toast.makeText(SpiceActivity.this, "Yeah !!!", Toast.LENGTH_LONG)
					.show();
		}
	}

	@OnClick(R.id.btnSearch)
	public void onClickOnConnect(View view) {

		// new ConnectTask(this, this.onAttemptToConnectCallback).execute(
		// this.mEdtEmail.getText().toString().trim(), this.mEdtPassword
		// .getText().toString().trim());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// check internet connexion
		case R.id.action_check_connect_item: {
			return true;
		}

		// show about panel
		case R.id.action_about_item:
			this.showAboutMessage();
			return true;

			// what else...
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showAboutMessage() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(R.string.title_about);
		alertDialogBuilder.setCancelable(false).setPositiveButton(
				R.string.btn_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		alertDialogBuilder.setMessage(this.getString(R.string.msg_about));
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void performRequest(String user) {
		setProgressBarIndeterminateVisibility(true);

		QuerySpiceRequest request = new QuerySpiceRequest();

		spiceManager.execute(request, null, DurationInMillis.ONE_MINUTE,
				new QueryRequestListener());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_spice);
		ButterKnife.inject(this);
	}

	@Override
	protected void onStop() {
		spiceManager.shouldStop();
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();
		spiceManager.start(this);
	}
}