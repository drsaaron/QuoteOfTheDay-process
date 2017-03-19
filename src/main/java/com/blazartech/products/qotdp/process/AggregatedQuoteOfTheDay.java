/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.process;

import com.blazartech.products.qotdp.data.Quote;
import com.blazartech.products.qotdp.data.QuoteOfTheDay;
import com.blazartech.products.qotdp.data.QuoteSourceCode;
import java.io.Serializable;

/**
 *
 * @author scott
 */
public class AggregatedQuoteOfTheDay implements Serializable {

    private QuoteOfTheDay quoteOfTheDay;

    /**
     * Get the value of quoteOfTheDay
     *
     * @return the value of quoteOfTheDay
     */
    public QuoteOfTheDay getQuoteOfTheDay() {
        return quoteOfTheDay;
    }

    /**
     * Set the value of quoteOfTheDay
     *
     * @param quoteOfTheDay new value of quoteOfTheDay
     */
    public void setQuoteOfTheDay(QuoteOfTheDay quoteOfTheDay) {
        this.quoteOfTheDay = quoteOfTheDay;
    }

    private Quote quote;

    /**
     * Get the value of quote
     *
     * @return the value of quote
     */
    public Quote getQuote() {
        return quote;
    }

    /**
     * Set the value of quote
     *
     * @param quote new value of quote
     */
    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    private QuoteSourceCode sourceCode;

    /**
     * Get the value of sourceCode
     *
     * @return the value of sourceCode
     */
    public QuoteSourceCode getSourceCode() {
        return sourceCode;
    }

    /**
     * Set the value of sourceCode
     *
     * @param sourceCode new value of sourceCode
     */
    public void setSourceCode(QuoteSourceCode sourceCode) {
        this.sourceCode = sourceCode;
    }

}

