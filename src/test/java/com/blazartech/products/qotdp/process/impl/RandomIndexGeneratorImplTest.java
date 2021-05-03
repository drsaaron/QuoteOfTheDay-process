/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.process.impl;

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
public class RandomIndexGeneratorImplTest {
    
    private static final Logger logger = LoggerFactory.getLogger(RandomIndexGeneratorImplTest.class);
    
    @TestConfiguration
    static class RandomIndexGeneratorImplTestConfiguration {
    
        @Bean
        public RandomIndexGeneratorImpl instance() {
            return new RandomIndexGeneratorImpl();
        }
    }
    
    @Autowired
    private RandomIndexGeneratorImpl instance;
    
    public RandomIndexGeneratorImplTest() {
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

    private static final int TEST_RANGE = 10;
    private static final int TEST_COUNT = 1000;
    
    /**
     * Test of randomIndex method, of class RandomIndexGeneratorImpl.
     */
    @Test
    public void testRandomIndex() {
        logger.info("randomIndex");
        
        /* generate a large number of random numbers.  verify that we get at 
           least one of each possible value.  The count must be large enough
           that that's a reliable expectation.
        */
        int[] counts = new int[TEST_RANGE];
        int range = TEST_RANGE;
        for (int i = 0; i < TEST_COUNT; i++) {
            int result = instance.randomIndex(range);
            counts[result]++;
        }
        
        for (int i = 0; i < TEST_RANGE; i++) {
            assertTrue(counts[i] > 0);
        }
    }
}
