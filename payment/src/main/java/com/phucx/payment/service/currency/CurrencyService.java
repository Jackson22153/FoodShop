package com.phucx.payment.service.currency;

public interface CurrencyService {
    public Double getExchangeRate(String currency);
    public String exchangeRate(Double amount, String currency);
}
