package com.phucx.order.service.order;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.Invoice;
import com.phucx.order.model.InvoiceDetails;
import com.phucx.order.model.Order;
import com.phucx.order.model.OrderDetailExtended;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderWithProducts;

public interface ConvertOrderService {
    public InvoiceDetails convertInvoiceDetails(List<Invoice> invoices) throws JsonProcessingException, NotFoundException;
    public List<OrderDetails> convertOrders(List<OrderDetailExtended> orders) throws JsonProcessingException, NotFoundException;
    public OrderDetails convertOrderDetail(List<OrderDetailExtended> orderDetailExtendeds) throws JsonProcessingException, NotFoundException;
    public OrderWithProducts convertOrderWithProducts(Order order) throws JsonProcessingException, NotFoundException;
}
