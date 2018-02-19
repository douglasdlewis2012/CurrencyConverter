package com.douglaslewis.currencyconverter.Application;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.douglaslewis.currencyconverter.CurrencyAPI.CurrencyDateRate;
import com.douglaslewis.currencyconverter.CurrencyAPI.CurrencyDates;
import com.douglaslewis.currencyconverter.Models.CountryData;
import com.douglaslewis.currencyconverter.Models.GraphData;
import com.douglaslewis.currencyconverter.R;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class ApplicationController {
	private static Typeface hugeFont;

	public ApplicationController () {
	}

	public void setup (@NonNull Resources res) {

		ApplicationSingleton.getINSTANCE ();
		ApplicationSingleton.getINSTANCE ().setupData (res);
		deserializePreviousJSON (res);
		hugeFont = Typeface.createFromAsset (res.getAssets (), "huge_agb_v5.ttf");

	}

	private void deserializePreviousJSON (@NonNull Resources res) {

		CurrencyDates currencyDates = null;

		try {
			InputStream inputStream = res.openRawResource (R.raw.previous_currency_rates);
			Reader reader = new InputStreamReader (inputStream, "UTF-8");
			currencyDates = new Gson ().fromJson (reader, CurrencyDates.class);

		} catch (Exception e) {
			Timber.d ("Something terrible happened");
		}

		assert currencyDates != null;
		ApplicationSingleton.getINSTANCE ().setCurrencyDates (currencyDates);
	}


	private boolean testNetworkUP (@NonNull Context context) {
		ConnectivityManager cm =
				(ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo ();

		return activeNetwork != null &&
				activeNetwork.isConnectedOrConnecting ();
	}


	public void loadIconInto (@NonNull ImageView image, @NonNull String imageStr, @NonNull Object tag, @NonNull Context context) {

		Integer which = ApplicationSingleton.getINSTANCE ().getImage (imageStr);

		Picasso.with (context).load (which).resize (200, 0).tag (tag).into (image, new Callback () {
			@Override
			public void onSuccess () {
				Timber.e ("On Success");
			}

			@Override
			public void onError () {
				Timber.e ("Error loading image");
			}
		});
	}

	public List<String> getCountryDataByCode (String name) {
		return ApplicationSingleton.getINSTANCE ().getCountryData (name);
	}

	public List<CountryData> getCountryData () {
		List<CountryData> mCountryData = new ArrayList<> ();
		Map<String, BigDecimal> rates = getLastCountryRates ();

		for (String key : rates.keySet ()) {
			CountryData data = new CountryData (getCountryDataByCode (key));
			data.setConversionAmount (rates.get (data.getCountryCode ()));
			mCountryData.add (data);
		}

		return mCountryData;
	}

	public Map<String, BigDecimal> getLastCountryRates () {
		return ApplicationSingleton.getINSTANCE ().getLastCurrencyData ();
	}

	public Map<String, GraphData> getGraphData () {
		return ApplicationSingleton.getINSTANCE ().getGraphData ();
	}

	public void setLastCountryRates(CurrencyDateRate currencyDateRate){
		ApplicationSingleton.getINSTANCE ().setLastCurrencyData (currencyDateRate);
	}

	public Typeface getHugeFont () {
		return hugeFont;
	}

	public String getHugePrimaryColor(){
		return ApplicationSingleton.getINSTANCE ().getHugeColor ();
	}
}
