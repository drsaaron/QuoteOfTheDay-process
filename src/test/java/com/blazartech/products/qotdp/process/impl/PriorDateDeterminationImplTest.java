/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.process.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author scott
 */
@ExtendWith(SpringExtension.class)
public class PriorDateDeterminationImplTest {
    
    private static final Logger logger = LoggerFactory.getLogger(PriorDateDeterminationImplTest.class);
    
    @TestConfiguration
    static class PriorDateDeterminationImplTestConfiguration {
        
        @Bean
        public PriorDateDeterminationImpl instance() {
            return new PriorDateDeterminationImpl();
        }
    }
    
    @Autowired
    private PriorDateDeterminationImpl instance;
    
    public PriorDateDeterminationImplTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    private Date parseDate(String d) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(d);
    }
    
    /**
     * Test of getPriorDate method, of class PriorDateDeterminationImpl.
     */
    @Test
    public void testGetPriorDate_1Month() throws ParseException {
        logger.info("getPriorDate_1Month");

        Date d = parseDate("2021-04-30");
        Date expResult = parseDate("2021-05-30");
        
        Date result = instance.getPriorDate(d, Calendar.MONTH, 1);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetPriorDate_30Days() throws ParseException {
        logger.info("getPriorDate_30Days");

        Date d = parseDate("2021-04-30");
        Date expResult = parseDate("2021-03-31");
        
        Date result = instance.getPriorDate(d, Calendar.DATE, -30);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetPriorDate_60Days() throws ParseException {
        logger.info("getPriorDate_60Days");

        Date d = parseDate("2021-09-1");
        Date expResult = parseDate("2021-07-01");
        
        Date result = instance.getPriorDate(d, Calendar.DATE, -62);
        assertEquals(expResult, result);
    }
}
