package com.douglaslewis.currencyconverter.myviews.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.douglaslewis.currencyconverter.Models.GraphData;
import com.douglaslewis.currencyconverter.R;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrencyGraphDialog extends BaseDialogFragment {
	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.linegraph)
	CurrencyGraphView mGraphView;
	private Map<String, GraphData> mGraphData;
	private String mCurrentCountryCode;

	public CurrencyGraphDialog () {

	}

	@NonNull
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {
		@SuppressLint("InflateParams") View v = LayoutInflater.from (getContext ()).inflate (R.layout.currency_graph_layout, null);
		ButterKnife.bind (this, v);

		mGraphView.changeData (mGraphData.get (mCurrentCountryCode));

		AlertDialog.Builder builder = new AlertDialog.Builder (getContext ());
		builder.setView (v);

		return builder.create ();
	}


	public void setGraphData (Map<String, GraphData> data) {

		mGraphData = data;
	}

	public void updateGraphData (String country) {
		mCurrentCountryCode = country;

		if (mGraphView != null)
			mGraphView.changeData (mGraphData.get (mCurrentCountryCode));

	}

}
