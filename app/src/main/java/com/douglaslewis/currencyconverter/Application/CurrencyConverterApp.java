package com.douglaslewis.currencyconverter.Application;

import android.app.Application;

import com.douglaslewis.currencyconverter.BuildConfig;

import com.douglaslewis.currencyconverter.CurrencyAPI.CurrencyDateRate;
import com.douglaslewis.currencyconverter.Events.DataUpdatedEvent;
import com.douglaslewis.currencyconverter.Networking.APIManager;
import com.squareup.leakcanary.LeakCanary;

import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

@SuppressWarnings("WeakerAccess")
public class CurrencyConverterApp extends Application implements APIManager.NetworkResponseCallback{
	private final String BACKEND_URL = "http://api.fixer.io/";
	private APIManager mAPIManager;
	private final ApplicationController mApplicationController = new ApplicationController ();


	@Override
	public void onCreate () {
		super.onCreate ();

		if (BuildConfig.DEBUG) {
			Timber.plant (new Timber.DebugTree ());
			LeakCanary.install (this);
		}


		mApplicationController.setup (getResources ());

		mAPIManager = new APIManager (this, BACKEND_URL);
		mAPIManager.makeNetworkCall ();

	}


	@Override
	public void onNetworkResponse (@SuppressWarnings("UnusedParameters") CurrencyDateRate currencyDateRate) {
		Timber.d(currencyDateRate.getBase ().toString ());
		mApplicationController.setLastCountryRates (currencyDateRate);
		EventBus.getDefault ().post (new DataUpdatedEvent ());

		Timber.d("=------------------------------------=");
	}

	@Override
	public void onNetworkError () {

	}
}
