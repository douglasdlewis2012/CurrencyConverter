package com.douglaslewis.currencyconverter.myviews.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.douglaslewis.currencyconverter.Application.ApplicationController;

import timber.log.Timber;

public class CurrencyAmountNumberPicker extends NumberPicker  implements NumberPicker.OnValueChangeListener{
	private static final ApplicationController mApplicationController = new ApplicationController ();
	public CurrencyAmountNumberPicker (Context context, AttributeSet attrs) {
		super (context, attrs);
		setDividerColor (this, Color.parseColor (mApplicationController.getHugePrimaryColor ()));
		setOnValueChangedListener (this);
	}


	@Override
	public void onValueChange (NumberPicker picker, int oldVal, int newVal) {
		Timber.d("" + newVal);
	}

	//	CustomEditText editText;
	@Override
	public void addView (View child) {
		super.addView (child);
		updateView (child, mApplicationController.getHugePrimaryColor ());

	}

	@Override
	public void addView (View child, int index, android.view.ViewGroup.LayoutParams params) {
		super.addView (child, index, params);
		updateView (child, mApplicationController.getHugePrimaryColor ());
	}

	@Override
	public void addView (View child, android.view.ViewGroup.LayoutParams params) {
		super.addView (child, params);
		updateView (child, mApplicationController.getHugePrimaryColor ());
	}

	private void updateView (View view, String hugeColor) {
		if (view instanceof EditText) {
			EditText editText = (EditText)view;
			editText.setTextSize (35);
			editText.setTextColor (Color.parseColor (hugeColor));
			editText.addTextChangedListener(new TextWatcher (){

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
											  int arg2, int arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					try{
						int num = Integer.parseInt(s.toString());
						setValue(num);
					}catch(NumberFormatException E){
						//Should never be called
						Timber.e("NumberFormatException, where shouldn't be");
					}
				}});

		}

	}

	private void setDividerColor (NumberPicker picker, int color) {
		/*
		There is no direct way to edit the color of the divider in android.
		If I were fully implementing this I would implement my own NumberPicker, so that reflection
		isn't used to adjust the divider color, it is really hacky.

		 */

		java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields ();
		for (java.lang.reflect.Field pf : pickerFields) {
			if (pf.getName ().equals ("mSelectionDivider")) {
				pf.setAccessible (true);
				try {
					ColorDrawable colorDrawable = new ColorDrawable (color);
					pf.set (picker, colorDrawable);
				} catch (IllegalArgumentException | Resources.NotFoundException | IllegalAccessException e) {
					e.printStackTrace ();
				}
				break;
			}
		}
	}

}
