package com.phucx.account.constant;

public enum DiscountType {
    Code("code"),
    Percentage_based("percentage_based");

    private String value;
    DiscountType(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }
}
