/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.qotdp.process.impl;

import java.util.Random;
import org.springframework.stereotype.Component;

/**
 *
 * @author scott
 */
@Component
public class RandomIndexGeneratorImpl implements RandomIndexGenerator {

    private final Random random = new Random();
    
    @Override
    public int randomIndex(int range) {
        float v = random.nextFloat();
        return (int) v * range;
    }
    
}
