package com.douglaslewis.currencyconverter.myviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class HugeTextView extends TextView {
	public HugeTextView (Context context) {
		super (context);
		init();
	}

	public HugeTextView (Context context, AttributeSet attrs) {
		super (context, attrs);
		init();
	}

	public HugeTextView (Context context, AttributeSet attrs, int defStyleAttr) {
		super (context, attrs, defStyleAttr);
		init();
	}

	private void init(){
		Typeface font = Typeface.createFromAsset(getContext ().getAssets(), "huge_agb_v5.ttf");

		setTypeface (font);
	}
}
