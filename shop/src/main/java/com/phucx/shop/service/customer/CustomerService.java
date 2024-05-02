package com.phucx.shop.service.customer;

import java.util.List;

import org.springframework.data.domain.Page;
import com.phucx.shop.model.Customer;

public interface CustomerService {
    public Customer getCustomer(String customerID);
    public List<Customer> getCustomers();
    public Page<Customer> getCustomers(int pageNumber, int pageSize);
    public Customer getCustomerByUserID(String userID);
}
