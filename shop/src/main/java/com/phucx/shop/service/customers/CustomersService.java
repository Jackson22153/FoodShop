package com.phucx.shop.service.customers;

import java.util.List;

import org.springframework.data.domain.Page;
import com.phucx.shop.model.Customers;

public interface CustomersService {
    public Customers getCustomer(String customerID);
    public List<Customers> getCustomers();
    public Page<Customers> getCustomers(int pageNumber, int pageSize);
}
