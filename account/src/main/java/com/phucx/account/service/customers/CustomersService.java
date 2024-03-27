package com.phucx.account.service.customers;

import org.springframework.security.access.prepost.PreAuthorize;
import com.phucx.account.model.Customers;

public interface CustomersService {
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public Customers getCustomerInfo(String customerID);
}
