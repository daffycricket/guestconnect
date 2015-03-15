package org.nla.android.guestconnect.activities;

import org.nla.android.guestconnect.R;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Fragment for showing use the terms of use.
 * 
 * @author Nico
 * 
 */
public class TermsFragment extends Fragment {

	/**
	 * Creates a new TermsFragment with terms string id.
	 * 
	 * @param termsId
	 * @return
	 */
	public static TermsFragment newInstance(int termsId) {
		TermsFragment f = new TermsFragment();
		Bundle args = new Bundle();
		args.putInt("termsId", termsId);
		f.setArguments(args);
		return f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle args = this.getArguments();
		int termsId = args.getInt("termsId", 0);

		View view = inflater.inflate(R.layout.terms_tab, container, false);
		TextView txtTerms = (TextView) view.findViewById(R.id.txtTerms);
		txtTerms.setText(Html.fromHtml(this.getText(termsId).toString()));
		return view;
	}
}
