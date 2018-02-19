package com.douglaslewis.currencyconverter;

import android.test.suitebuilder.annotation.SmallTest;

import com.douglaslewis.currencyconverter.Models.GraphData;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.Assert.assertEquals;

@SmallTest
public class GraphDataTest {
	BigDecimal[] entries = new BigDecimal[50];
	int min = 0;
	int max = 49;
	BigDecimal[] nullEntries;
	@Before
	public void setupGraphTest(){
		for(int i= max; i>=min;i--){
			entries[i] = new BigDecimal (i);
		}
	}

	@Test (expected = NullPointerException.class)
	public void testNull(){
		GraphData data = new GraphData (nullEntries);
	}

	@Test
	public void testGraphInit(){
		GraphData data = new GraphData (entries);
	}

	@Test
	public void testMin(){
		GraphData data = new GraphData (entries);
		assertEquals(data.getMin (), BigDecimal.valueOf (min));
	}

	@Test (expected = AssertionFailedError.class)
	public void testMinFail(){
		GraphData data = new GraphData (entries);
		assertEquals(data.getMin (), BigDecimal.valueOf (1));
	}

	@Test
	public void testMax(){
		GraphData data = new GraphData (entries);
		assertEquals(data.getMax (), BigDecimal.valueOf (max));
	}

	@Test (expected = AssertionFailedError.class)
	public void testMaxFail(){
		GraphData data = new GraphData (entries);
		assertEquals(data.getMax (), BigDecimal.valueOf (42));
	}


}
