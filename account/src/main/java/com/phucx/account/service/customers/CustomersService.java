package com.phucx.account.service.customers;

import org.springframework.data.domain.Page;
import com.phucx.account.model.Customers;

// @PreAuthorize("hasRole('ROLE_CUSTOMER')")
public interface CustomersService {
    public Customers getCustomerDetail(String username);
    public boolean createCustomer(Customers customer);
    public boolean updateCustomerInfo(Customers customer);
    public Page<Customers> findAllCustomers(int pageNumber, int pageSize);
}
