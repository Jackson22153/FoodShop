package com.phucx.shop.constant;

public enum EventType {
    // validate products
    ValidateProducts,
    ReturnValidateProducts,

    // remove products in cart
    RemoveProductsInCart,
    ReturnRemoveProductsInCart,

    NotFoundException,
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
