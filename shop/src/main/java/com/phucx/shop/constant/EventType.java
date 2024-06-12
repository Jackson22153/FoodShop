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
    UpdateProductsUnitsInStock,
    // return
    // product
    ReturnProductByID,
    ReturnProductsByIDs,
    ReturnUpdateProductsUnitsInStock,
    // customer
    ReturnCustomerByUserID,
    ReturnCustomersByIDs,
    // discount
    ReturnDiscountByID,
    ReturnDiscountsByIDs,
    ReturnValidateDiscounts;
}
