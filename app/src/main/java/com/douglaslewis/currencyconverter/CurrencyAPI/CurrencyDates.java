
package com.douglaslewis.currencyconverter.CurrencyAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class CurrencyDates {

    @SerializedName("currency_date")
    @Expose
    private List<CurrencyDateRate> currencyDate = new ArrayList<CurrencyDateRate>();

    /**
     * 
     * @return
     *     The currencyDate
     */
    public List<CurrencyDateRate> getCurrencyDateRate() {
        return currencyDate;
    }

    /**
     * 
     * @param currencyDate
     *     The currency_date
     */
    public void setCurrencyDateRate(List<CurrencyDateRate> currencyDate) {
        this.currencyDate = currencyDate;
    }

}
