package com.phucx.shop.constant;

public enum EventType {
    // customer
    GetCustomerByUserID,
    GetCustomersByIDs,
    // discount
    ValidateDiscounts,
    GetDiscountByID,
    GetDiscountsByIDs,
    // product
    GetProductByID,
    GetProductsByIDs,
    
    // return
    // product
    ReturnProductByID,
    ReturnProductsByIDs,
    // customer
    ReturnCustomerByUserID,
    ReturnCustomersByIDs,
    // discount
    ReturnDiscountByID,
    ReturnDiscountsByIDs,
    ReturnValidateDiscounts;
}
