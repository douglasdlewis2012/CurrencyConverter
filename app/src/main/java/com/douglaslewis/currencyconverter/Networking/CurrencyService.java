package com.douglaslewis.currencyconverter.Networking;

import com.douglaslewis.currencyconverter.CurrencyAPI.CurrencyDateRate;

import retrofit2.http.GET;
import rx.Observable;

@SuppressWarnings("WeakerAccess")
public interface CurrencyService {
//$ curl -g http://api.fixer.io/latest?base=USD
	@GET("latest?base=USD")
	Observable<CurrencyDateRate> getLatestCurrency (

	);


}
