package com.phucx.order.constant;

public enum EventType {
    NotFoundException,
    // notification
    SendOrderNotificationToUser,
    // udpate order status
    MarkOrderAsConfirmed,
    ReturnStatusMarkOrderAsConfirmed,
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
    UpdateProductsUnitsInStock,
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
    ReturnUpdateProductsUnitsInStock,
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
