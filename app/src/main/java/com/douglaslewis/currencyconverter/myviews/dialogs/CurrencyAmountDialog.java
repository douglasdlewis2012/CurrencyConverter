package com.douglaslewis.currencyconverter.myviews.dialogs;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import com.douglaslewis.currencyconverter.Application.ApplicationController;
import com.douglaslewis.currencyconverter.Events.CurrencyAmountChangedEvent;
import com.douglaslewis.currencyconverter.R;

import org.greenrobot.eventbus.EventBus;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class CurrencyAmountDialog extends BaseDialogFragment {
	private final String mDialogTittle = "Conversion Amount";
	//ButterKnife needs the fields to be package private, not private to inject views.
	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.numberPicker)
	NumberPicker mNumberPicker;
	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.confirm_button)
	Button mConfirmButton;
	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.cancel_button)
	Button mCanelButton;

	private final ApplicationController mApplicationController = new ApplicationController ();

	private int mPickerValue = 0;


	public CurrencyAmountDialog () {
	}

	@Nullable
	@Override
	public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		getDialog ().getWindow ().requestFeature (Window.FEATURE_NO_TITLE);

		View v = inflater.inflate (R.layout.currency_dialog, container, false);

		ButterKnife.bind (this, v);

		AlertDialog.Builder builder = new AlertDialog.Builder (getContext ());
		builder.setView (v);

		mNumberPicker.setMinValue (0);
		mNumberPicker.setMaxValue (100000);
		mNumberPicker.setVisibility (View.VISIBLE);
		mNumberPicker.setValue (mPickerValue);

		mCanelButton.setTypeface (mApplicationController.getHugeFont ());
		mConfirmButton.setTypeface (mApplicationController.getHugeFont ());

		Timber.d ("View created");
		return v;
	}

	@OnClick({R.id.confirm_button, R.id.cancel_button})
	public void onClick (View v) {
		switch (v.getId ()) {

			case R.id.cancel_button:
				Timber.d ("Pressed cancel");
				break;

			case R.id.confirm_button:
				Timber.d ("Pressed confirm");
				int tempValue = mNumberPicker.getValue ();
				mNumberPicker.clearFocus ();

				EventBus.getDefault ().post (new CurrencyAmountChangedEvent (tempValue));
				mPickerValue = tempValue;
				break;
		}

		this.dismiss ();

	}

}

//Note #1 https://github.com/JakeWharton/butterknife/issues/515