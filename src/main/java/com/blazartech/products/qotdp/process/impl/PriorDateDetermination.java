/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.process.impl;

import java.time.LocalDate;

/**
 *
 * @author scott
 */
public interface PriorDateDetermination {
    
    /**
     * get a prior date, <em>e.g.</em> a date one month prior.
     * 
     * @param d
     * @param calendarType
     * @param interval
     * @return 
     */
    LocalDate getPriorDate(LocalDate d, int calendarType, int interval);
}
