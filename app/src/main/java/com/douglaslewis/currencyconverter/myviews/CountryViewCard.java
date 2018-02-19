package com.douglaslewis.currencyconverter.myviews;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.douglaslewis.currencyconverter.Application.ApplicationController;
import com.douglaslewis.currencyconverter.Events.FlagClickedEvent;
import com.douglaslewis.currencyconverter.Models.CountryData;
import com.douglaslewis.currencyconverter.R;
import com.douglaslewis.currencyconverter.Utils.FormatStrings;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class CountryViewCard extends CardView {
	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.conversion_amount)
	HugeTextView mConversionAmount;
	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.converted_amount)
	HugeTextView mConvertedAmount;
	@BindView(R.id.flag_image)
	ImageView mFlagImage;

	private final ApplicationController mApplicationController = new ApplicationController ();

	private CountryData mCountryData;

	public CountryViewCard (Context context, AttributeSet attrs) {
		super (context, attrs);
	}

	public CountryViewCard (Context context) {
		super (context);
		Timber.e ("CountryViewCard instantiated");
	}


	@Override
	protected void onFinishInflate () {
		super.onFinishInflate ();
		ButterKnife.bind (this, getRootView ());
		mConversionAmount.setTypeface (mApplicationController.getHugeFont ());
	}

	@Override
	public void setContentDescription (CharSequence contentDescription) {
		super.setContentDescription (contentDescription);
		mFlagImage.setContentDescription (contentDescription + "-image");
	}

	public void setAmount (int amount) {
		BigDecimal d = new BigDecimal (amount);
		String text = FormatStrings.formatMonetaryValue (d, mCountryData.getCountrySymbol ());
		mConvertedAmount.setText (text);
	}

	private void setConversionAmountText (BigDecimal n) {
		String text = FormatStrings.formatConversionAmount (n, mCountryData.getCountrySymbol (), mCountryData.getCountryCode ());
		mConversionAmount.setText (text);
	}

	private void setConvertedAmountText (BigDecimal n) {
		String text = FormatStrings.formatMonetaryValue (n, mCountryData.getCountrySymbol ());

		mConvertedAmount.setText (text);
	}

	private void setFlagIcon (String icon) {
		mApplicationController.loadIconInto (mFlagImage, icon, this, getContext ());
		Timber.d ("finished creating quickview card");
	}

	@OnClick(R.id.flag_image)
	public void onClick (@SuppressWarnings("UnusedParameters") View v) {
		EventBus.getDefault ().post (new FlagClickedEvent (mCountryData.getCountryCode (), mCountryData.getCountryName ()));
		Timber.d ("Image clicked");
	}

	public CountryData getCountryData () {
		return mCountryData;
	}

	public void setCountryData (CountryData data) {
		mCountryData = data;
		setConversionAmountText (data.getConversionAmount ());
		setConvertedAmountText (data.getConvertedAmount ());

		setFlagIcon (data.getCountryCode ());
	}
}
