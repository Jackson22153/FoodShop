package com.phucx.payment.service.payment.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.phucx.model.PaymentDTO;
import com.phucx.payment.constant.PaymentConstant;
import com.phucx.payment.constant.PaymentMethodConstant;
import com.phucx.payment.model.PaymentResponse;
import com.phucx.payment.service.payment.PaymentProcessorService;
import com.phucx.payment.service.paymentHandler.CODHandlerService;
import com.phucx.payment.service.paymentHandler.PaypalHandlerService;
import com.phucx.payment.service.paymentHandler.ZaloPayHandlerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentProcessorImpService implements PaymentProcessorService {
    @Autowired
    private PaypalHandlerService paypalService;
    @Autowired
    private CODHandlerService codHandlerService;
    @Autowired
    private ZaloPayHandlerService zaloPayHandlerService;

    @Override
    public PaymentResponse createPayment(PaymentDTO payment) throws PayPalRESTException {
        log.info("createPayment(payment={})", payment);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStatus(true);
        PaymentMethodConstant method = PaymentMethodConstant.fromString(payment.getMethod());
        // payment handlers
        switch (method) {
            case COD:
                Boolean status = codHandler(payment);
                paymentResponse.setStatus(status);
                break;

            case PAYPAL:
                String redirectUrl = paypalHandler(payment);
                paymentResponse.setRedirect_url(redirectUrl);
                break;

            case ZALOPAY:
                String orderUrl = this.zalopayHandler(payment);
                paymentResponse.setRedirect_url(orderUrl);
                break;

            default:
                log.warn("There is no {} payment method avaiable", payment.getMethod());
                paymentResponse.setStatus(false);
                paymentResponse.setMessage("There is no " + payment.getMethod() + " method avaiable!");
                break; 
        }
        return paymentResponse;
    }
    // paypal method handler
    private String paypalHandler(PaymentDTO payment) throws PayPalRESTException{
        log.info("paypalHandler(payment={})", payment);
        String baseUrl = payment.getBaseUrl();
        Payment paypalPayment = paypalService.createPayment(payment, 
            baseUrl + PaymentConstant.PAYPAL_CANCEL_URL, 
            baseUrl + PaymentConstant.PAYPAL_SUCCESSFUL_URL);
        for(var link: paypalPayment.getLinks()){
            if(link.getRel().equals("approval_url")){
                return link.getHref();
            }
        }
        return null;
    }
    // cod method handler
    private Boolean codHandler(PaymentDTO payment){
        log.info("codHandler(payment={})", payment);
        var codPayment = codHandlerService.createPayment(payment);
        if(codPayment!=null && codPayment.getPaymentID()!=null){
            return true;
        }
        return false;
    }

    // zalopay method handler
    private String zalopayHandler(PaymentDTO payment){
        log.info("zalopayHandler(payment={})", payment);
        String payUrl = zaloPayHandlerService.createPayment(payment);
        return payUrl;
    }
}
