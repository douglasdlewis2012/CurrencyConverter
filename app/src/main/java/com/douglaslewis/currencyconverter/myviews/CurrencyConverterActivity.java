package com.douglaslewis.currencyconverter.myviews;

import android.support.v4.app.Fragment;

import com.douglaslewis.currencyconverter.Application.ApplicationController;
import com.douglaslewis.currencyconverter.Events.CountryClickedEvent;
import com.douglaslewis.currencyconverter.Events.FlagClickedEvent;
import com.douglaslewis.currencyconverter.Utils.FormatStrings;
import com.douglaslewis.currencyconverter.myviews.dialogs.BaseDialogFragment;
import com.douglaslewis.currencyconverter.myviews.dialogs.CurrencyAmountDialog;
import com.douglaslewis.currencyconverter.myviews.dialogs.CurrencyGraphDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class CurrencyConverterActivity extends BaseFragmentActivity {
	private final ApplicationController mApplicationController = new ApplicationController ();
	private CurrencyAmountDialog mCurrencyAmountDialog;
	private CurrencyGraphDialog mGraphDialog;

	@Override
	public Fragment createFragment () {
		return CurrencyConverterActivityFragment.newInstance ();
	}


	@Override
	protected void onStart () {
		super.onStart ();
		setupDialogs ();
		EventBus.getDefault ().register (this);
	}

	@Override
	protected void onStop () {
		super.onStop ();
		EventBus.getDefault ().unregister (this);
	}


	private void setupDialogs () {
		mCurrencyAmountDialog = new CurrencyAmountDialog ();
		mGraphDialog = new CurrencyGraphDialog ();
		mGraphDialog.setGraphData (mApplicationController.getGraphData ());

	}

	public void showAmountDialog () {
		if (shouldShowDialog (mCurrencyAmountDialog))
			mCurrencyAmountDialog.show (mFragmentManager, "Currency");
	}

	private void showGraphDialog (String country) {
		if (shouldShowDialog (mGraphDialog)) {
			mGraphDialog.show (mFragmentManager, "Graph");
			mGraphDialog.updateGraphData (country);

		}

	}

	private boolean shouldShowDialog (BaseDialogFragment dialog) {
		//If dialog is already displayed don't want to show it again.
		return dialog != null && !dialog.isAdded ();
	}


	private void showSnackBar (String message) {
		mSnackbar.setText (message).show ();
	}


	@Subscribe
	public void onCountryClickedEvent (CountryClickedEvent event) {
		showGraphDialog (event.which);
	}

	@Subscribe
	public void onFlagClickedEvent (FlagClickedEvent event) {
		showSnackBar (FormatStrings.formatSnackBarTexT (event.mCountryCode, event.mCountryName));
	}


}
