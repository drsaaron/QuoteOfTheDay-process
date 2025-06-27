/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.process.impl;

import com.blazartech.products.services.date.DateServices;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author scott
 */
@Component
public class PriorDateDeterminationImpl implements PriorDateDetermination {

    @Autowired
    private DateServices dateServices;
    
    @Override
    public LocalDate getPriorDate(LocalDate d, int calendarType, int interval) {
        switch (calendarType) {
            case Calendar.MONTH -> {
                return dateServices.getPriorMonth(d, interval);
            }
            case Calendar.DATE -> {
                return dateServices.getPriorLocalDate(d, interval);
            }
            default -> throw new IllegalArgumentException("unsupported calendar type");
        }
    }
    
}
