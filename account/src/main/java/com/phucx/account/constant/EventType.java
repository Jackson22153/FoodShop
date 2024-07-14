package com.phucx.account.constant;

public enum EventType {

    CreateCustomerDetail,
    ReturnCreateCustomerDetail,

    CreateEmployeeDetail,
    ReturnCreateEmployeeDetail,

    InvalidUserException,

    NotFoundException,
    // get users from authorization server by userID
    GetUsersByUserID,
    ReturnGetUsersByUserID,

    // add new user
    AddNewUser,
    ReturnAddNewuser,

    // notification
    SendCustomerNotificationToUser,
    SendEmployeeNotificationToUser,
    // customer
    GetCustomerByUserID,
    GetCustomerByID,
    GetCustomersByIDs,


    GetCustomersByUserIDs,
    ReturnCustomersByUserIDs,

    // employee
    GetEmployeeByID,
    GetEmployeeByUserID,


    GetEmployeesByUserIDs,
    ReturnEmployeesByUserIDs,

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
