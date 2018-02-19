package com.douglaslewis.currencyconverter.myviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.douglaslewis.currencyconverter.Events.CountryClickedEvent;
import com.douglaslewis.currencyconverter.Models.CountryData;
import com.douglaslewis.currencyconverter.R;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class CountriesRecyclerAdapter extends RecyclerView.Adapter<CountriesRecyclerAdapter.ViewHolder> {

	private final List<CountryData> mCountryDataElements;

	public CountriesRecyclerAdapter (List<CountryData> dataList) {
		mCountryDataElements = dataList;
	}


	@Override
	public CountriesRecyclerAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

		CountryViewCard card = (CountryViewCard) LayoutInflater.from (parent.getContext ()).inflate (R.layout.country_amount_view, parent, false);

		return new ViewHolder (card);
	}

	@Override
	public void onBindViewHolder (ViewHolder holder, int position) {
		holder.mCountryViewCard.setCountryData (mCountryDataElements.get (position));
		holder.mCountryViewCard.setContentDescription (mCountryDataElements.get (position).getCountryCode ());
	}

	@Override
	public int getItemCount () {
		return mCountryDataElements.size ();
	}

	public void onDataChanged (Map<String, BigDecimal> data) {

		for (CountryData mCountryData : mCountryDataElements) {
			BigDecimal value = data.get (mCountryData.getCountryCode ());
			mCountryData.setConversionAmount (value);
		}

		notifyDataSetChanged ();

	}

	public void onCurrencyAmountChanged (int amount) {
		BigDecimal bigAmount = new BigDecimal (amount);
		for (CountryData data : mCountryDataElements) {
			BigDecimal value = data.getConversionAmount ().multiply (bigAmount);
			data.setConvertedAmount (value);
		}
		notifyDataSetChanged ();

	}

//	public void onDataUpdated(){
//		BigDecimal b = new BigDecimal (8);
//		for (CountryData data : mCountryDataElements) {
//			data.setConversionAmount (b);
////			data.setConvertedAmount (value);
//		}
//		notifyDataSetChanged ();
//
//	}


	static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.country_card)
		CountryViewCard mCountryViewCard;

		public ViewHolder (CountryViewCard v) {
			super (v);
			ButterKnife.bind (this, v);
		}


		@OnClick(R.id.country_card)
		public void onClick (@SuppressWarnings("UnusedParameters") View v) {
			Timber.d ("Pressed button inside viewholder");
			EventBus.getDefault ().post (new CountryClickedEvent (mCountryViewCard.getCountryData ().getCountryCode ()));

		}

	}

}