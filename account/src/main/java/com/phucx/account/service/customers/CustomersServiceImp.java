package com.phucx.account.service.customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.account.model.Customers;
import com.phucx.account.repository.CustomersRepository;

@Service
public class CustomersServiceImp implements CustomersService {
    @Autowired
    private CustomersRepository customersRepository;
    @Override
    public Customers getCustomerInfo(String customerID) {
        var opCustomer = customersRepository.findById(customerID);
        if(opCustomer.isPresent()) return opCustomer.get();
        return null;
    }

    
}
