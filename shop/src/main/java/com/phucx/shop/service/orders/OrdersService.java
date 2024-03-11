package com.phucx.shop.service.orders;

import java.util.List;

import org.springframework.data.domain.Page;
import com.phucx.shop.model.Orders;

public interface OrdersService {
    public Orders getOrder(int orderID);
    public List<Orders> getOrders();
    public Page<Orders> getOrders(int pageNumber, int pageSize);
}
