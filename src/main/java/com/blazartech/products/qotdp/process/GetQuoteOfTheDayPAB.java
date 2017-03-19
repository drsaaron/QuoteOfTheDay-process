/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.process;

import com.blazartech.products.qotdp.data.QuoteOfTheDay;
import java.util.Date;

/**
 *
 * @author scott
 */
public interface GetQuoteOfTheDayPAB {
    
        /**
     * get quote of the day for the current date.
     * @return
     */
    public QuoteOfTheDay getQuoteOfTheDay();

    /**
     * get quote of the day for an arbitrary date
     * @param runDate effective date
     * @return
     */
    public QuoteOfTheDay getQuoteOfTheDay(Date runDate);

    /**
     * get all the quote information for a quote of the day.
     * @return the quote of the day data
     */
    public AggregatedQuoteOfTheDay getAggregatedQuoteOfTheDay();

    /**
     * get all the quote information for a quote of the day.
     * @param runDate effective date
     * @return the quote of the day data
     */
    public AggregatedQuoteOfTheDay getAggregatedQuoteOfTheDay(Date runDate);
}
