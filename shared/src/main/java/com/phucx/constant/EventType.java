package com.phucx.constant;

public enum EventType {
    // validate products
    ValidateProducts,
    ReturnValidateProducts,

    ValidateAndProcessProducts,
    ReturnValidateAndProcessProducts,

    // notification
    SendOrderNotificationToUser,

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
    ReturnValidateDiscounts,

    GetShippingProduct,
    ReturnShippingProduct,

    CreatePayment,
    ReturnStatusCreatePayment,

    UpdatePaymentStatusAsSuccessfulByID,
    ReturnStatusUpdatePaymentStatusAsSuccessfulByID, 

    UpdatePaymentStatusAsCanceledByID,
    ReturnStatusUpdatePaymentStatusAsCanceledByID, 

    UpdatePaymentStatusAsSuccessfulByOrderID,
    ReturnStatusUpdatePaymentStatusAsSuccessfulByOrderID, 

    UpdatePaymentStatusAsCanceledByOrderID,
    ReturnStatusUpdatePaymentStatusAsCanceledByOrderID, 
    // update order status
    UpdateOrderStatusAsCanceled,
    ReturnUpdateOrderStatus,
    GetCustomerByID,


    GetOrderByOrderID,
    ReturnOrder,

    GetAdministratorProfile,
    ReturnEmployeeProfile,

    // udpate order status
    MarkOrderAsConfirmed,
    ReturnStatusMarkOrderAsConfirmed,

    CreateCustomerDetail,
    ReturnCreateCustomerDetail,

    CreateEmployeeDetail,
    ReturnCreateEmployeeDetail,

    InvalidUserException,

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
    ReturnCustomerByID,
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

    GetPaymentDetailsByOrderID,
    ReturnPaymentDetails,

    // update payment status
    UpdatePaymentByOrderIDAsSuccesful,
    UpdatePaymentByOrderIDAsCanceled,
    ReturnUpdatePaymentStatus
    
}
