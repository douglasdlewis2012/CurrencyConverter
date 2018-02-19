package com.douglaslewis.currencyconverter.myviews.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.view.WindowManager;

import com.douglaslewis.currencyconverter.R;


public abstract class BaseDialogFragment extends DialogFragment {


	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
		super.onActivityCreated (savedInstanceState);

		setAnimation ();
		setDialogFullWidth ();
	}

	private void setAnimation () {
		//Set custom animation
		getDialog ().getWindow ().getAttributes ().windowAnimations = R.style.CurrencyDialogAnimation;
	}

	private void setDialogFullWidth () {
		//Set width of dialog to be match_parent
		WindowManager.LayoutParams wmLayoutParams = new WindowManager.LayoutParams ();
		Window window = getDialog ().getWindow ();
		wmLayoutParams.copyFrom (window.getAttributes ());
		wmLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		window.setAttributes (wmLayoutParams);
	}

}
