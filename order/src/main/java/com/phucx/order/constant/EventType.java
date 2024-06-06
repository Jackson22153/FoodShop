package com.phucx.order.constant;

public enum EventType {
    // customer request
    GetCustomerByID,
    GetCustomersByIDs,
    GetCustomerByUserID,
    // employee request
    GetEmployeeByID,
    GetEmployeeByUserID,
    // user request
    GetUserByCustomerID,
    GetUserByEmployeeID,
    // shipper request
    GetShipperByID,
    // discount request
    GetDiscountByID,
    GetDiscountsByIDs,
    ValidateDiscounts,
    // product request
    GetProductByID,
    GetProductsByIDs,
    UpdateProductInStocks,
    // order request
    GetOrderInvoiceByIdAndCustomerID,
    GetOrdersByCustomerID,
    GetOrderByEmployeeID,
    GetOrdersByEmployeeID,

    // discount response 
    ReturnDiscountByID,
    ReturnDiscountsByIDs,
    ReturnValidateDiscounts,
    // order response 
    ReturnOrderInvoiceByIdAndCustomerID,
    ReturnOrdersByCustomerID,
    ReturnOrderByEmployeeID,
    ReturnOrdersByEmployeeID,
    // product response
    ReturnProductByID,
    ReturnProductsByIDs,
    ReturnUpdateProductInStocks,
    // shipper response
    ReturnShipperByID,
    // customer response
    ReturnCustomerByID,
    ReturnCustomersByIDs,
    ReturnCustomerByUserID,
    // employee response
    ReturnEmployeeByID,
    ReturnEmployeeByUserID,
    // user response
    ReturnUserByCustomerID,
    ReturnUserByEmployeeID,

}
