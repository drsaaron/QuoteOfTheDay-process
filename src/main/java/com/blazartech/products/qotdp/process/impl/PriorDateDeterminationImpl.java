/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.process.impl;

import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 *
 * @author scott
 */
@Component
public class PriorDateDeterminationImpl implements PriorDateDetermination {

    @Override
    public Date getPriorDate(Date d, int calendarType, int interval) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(calendarType, interval);
        return new Date(c.getTime().getTime());
    }
    
}
