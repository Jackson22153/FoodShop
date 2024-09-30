package com.phucx.payment.constant;

public enum EventType {
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
}
