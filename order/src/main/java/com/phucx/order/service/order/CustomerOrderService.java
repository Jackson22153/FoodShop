package com.phucx.order.service.order;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.InvoiceDetails;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.PaymentResponse;

public interface CustomerOrderService {
    // place an order by customer
    public PaymentResponse placeOrder(OrderWithProducts order, String userID) 
        throws JsonProcessingException, InvalidDiscountException, InvalidOrderException, NotFoundException;
    // receive order
    public void receiveOrder(OrderWithProducts order) throws JsonProcessingException, NotFoundException;
    // get customer's orders
    public Page<OrderDetails> getOrders(int pageNumber, int pageSize, String userID, OrderStatus orderStatus) throws JsonProcessingException, NotFoundException;
    // get customer 's invoice
    public InvoiceDetails getInvoice(String orderID, String userID) throws JsonProcessingException, NotFoundException;
}