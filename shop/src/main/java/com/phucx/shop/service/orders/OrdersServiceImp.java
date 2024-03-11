package com.phucx.shop.service.orders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.Orders;
import com.phucx.shop.repository.OrdersRepository;

@Service
public class OrdersServiceImp implements OrdersService{
    @Autowired
    private OrdersRepository ordersRepository;
    @Override
    public List<Orders> getOrders() {
        return ordersRepository.findAll();
    }

    @Override
    public Page<Orders> getOrders(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return ordersRepository.findAll(page);
    }

    @Override
    public Orders getOrder(int orderID) {
        var order = ordersRepository.findById(orderID);
        if(order.isPresent()) return order.get();
        return new Orders();
    }
    
}
