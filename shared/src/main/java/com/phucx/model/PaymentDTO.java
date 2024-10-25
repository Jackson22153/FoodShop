package com.phucx.model;

public class PaymentDTO extends DataDTO{
    private String paymentID;
    private Double amount;
    private String orderID;
    private String method;
    private String customerID;
    private String baseUrl;

    public PaymentDTO() {
    }

    public PaymentDTO(Double amount, String orderID, String method, String customerID, String baseUrl) {
        this.amount = amount;
        this.orderID = orderID;
        this.method = method;
        this.customerID = customerID;
        this.baseUrl = baseUrl;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
