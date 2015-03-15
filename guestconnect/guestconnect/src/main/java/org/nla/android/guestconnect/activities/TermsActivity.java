package org.nla.android.guestconnect.activities;

import org.nla.android.guestconnect.R;
import org.nla.android.guestconnect.common.TrackingHelper;
import org.nla.android.guestconnect.common.TrackingHelper.TrackScreen;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Main activity.
 * 
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class TermsActivity extends Activity {

	public class MyTabListener implements ActionBar.TabListener {
		Fragment fragment;

		public MyTabListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// nothing done here
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.fragment_container, this.fragment);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(this.fragment);
		}
	}

	private final Fragment fragmentTabEN = TermsFragment
			.newInstance(R.string.terms_of_use_content_en);

	private final Fragment fragmentTabFR = TermsFragment
			.newInstance(R.string.terms_of_use_content_fr);

	private ActionBar.Tab tab1, tab2;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_terms);

		ActionBar actionBar = this.getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		this.tab1 = actionBar.newTab().setText(R.string.tab_english);
		this.tab2 = actionBar.newTab().setText(R.string.tab_french);

		this.tab1.setTabListener(new MyTabListener(this.fragmentTabEN));
		this.tab2.setTabListener(new MyTabListener(this.fragmentTabFR));

		actionBar.addTab(this.tab1);
		actionBar.addTab(this.tab2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		TrackingHelper.trackScreen(TrackScreen.Terms);
	}
}
