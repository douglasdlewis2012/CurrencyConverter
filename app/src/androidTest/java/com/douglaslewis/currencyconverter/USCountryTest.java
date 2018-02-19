package com.douglaslewis.currencyconverter;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.douglaslewis.currencyconverter.myviews.CurrencyConverterActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class USCountryTest {

	/*
	The below tests could be normalized so that is one for each set of tests, but I specifically
	want each test to be carried out against the individual languages ie... one test for USD
	one test for EUR etc... The former is cleaner code wise, the later is cleaner test wise.  I need
	to know under all circumstances which test fails, quickly and easily.


	 */

	@Rule
	public ActivityTestRule<CurrencyConverterActivity> mActivityRule = new ActivityTestRule<> (CurrencyConverterActivity.class);


	//check Amount DialogAppear
	@Test
	public void check_ClickUSD () {
		onView (allOf (withId (R.id.country_card), withContentDescription ("USD"))).perform (click ());
		//noinspection unchecked
		onView (allOf (withId (R.id.dialog_currency))).check (matches (isDisplayed ()));
	}

	//check Amount Graph Appear

	@SuppressWarnings("unchecked")
	@Test
	public void check_ClickGBP () {
		onView (allOf (withId (R.id.country_card), withContentDescription ("GBP"))).perform (click ());
		onView (allOf (withId (R.id.linegraph))).check (matches (isDisplayed ()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void check_ClickJPY () {
		onView (allOf (withId (R.id.country_card), withContentDescription ("JPY"))).perform (click ());
		onView (allOf (withId (R.id.linegraph))).check (matches (isDisplayed ()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void check_ClickBRL () {
		onView (allOf (withId (R.id.country_card), withContentDescription ("BRL"))).perform (click ());
		onView (allOf (withId (R.id.linegraph))).check (matches (isDisplayed ()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void check_ClickEUR () {
		onView (allOf (withId (R.id.country_card), withContentDescription ("EUR"))).perform (click ());
		onView (allOf (withId (R.id.linegraph))).check (matches (isDisplayed ()));
	}

//
//	@Test
//	public void check_USDValueChange(){
//		onView (allOf (withId (R.id.country_card), withContentDescription ("USD"))).perform (click ());
//		onView (withId (R.id.dialog_currency)).perform (swipeDown ());
//		onView(withId(R.id.confirm_button)).perform (click ());
//
//
//		onView(allOf(withId(R.id.country_card), withContentDescription ("USD"), withChild (withId (R.id.conversion_amount)))).check(matches(withText ("$ 1.00")));
//
//	}

	//Test SnackBarAppear

	@SuppressWarnings("unchecked")
	@Test
	public void check_SnackBar_USD () {
		onView (allOf (withId (R.id.flag_image), withContentDescription ("USD-image"))).perform (click ());
		onView (allOf (withId (android.support.design.R.id.snackbar_text))).check (matches (isDisplayed ()));
		onView (allOf (withId (android.support.design.R.id.snackbar_text))).check (matches (withText ("USD = United States")));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void check_SnackBar_EUR () {
		onView (allOf (withId (R.id.flag_image), withContentDescription ("EUR-image"))).perform (click ());
		onView (allOf (withId (android.support.design.R.id.snackbar_text))).check (matches (isDisplayed ()));
		onView (allOf (withId (android.support.design.R.id.snackbar_text))).check (matches (withText ("EUR = Europe")));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void check_SnackBar_JPY () {
		onView (allOf (withId (R.id.flag_image), withContentDescription ("JPY-image"))).perform (click ());
		onView (allOf (withId (android.support.design.R.id.snackbar_text))).check (matches (isDisplayed ()));
		onView (allOf (withId (android.support.design.R.id.snackbar_text))).check (matches (withText ("JPY = Japan")));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void check_SnackBar_BRL () {
		onView (allOf (withId (R.id.flag_image), withContentDescription ("BRL-image"))).perform (click ());
		onView (allOf (withId (android.support.design.R.id.snackbar_text))).check (matches (isDisplayed ()));
		onView (allOf (withId (android.support.design.R.id.snackbar_text))).check (matches (withText ("BRL = Brazil")));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void check_SnackBar_GBP () {
		onView (allOf (withId (R.id.flag_image), withContentDescription ("GBP-image"))).perform (click ());
		onView (allOf (withId (android.support.design.R.id.snackbar_text))).check (matches (isDisplayed ()));
		onView (allOf (withId (android.support.design.R.id.snackbar_text))).check (matches (withText ("GBP = Great Britain")));

	}

}
