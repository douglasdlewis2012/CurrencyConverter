package com.douglaslewis.currencyconverter.Events;


public class CurrencyAmountChangedEvent {
	public final int currencyAmount;

	public CurrencyAmountChangedEvent (int currencyAmount) {
		this.currencyAmount = currencyAmount;
	}
}
