package com.douglaslewis.currencyconverter.myviews;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.douglaslewis.currencyconverter.R;

import timber.log.Timber;

public abstract class BaseFragmentActivity extends AppCompatActivity {

	FragmentManager mFragmentManager;
	Snackbar mSnackbar;


	@Override
	protected void onCreate (@Nullable Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (getLayoutResID ());

		createSnackBar ();

		Timber.d ("instantiated");

		mFragmentManager = getSupportFragmentManager ();
		Fragment fragment = mFragmentManager.findFragmentById (R.id.fragment_container);

		if (fragment == null) {
			fragment = createFragment ();
			mFragmentManager.beginTransaction ().add (R.id.fragment_container, fragment).commit ();
		}

	}

	private void createSnackBar () {
		View v = this.findViewById (android.R.id.content);
		//noinspection ConstantConditions
		mSnackbar = Snackbar.make (v, "", Snackbar.LENGTH_LONG)
				.setAction ("Action", null);
	}

	@SuppressWarnings("SameReturnValue")
	@LayoutRes
	public int getLayoutResID () {
		return R.layout.activity_currency_converter;
	}


	protected abstract Fragment createFragment ();

}
