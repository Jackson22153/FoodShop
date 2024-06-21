package com.phucx.account.constant;

public enum EventType {

    NotFoundException,

    // notification
    SendCustomerNotificationToUser,
    SendEmployeeNotificationToUser,
    // customer
    GetCustomerByUserID,
    GetCustomerByID,
    GetCustomersByIDs,
    // employee
    GetEmployeeByID,
    GetEmployeeByUserID,
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
    ReturnEmployeeByUserID,
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
