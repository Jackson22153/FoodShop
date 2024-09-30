package com.phucx.order.constant;

public enum PaymentMethodConstant {
    PAYPAL("paypal"),
    COD("cod"),
    ZALOPAY("zalopay"),
    MOMO("momo");

    private String value;
    private PaymentMethodConstant(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public static PaymentMethodConstant fromString(String value) {
        for (PaymentMethodConstant paymentMethod : values()) {
            if (paymentMethod.getValue().equalsIgnoreCase(value)) {
                return paymentMethod;
            }
        }
        return null;
    }
}
