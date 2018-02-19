package com.douglaslewis.currencyconverter.Application;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;

import com.douglaslewis.currencyconverter.CurrencyAPI.CurrencyDateRate;
import com.douglaslewis.currencyconverter.CurrencyAPI.CurrencyDates;
import com.douglaslewis.currencyconverter.CurrencyAPI.Rates;
import com.douglaslewis.currencyconverter.Models.GraphData;
import com.douglaslewis.currencyconverter.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationSingleton {
	/*
	This is the only class in the project I dislike and would re-write.  I dislike how setupData works.
	It hard codes the values

	 */

	private final static ApplicationSingleton INSTANCE = new ApplicationSingleton ();
	private final Map<String, List<String>> mCountryDataMap = new HashMap<> ();
	private final Map<String, Integer> mImagesMap = new HashMap<> ();
	private CurrencyDates mCurrencyDates;
	private CurrencyDateRate mCurrentCurrencyRates;
	@SuppressWarnings("FieldCanBeLocal")
	private final String mHugeColor = "#ec008c";


	private ApplicationSingleton () {
		setupImagesMap ();
	}

	public static ApplicationSingleton getINSTANCE () {
		return INSTANCE;
	}

	public void setupData (Resources resource) {

		TypedArray typedArray = resource.obtainTypedArray (R.array.country_codes);
		int n = typedArray.length ();
		String[][] array = new String[n][];
		for (int i = 0; i < n; ++i) {
			int id = typedArray.getResourceId (i, 0);
			if (id > 0) {
				array[i] = resource.getStringArray (id);
			}
		}
		typedArray.recycle ();

		//noinspection ForLoopReplaceableByForEach
		for (int i = 0; i < array.length; i++) {
			List<String> countryData = new ArrayList<> ();
			Collections.addAll (countryData, array[i]);
			mCountryDataMap.put (countryData.get (countryData.size () - 1), countryData);
		}
	}

	//
	private void setupImagesMap () {

		mImagesMap.put ("BRL", R.drawable.flag_of_brazil);
		mImagesMap.put ("EUR", R.drawable.flag_of_europe);
		mImagesMap.put ("JPY", R.drawable.flag_of_japan);
		mImagesMap.put ("GBP", R.drawable.flag_of_the_united_kingdom);
		mImagesMap.put ("USD", R.drawable.flagoftheunitedstates);
		mImagesMap.put ("default", R.drawable.flagoftheunitedstates);
	}

	public List<String> getCountryData (String country) {
		return mCountryDataMap.get (country);
	}

	public Integer getImage (@NonNull String imageName) {
		Integer location = mImagesMap.get (imageName);
		if (location != null)
			return location;

		return mImagesMap.get ("default");
	}

	public void setCurrencyDates (@NonNull CurrencyDates currencyDates) {
		mCurrencyDates = currencyDates;
	}

	public Map<String, BigDecimal> getLastCurrencyData () {

		if(mCurrentCurrencyRates != null)
			return setCurrencyRates (mCurrentCurrencyRates);

		CurrencyDateRate currencyDay = mCurrencyDates.getCurrencyDateRate ().get (mCurrencyDates.getCurrencyDateRate ().size () - 1);
		Map<String, BigDecimal> rates = setCurrencyRates (currencyDay);


		return rates;
	}

	public void setLastCurrencyData (CurrencyDateRate currencyDateRate) {
		mCurrentCurrencyRates = currencyDateRate;

	}

	private Map<String, BigDecimal>  setCurrencyRates(CurrencyDateRate currencyDay){
		Map<String, BigDecimal> rates = new HashMap<> ();

		rates.put ("JPY", currencyDay.getRates ().getJPY ());
		rates.put ("BRL", currencyDay.getRates ().getBRL ());
		rates.put ("EUR", currencyDay.getRates ().getEUR ());
		rates.put ("GBP", currencyDay.getRates ().getGBP ());

		return rates;
	}

	public Map<String, GraphData> getGraphData () {
		Map<String, GraphData> data = new HashMap<> ();
		int length = mCurrencyDates.getCurrencyDateRate ().size ();
		BigDecimal[] japanList = new BigDecimal[length];
		BigDecimal[] ukList = new BigDecimal[length];
		BigDecimal[] brazilList = new BigDecimal[length];
		BigDecimal[] euroList = new BigDecimal[length];

		for (int i = 0; i < length; i++) {
			CurrencyDateRate date = mCurrencyDates.getCurrencyDateRate ().get (i);
			Rates rates = date.getRates ();

			japanList[i] = rates.getJPY ();
			ukList[i] = rates.getGBP ();
			euroList[i] = rates.getEUR ();
			brazilList[i] = rates.getBRL ();

		}

		data.put ("JPY", new GraphData (japanList));
		data.put ("EUR", new GraphData (euroList));
		data.put ("BRL", new GraphData (brazilList));
		data.put ("GBP", new GraphData (ukList));

		return data;
	}

	public String getHugeColor(){
		return mHugeColor;
	}

}
