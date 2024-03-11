package com.phucx.shop.service.customers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.Customers;
import com.phucx.shop.repository.CustomersRepository;

@Service
public class CustomersServiceImp implements CustomersService{
    @Autowired
    private CustomersRepository customerRepository;
    @Override
    public List<Customers> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Page<Customers> getCustomers(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return customerRepository.findAll(page);
    }

    @Override
    public Customers getCustomer(String customerID) {
        @SuppressWarnings("null")
        var customer = customerRepository.findById(customerID);
        if(customer.isPresent()) return customer.get();
        return new Customers();
    }
    
}
