/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.process.impl;

import com.blazartech.products.qotdp.data.Quote;
import com.blazartech.products.qotdp.data.QuoteOfTheDay;
import com.blazartech.products.qotdp.data.QuoteSourceCode;
import com.blazartech.products.qotdp.data.access.QuoteOfTheDayDAL;
import com.blazartech.products.qotdp.process.AggregatedQuoteOfTheDay;
import com.blazartech.products.qotdp.process.GetQuoteOfTheDayPAB;
import com.blazartech.products.services.date.DateServices;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author scott
 */
@Service
public class GetQuoteOfTheDayPABImpl implements GetQuoteOfTheDayPAB {

    private static final Logger logger = LoggerFactory.getLogger(GetQuoteOfTheDayPABImpl.class);

    @Autowired
    private QuoteOfTheDayDAL dal;

    @Autowired
    private DateServices dateServices;

    /**
     * Get the value of dal
     *
     * @return the value of dal
     */
    public QuoteOfTheDayDAL getDal() {
        return dal;
    }

    /**
     * Set the value of dal
     *
     * @param dal new value of dal
     */
    public void setDal(QuoteOfTheDayDAL dal) {
        this.dal = dal;
    }

    private Date getCurrentDate() {
        return dateServices.getCurrentDate();
    }

    /* Get a date 1 month before a specified date. */
    private Date getDateOneMonthBefore(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MONTH, -1);
        return new Date(c.getTime().getTime());
    }

    @Override
    public QuoteOfTheDay getQuoteOfTheDay() {
        return getQuoteOfTheDay(getCurrentDate());
    }

    public List<Quote> removeRecentQuotes(Collection<Quote> allQuotes, Collection<QuoteOfTheDay> recentQuotes) {
        List<Quote> availableQuotes = new ArrayList<>(allQuotes);
        recentQuotes.forEach(used -> availableQuotes.removeIf(q -> q.getNumber() == used.getQuoteNumber()));
        return availableQuotes;
    }

    @Override
    @Transactional("txManager")
    public QuoteOfTheDay getQuoteOfTheDay(Date runDate) {

        // do we already have a quote for today?
        QuoteOfTheDay qotd = dal.getQuoteOfTheDay(runDate);
        if (qotd == null) {
            logger.info("don't have a quote of the day for " + runDate);

            // get all quotes
            Collection<Quote> fullQuoteCollection = dal.getUsableQuotes();
            
            // get the quotes of the day from the last month.
            Collection<QuoteOfTheDay> quotesOfTheDay = dal.getQuoteOfTheDayInDateRange(getDateOneMonthBefore(runDate), runDate);

            // remove the quotes from availableQuotes that have been recently used
            List<Quote> availableQuotes = removeRecentQuotes(fullQuoteCollection, quotesOfTheDay);        
            
            // shuffle the result
            Collections.shuffle(availableQuotes);
            
            // return the first one
            Quote quote = availableQuotes.get(0);
                    
            // store this quote.
            qotd = new QuoteOfTheDay();
            qotd.setRunDate(runDate);
            qotd.setQuoteNumber(quote.getNumber());
            dal.addQuoteOfTheDay(qotd);
        }

        // return it.
        return qotd;
    }

    @Override
    public AggregatedQuoteOfTheDay getAggregatedQuoteOfTheDay() {
        return getAggregatedQuoteOfTheDay(getCurrentDate());
    }

    @Override
    public AggregatedQuoteOfTheDay getAggregatedQuoteOfTheDay(Date runDate) {
        QuoteOfTheDay qotd = getQuoteOfTheDay(runDate);
        return getAggregatedQuoteOfTheDay(qotd);
    }

    @Override
    public AggregatedQuoteOfTheDay getAggregatedQuoteOfTheDay(QuoteOfTheDay qotd) {
        Quote q = dal.getQuote(qotd.getQuoteNumber());
        QuoteSourceCode sourceCode = dal.getQuoteSourceCode(q.getSourceCode());
        AggregatedQuoteOfTheDay aggQuote = new AggregatedQuoteOfTheDay();
        aggQuote.setQuote(q);
        aggQuote.setQuoteOfTheDay(qotd);
        aggQuote.setSourceCode(sourceCode);
        return aggQuote;
    }
}
