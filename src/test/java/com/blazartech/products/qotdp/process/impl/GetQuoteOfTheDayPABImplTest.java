/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.process.impl;

import com.blazartech.products.qotdp.data.Quote;
import com.blazartech.products.qotdp.data.QuoteOfTheDay;
import com.blazartech.products.qotdp.data.access.QuoteOfTheDayDAL;
import com.blazartech.products.services.date.DateServices;
import com.blazartech.products.services.date.impl.DateServicesImpl;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author scott
 */
@ExtendWith(SpringExtension.class)
public class GetQuoteOfTheDayPABImplTest {

    private static final Logger logger = LoggerFactory.getLogger(GetQuoteOfTheDayPABImplTest.class);

    @TestConfiguration
    static class GetQuoteOfTheDayPABImplTestConfiguration {

        @Bean
        public GetQuoteOfTheDayPABImpl instance() {
            return new GetQuoteOfTheDayPABImpl();
        }

        @Bean
        public DateServices dateServices() {
            return new DateServicesImpl();
        }
    }

    @Autowired
    private GetQuoteOfTheDayPABImpl instance;

    @MockBean
    private QuoteOfTheDayDAL dal;

    public GetQuoteOfTheDayPABImplTest() {
    }

    private static List<Quote> AVAILABLE_QUOTES;
    private static List<QuoteOfTheDay> USED_QUOTES;

    @BeforeAll
    public static void setUpClass() {
        Quote q1 = new Quote();
        q1.setNumber(1);
        q1.setSourceCode(1);
        q1.setText("my quote");
        q1.setUsable(true);

        Quote q2 = new Quote();
        q2.setNumber(2);
        q2.setSourceCode(1);
        q2.setText("my quote2");
        q2.setUsable(true);

        Quote q3 = new Quote();
        q3.setNumber(3);
        q3.setSourceCode(1);
        q3.setText("my quote2");
        q3.setUsable(true);

        AVAILABLE_QUOTES = List.of(q1, q2, q3);

        QuoteOfTheDay qotd1 = new QuoteOfTheDay();
        qotd1.setNumber(1);
        qotd1.setQuoteNumber(1);
        qotd1.setRunDate(new Date());

        QuoteOfTheDay qotd2 = new QuoteOfTheDay();
        qotd2.setNumber(2);
        qotd2.setQuoteNumber(3);
        qotd2.setRunDate(new Date());

        USED_QUOTES = List.of(qotd1, qotd2);
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {

        Mockito.when(dal.getAllQuotes()).thenReturn(AVAILABLE_QUOTES);
        Mockito.when(dal.getUsableQuotes()).thenReturn(AVAILABLE_QUOTES);
        Mockito.when(dal.getQuoteOfTheDay(Mockito.any(Date.class))).thenReturn(null);
        Mockito.when(dal.getQuoteOfTheDayInDateRange(Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(USED_QUOTES);
        doNothing().when(dal).addQuoteOfTheDay(Mockito.any(QuoteOfTheDay.class));
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getDal method, of class GetQuoteOfTheDayPABImpl.
     */
    @Test
    public void testGetDal() {
        logger.info("getDal");

        QuoteOfTheDayDAL expResult = dal;
        QuoteOfTheDayDAL result = instance.getDal();
        assertEquals(expResult, result);
    }

    /**
     * Test of removeRecentQuotes method, of class GetQuoteOfTheDayPABImpl.
     */
    @Test
    public void testRemoveRecentQuotes() {
        logger.info("removeRecentQuotes");

        Collection<Quote> allQuotes = AVAILABLE_QUOTES;
        Collection<QuoteOfTheDay> recentQuotes = USED_QUOTES;

        List<Quote> result = instance.removeRecentQuotes(allQuotes, recentQuotes);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetQuoteOfTheDay() {
        logger.info("getQuoteOfTheDay");

        QuoteOfTheDay qotd = instance.getQuoteOfTheDay(new Date());

        assertNotNull(qotd);
        assertEquals(2, qotd.getQuoteNumber());
    }
}
