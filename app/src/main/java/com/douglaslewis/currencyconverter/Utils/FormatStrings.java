package com.douglaslewis.currencyconverter.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class FormatStrings {
	private static final DecimalFormat df = new DecimalFormat ("#,###.00");


	private FormatStrings () {
	}//no instances, helper;

	public static String formatMonetaryValue (BigDecimal value, String countrySymbol) {
		return String.format ("%s %s", countrySymbol, df.format (value));

	}

	public static String formatConversionAmount (BigDecimal value, String countrySymbol, String countryCode) {
		return String.format ("$1 USD = %s %s %s", countrySymbol, df.format (value), countryCode);
	}


	public static String formatSnackBarTexT (String countryCode, String countryName) {
		return String.format ("%s = %s", countryCode, countryName);
	}

}
