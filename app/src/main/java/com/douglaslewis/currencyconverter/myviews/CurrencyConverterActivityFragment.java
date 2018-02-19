package com.douglaslewis.currencyconverter.myviews;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.douglaslewis.currencyconverter.Application.ApplicationController;
import com.douglaslewis.currencyconverter.CurrencyAPI.CurrencyDateRate;
import com.douglaslewis.currencyconverter.Events.CurrencyAmountChangedEvent;
import com.douglaslewis.currencyconverter.Events.DataUpdatedEvent;
import com.douglaslewis.currencyconverter.Models.CountryData;
import com.douglaslewis.currencyconverter.Networking.APIManager;
import com.douglaslewis.currencyconverter.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class CurrencyConverterActivityFragment extends Fragment implements APIManager.NetworkResponseCallback {

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.country_card)
	CountryViewCard mCountryViewCard;
	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.country_cardview_recycler)
	RecyclerView mCountriesRecyclerView;

	private final ApplicationController mApplicationController = new ApplicationController ();
	private CountriesRecyclerAdapter mCountriesRecyclerAdapater;

	@SuppressWarnings("WeakerAccess")
	public CurrencyConverterActivityFragment () {
	}

	public static CurrencyConverterActivityFragment newInstance () {
		return new CurrencyConverterActivityFragment ();
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
							  Bundle savedInstanceState) {
		View v = inflater.inflate (R.layout.currency_fragment_container, container, false);
		ButterKnife.bind (this, v);
		setRetainInstance (true);
		return v;
	}

	@Override
	public void onStart () {
		super.onStart ();
		EventBus.getDefault ().register (this);
	}

	@Override
	public void onStop () {
		super.onStop ();
		EventBus.getDefault ().unregister (this);
	}

	@Override
	public void onResume () {
		super.onResume ();

		mApplicationController.loadIconInto (mCountryViewCard.mFlagImage, "US", this, getContext ());
		mCountryViewCard.setCountryData (new CountryData (mApplicationController.getCountryDataByCode ("USD")));
		mCountryViewCard.setContentDescription ("USD");

		mCountriesRecyclerView.setHasFixedSize (true);
		setupLayoutManager (mCountriesRecyclerView);

		List<CountryData> mCountryData = mApplicationController.getCountryData ();

		mCountriesRecyclerAdapater = new CountriesRecyclerAdapter (mCountryData);
		mCountriesRecyclerView.setAdapter (mCountriesRecyclerAdapater);



	}


	@Override
	public void onNetworkResponse (CurrencyDateRate response) {
//		forecast.toString ();
//		Timber.d (forecast.getRates ().getBGN ().toString ());

		Timber.d ("Got response");
	}

	@Override
	public void onNetworkError () {
		Timber.d ("Network error");
	}


	private void setupLayoutManager (RecyclerView view) {
		LinearLayoutManager manager = new LinearLayoutManager (getContext ());

		view.addItemDecoration (new CardSpacingDecoration ());
		manager.setOrientation (LinearLayout.VERTICAL);

		view.setLayoutManager (manager);
	}

	public void updateData (Map<String, BigDecimal> data) {
		mCountriesRecyclerAdapater.onDataChanged (data);
	}

	@OnClick(R.id.country_card)
	public void onClick (@SuppressWarnings("UnusedParameters") View v) {

		((CurrencyConverterActivity) getActivity ()).showAmountDialog ();
		Timber.d ("This is being shown");
	}

	@Subscribe
	public void onCurrencyAmountChangedEvent (CurrencyAmountChangedEvent event) {
		mCountryViewCard.setAmount (event.currencyAmount);
		mCountriesRecyclerAdapater.onCurrencyAmountChanged (event.currencyAmount);
	}

	@Subscribe
	public void onDataUpdatedEvent(DataUpdatedEvent event){
		Timber.d("EVERT----------------------------------------------------------!!!!!!!!!!!!!!!!!!");
		mCountriesRecyclerAdapater.onDataChanged (mApplicationController.getLastCountryRates ());

		//.69
		//106.58
		//.87
		//3.57
	}

	private final class CardSpacingDecoration extends RecyclerView.ItemDecoration {
		//Utility to space Cards in recylcerview.
		@SuppressWarnings("FieldCanBeLocal")
		private final int mSpacing = 20;

		public CardSpacingDecoration () {

		}

		@Override
		public void getItemOffsets (Rect outRect, View v, RecyclerView parent, RecyclerView.State state) {
			outRect.bottom = mSpacing;
		}

	}

}
