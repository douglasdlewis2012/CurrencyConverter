package com.douglaslewis.currencyconverter.Models;

import java.math.BigDecimal;

public class GraphData {
	private BigDecimal min;
	private BigDecimal max;
	private final BigDecimal[] entries;

	public GraphData (BigDecimal[] addedEntries) {

		this.entries = addedEntries;
		setMaxMin ();
	}

	public BigDecimal[] getEntries () {

		return entries;
	}

	public BigDecimal getMax () {
		return max;
	}

	public BigDecimal getMin () {
		return min;
	}

	private void setMaxMin () {
		BigDecimal maximum = new BigDecimal (Integer.MIN_VALUE);
		BigDecimal minimum = new BigDecimal (Integer.MAX_VALUE);

		for (BigDecimal d : entries) {

			if (d.compareTo (maximum) > 0)
				maximum = d;

			if (d.compareTo (minimum) < 0)
				minimum = d;
		}

		max = maximum;
		min = minimum;
	}
}
