package com.douglaslewis.currencyconverter.Models;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.util.List;

public class CountryData {
	private final String mCountryCode;
	private final String mCountryFlag;
	private final String mCountrySymbol;
	private final String mCountryName;

	private GraphData mGraphData;


	private BigDecimal mConversionAmount;
	private BigDecimal mConvertedAmount;

	public CountryData (@NonNull List<String> countryData) {
		//This needs to check for null because
		// @NonNull can only check at compile time, and this
		//resource is set at runtime, so it could be empty or null
		// It shouldn't be because it comes from the string file, but ya know....
		if (countryData != null && countryData.size () == 4) {
			mCountryCode = countryData.get (countryData.size () - 1);
			mCountrySymbol = countryData.get (0);
			mCountryName = countryData.get (1);
			mCountryFlag = countryData.get (2);

			mConvertedAmount = BigDecimal.ZERO;
			mConversionAmount = BigDecimal.ONE;

		} else {
			throw new IllegalStateException ("countryData should have 4 elements");
		}

	}

	public BigDecimal getConversionAmount () {
		return mConversionAmount;
	}

	public void setConversionAmount (BigDecimal conversionAmount) {
		mConversionAmount = conversionAmount;
	}

	public BigDecimal getConvertedAmount () {
		return mConvertedAmount;
	}

	public void setConvertedAmount (BigDecimal convertedAmount) {
		mConvertedAmount = convertedAmount;
	}

	public String getCountrySymbol () {
		return mCountrySymbol;
	}

	public String getCountryFlag () {
		return mCountryFlag;
	}

	public String getCountryCode () {
		return mCountryCode;
	}

	public String getCountryName () {
		return mCountryName;
	}

	public GraphData getGraphData () {
		return mGraphData;
	}

	public void setGraphData (GraphData graphData) {
		mGraphData = graphData;
	}
}
