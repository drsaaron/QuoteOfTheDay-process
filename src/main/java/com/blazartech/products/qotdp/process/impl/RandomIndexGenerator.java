/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.process.impl;

/**
 *
 * @author scott
 */
public interface RandomIndexGenerator {
    
    /**
     * generate a random index in the range (0, range].
     * @param range
     * @return 
     */
    public int randomIndex(int range);
}
