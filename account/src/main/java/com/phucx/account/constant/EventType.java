package com.phucx.account.constant;

public enum EventType {
    // customer
    GetCustomerByUserID,
    GetCustomerByID,
    GetCustomersByIDs,
    // employee
    GetEmployeeByID,
    // shipper
    GetShipperByID,
    // user
    GetUserByCustomerID,
    GetUserByEmployeeID,
    // order
    GetOrderInvoiceByIdAndCustomerID,
    GetOrdersByCustomerID,
    GetOrderByEmployeeID,
    GetOrdersByEmployeeID,

    // return
    // customer
    ReturnCustomerByUserID,
    ReturnCustomerByID,
    ReturnCustomersByIDs,
    // employee
    ReturnEmployeeByID,
    // usre
    ReturnUserByCustomerID,
    ReturnUserByEmployeeID,
    // shipper
    ReturnShipperByID,
    // order
    ReturnOrderInvoiceByIdAndCustomerID,
    ReturnOrdersByCustomerID,
    ReturnOrderByEmployeeID,
    ReturnOrdersByEmployeeID,
}
