package com.phucx.payment.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.base.rest.PayPalRESTException;
import com.phucx.payment.constant.PaymentConstant;
import com.phucx.payment.model.PaymentDTO;
import com.phucx.payment.model.PaymentMethod;
import com.phucx.payment.model.PaymentResponse;
import com.phucx.payment.service.currency.CurrencyService;
import com.phucx.payment.service.payment.PaymentProcessorService;
import com.phucx.payment.service.paymentHandler.ZaloPayHandlerService;
import com.phucx.payment.service.paymentMethod.PaymentMethodService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PaymentController {
    @Autowired
    private PaymentProcessorService paymentProcessorService;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private ZaloPayHandlerService zaloPayHandlerService;

    @GetMapping("/exchangeRate")
    public ResponseEntity<String> home(@RequestParam(name = "amount") Double amount){
        String rate = currencyService.exchangeRate(amount, PaymentConstant.CURRENCY_VND);
        return ResponseEntity.ok().body(rate);
    }

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> payment(@RequestBody PaymentDTO payment, 
        HttpServletRequest request, HttpServletResponse response
    ) throws PayPalRESTException, IOException{
        String baseUrl = getBaseUrl(request);
        payment.setBaseUrl(baseUrl);
        zaloPayHandlerService.createPayment(payment);
        // PaymentResponse paymentResponse = paymentProcessorService.createPayment(payment);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/methods")
    public ResponseEntity<List<PaymentMethod>> getPaymentMethods(){
        List<PaymentMethod> methods = paymentMethodService.getPaymmentMethods();
        return ResponseEntity.ok().body(methods);
    }

    // get base url
    private String getBaseUrl(HttpServletRequest request){
        String uri = request.getRequestURI().toString();
        String url = request.getRequestURL().toString();
        String baseurl = url.substring(0, url.length()-uri.length());
        return baseurl;
    }
}
