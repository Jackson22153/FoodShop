package com.phucx.shop.service.bigdecimal;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

@Service
public class BigDecimalServiceImp implements BigDecimalService{

    @Override
    public BigDecimal formatter(BigDecimal value) {
        return value.setScale(4, RoundingMode.CEILING);
    }
    
}
