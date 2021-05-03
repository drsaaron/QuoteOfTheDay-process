/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.process.impl;

import java.util.Date;

/**
 *
 * @author scott
 */
public interface PriorDateDetermination {
    
    /**
     * get a prior date, <em>e.g.</em> a date one month prior.
     * 
     * @param d
     * @return 
     */
    Date getPriorDate(Date d, int calendarType, int interval);
}
