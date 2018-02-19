package com.douglaslewis.currencyconverter.Events;

public class FlagClickedEvent {
	public final String mCountryCode;
	public final String mCountryName;

	public FlagClickedEvent (String countryCode, String countryName) {
		mCountryCode = countryCode;
		mCountryName = countryName;
	}
}
