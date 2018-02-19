package com.douglaslewis.currencyconverter;

import android.test.suitebuilder.annotation.SmallTest;

import com.douglaslewis.currencyconverter.Models.CountryData;
import com.douglaslewis.currencyconverter.Models.GraphData;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SmallTest
public class CountryDataTest {
//	GraphData mNullGraphData;
//	GraphData mNormalGraphData;
	List<String> mNormalData;
	List<String> mNullData;
	@Before
	public void setupTests(){

		mNormalData = new ArrayList<> ();
		mNormalData.add ("one");
		mNormalData.add("two");
		mNormalData.add("three");
		mNormalData.add("four");
	}

	@Test (expected = IllegalStateException.class)
	public void testCountryDataSetupWithNull(){
		CountryData data = new CountryData (mNullData);
	}

	@Test
	public void testCountryDataSetupWithNormal(){
		CountryData data = new CountryData (mNormalData);
		assertEquals (data.getCountryCode (), mNormalData.get (mNormalData.size ()-1));
		assertEquals (data.getCountryFlag (), mNormalData.get (2));
		assertEquals (data.getCountryName (), mNormalData.get (1));
		assertEquals (data.getCountrySymbol (), mNormalData.get (0));
	}

	@Test
	public void testConversionAmount(){
		CountryData data =  new CountryData (mNormalData);
		BigDecimal conversionAmount = new BigDecimal (4);
		BigDecimal convertedAmount = new BigDecimal (15);
		data.setConversionAmount (conversionAmount);
		data.setConvertedAmount (convertedAmount);

		assertEquals (data.getConversionAmount (), conversionAmount );
		assertEquals (data.getConvertedAmount (), convertedAmount);

	}


}


