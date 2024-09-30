package com.phucx.payment.service.currency;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.phucx.payment.constant.PaymentConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CurrencyServiceImp implements CurrencyService {

    private final RestTemplate restTemplate;

    public CurrencyServiceImp(){
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Double getExchangeRate(String currency) {
        log.info("getExchangeRate()");
        ResponseEntity<Map> response = restTemplate. getForEntity(PaymentConstant.EXCHANGE_RATE_URL, Map.class);
        Map<String, Object> rates = (Map<String, Object>) response.getBody().get("rates");
        return (Double) rates.get(currency);
    }

    @Override
    public String exchangeRate(Double amount, String currency) {
        log.info("exchangeRate(amount={}, currency={})", amount, currency);

        Double rate = this.getExchangeRate(PaymentConstant.CURRENCY_VND);
        Double value = rate*amount;
        return String.valueOf(Math.round(value));
    }
    
}
