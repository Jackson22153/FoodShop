package com.phucx.account.constant;

public enum EventType {
    GetCustomerByUserID,
    GetCustomerByID,
    GetCustomersByID,

    GetEmployeeByID,

    GetShipperByID,

    GetUserByCustomerID,
    GetUserByEmployeeID,

    GetOrderInvoiceByIdAndCustomerID,
    GetOrdersByCustomerID,
    GetOrderByEmployeeID,
    GetOrdersByEmployeeID,

    ReturnCustomerByUserID,
    ReturnCustomerByID,
    ReturnCustomersByID,

    ReturnEmployeeByID,

    ReturnUserByCustomerID,
    ReturnUserByEmployeeID,

    ReturnShipperByID,

    ReturnOrder,
}
