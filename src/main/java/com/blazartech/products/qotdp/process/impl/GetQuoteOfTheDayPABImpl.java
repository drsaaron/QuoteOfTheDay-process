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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author scott
 */
@Service
public class GetQuoteOfTheDayPABImpl implements GetQuoteOfTheDayPAB, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(GetQuoteOfTheDayPABImpl.class);

    @Autowired
    private QuoteOfTheDayDAL dal;

    @Autowired
    private DateServices dateServices;
    
    @Autowired
    private RandomIndexGenerator indexGenerator;

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

    private LocalDate getCurrentDate() {
        return dateServices.getCurrentLocalDate();
    }

    @Autowired
    private PriorDateDetermination priorDate;

    @Override
    public QuoteOfTheDay getQuoteOfTheDay() {
        return getQuoteOfTheDay(getCurrentDate());
    }

    public List<Quote> removeRecentQuotes(Collection<Quote> allQuotes, Collection<QuoteOfTheDay> recentQuotes) {
        List<Quote> availableQuotes = new ArrayList<>(allQuotes);
        recentQuotes.forEach(used -> availableQuotes.removeIf(q -> q.getNumber() == used.getQuoteNumber()));
        return availableQuotes;
    }

    @Value("${quoteOfTheDay.reuseWindowMonths:1}")
    private int reuseWindowMonths;
    
    @Override
    @Transactional("txManager")
    public QuoteOfTheDay getQuoteOfTheDay(LocalDate runDate) {

        // do we already have a quote for today?
        QuoteOfTheDay qotd = dal.getQuoteOfTheDay(runDate);
        if (qotd == null) {
            logger.info("don't have a quote of the day for " + runDate);

            // get all quotes
            Collection<Quote> fullQuoteCollection = dal.getUsableQuotes();
            
            // get the start date of the reuse window block
            LocalDate windowStart = priorDate.getPriorDate(runDate, Calendar.MONTH, reuseWindowMonths);
            logger.info("window start date = " + windowStart);
            
            // get the quotes of the day from the last month.
            Collection<QuoteOfTheDay> quotesOfTheDay = dal.getQuoteOfTheDayInDateRange(windowStart, runDate);

            // remove the quotes from availableQuotes that have been recently used
            List<Quote> availableQuotes = removeRecentQuotes(fullQuoteCollection, quotesOfTheDay);        
            
            // create a random index.
            int index = indexGenerator.randomIndex(availableQuotes.size());
            
            // return that quote
            Quote quote = availableQuotes.get(index);
                    
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
    public AggregatedQuoteOfTheDay getAggregatedQuoteOfTheDay(LocalDate runDate) {
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

    @Override
    public void afterPropertiesSet() throws Exception {
        // sanity check that the window interval is positive
        if (reuseWindowMonths <= 0) {
            throw new IllegalStateException("reuseWkindowMonths must be positive");
        }
    }
}
