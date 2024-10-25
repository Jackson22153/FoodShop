package com.phucx.payment.service.currency;

import com.phucx.payment.constant.Currency;

public interface CurrencyService {
    public Double getExchangeRate(Currency currency);
    public String exchangeRateFromUSDToVND(Double amount);
    public String exchangeRateFromVNDToUSD(Long amount);
}
