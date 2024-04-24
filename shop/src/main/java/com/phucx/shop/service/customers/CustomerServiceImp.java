package com.phucx.shop.service.customers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.Customer;
import com.phucx.shop.repository.CustomerRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class CustomerServiceImp implements CustomerService{
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Page<Customer> getCustomers(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return customerRepository.findAll(page);
    }

    @Override
    public Customer getCustomer(String customerID) {
        Customer customer = customerRepository.findById(customerID)
            .orElseThrow(()-> new NotFoundException("Customer " + customerID + " does not found"));
        return customer;
    }

    @Override
    public Customer getCustomerByUserID(String userID) {
        Customer customer = customerRepository.findByUserID(userID)
            .orElseThrow(()-> new NotFoundException("Customer does not found"));
        return customer;
    }
    
}
