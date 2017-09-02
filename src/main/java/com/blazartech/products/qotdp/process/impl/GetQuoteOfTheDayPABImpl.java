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
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author scott
 */
@Service
public class GetQuoteOfTheDayPABImpl implements GetQuoteOfTheDayPAB {
    
    private static final Logger logger = Logger.getLogger(GetQuoteOfTheDayPABImpl.class);

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
    @Transactional("txManager")
    public QuoteOfTheDay getQuoteOfTheDay() {
        return getQuoteOfTheDay(getCurrentDate());
    }

    @Override
    @Transactional("txManager")
    public QuoteOfTheDay getQuoteOfTheDay(Date runDate) {

        // do we already have a quote for today?
        QuoteOfTheDay qotd = dal.getQuoteOfTheDay(runDate);
        if (qotd == null) {
            logger.info("don't have a quote of the day for " + runDate);
            
            /* Don't have one so figure out how.  Figure out how many total quotes we have.  Then
             * generate a random number between 1 and the total number of quotes, and find that one. */
            Collection<Quote> fullQuoteCollection = dal.getUsableQuotes();
            List<Quote> fullQuotes = new ArrayList<>();
            fullQuotes.addAll(fullQuoteCollection);
            int quoteCount = fullQuotes.size();
            boolean found = false;
            Quote quote = null;
            while (!found) {
                Random rgen = new Random();
                float rnum = rgen.nextFloat();
                int n = ((int) (rnum * quoteCount));
                quote = fullQuotes.get(n);

                // check if we have used this quote recently.
                if (dal.getQuoteOfTheDayInDateRange(quote.getNumber(), getDateOneMonthBefore(runDate), runDate).isEmpty()) {
                    found = true;
                }
            }

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
        Quote q = dal.getQuote(qotd.getQuoteNumber());
        QuoteSourceCode sourceCode = dal.getQuoteSourceCode(q.getSourceCode());
        AggregatedQuoteOfTheDay aggQuote = new AggregatedQuoteOfTheDay();
        aggQuote.setQuote(q);
        aggQuote.setQuoteOfTheDay(qotd);
        aggQuote.setSourceCode(sourceCode);
        return aggQuote;
    }

}
