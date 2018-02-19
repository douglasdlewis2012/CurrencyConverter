package com.douglaslewis.currencyconverter.Networking;

import com.douglaslewis.currencyconverter.CurrencyAPI.CurrencyDateRate;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class APIManager {

	@SuppressWarnings("FieldCanBeLocal")
	private Retrofit restAdapter;
	private final String backend_url;
	private final NetworkResponseCallback mResponseCallback;

	public APIManager (NetworkResponseCallback callback, String backend) {
		mResponseCallback = callback;
		backend_url = backend;


	}

	public final void makeNetworkCall () {



		restAdapter = new Retrofit.Builder ()
				.baseUrl (backend_url)
				.addConverterFactory (GsonConverterFactory.create ())
				.addCallAdapterFactory (RxJavaCallAdapterFactory.create ())
				.build ();

		CurrencyService currencyService = restAdapter.create (CurrencyService.class);

		//Should be saving the subscription and canceling if no longer valid, but
		// you always want the network call to finish.  So always allow the call to finish and save its data.

		Observable<CurrencyDateRate> call = currencyService.getLatestCurrency ();
		@SuppressWarnings("UnusedAssignment") Subscription subscription = call.subscribeOn (Schedulers.io ())
				.observeOn (AndroidSchedulers.mainThread ())
				.subscribe (new Subscriber<CurrencyDateRate> () {
					@Override
					public void onCompleted () {
						Timber.d ("OnCompleted");
					}

					@Override
					public void onError (Throwable e) {
						Timber.e ("Error on network call");

						mResponseCallback.onNetworkError ();
						if (e instanceof HttpException) {
							HttpException response = (HttpException) e;
							int code = response.code ();
							Timber.e (e.toString ());
							Timber.e ("Oncreate", Integer.toString (code));
						}

						mResponseCallback.onNetworkError ();
					}

					@Override
					public void onNext (CurrencyDateRate forecast) {
						mResponseCallback.onNetworkResponse (forecast);
						Timber.d ("got the data");
					}
				});

	}

	public interface NetworkResponseCallback {
		void onNetworkResponse ( CurrencyDateRate currencyDateRate);

		void onNetworkError ();
	}
}
