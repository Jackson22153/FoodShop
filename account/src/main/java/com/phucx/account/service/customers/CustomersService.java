package com.phucx.account.service.customers;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import com.phucx.account.model.Customers;

@PreAuthorize("hasRole('ROLE_CUSTOMER')")
public interface CustomersService {
    public Customers getCustomerDetail(String customerID);
    public boolean createCustomer(Customers customer);
    public boolean updateCustomerInfo(Customers customer);
    public Page<Customers> findAllCustomers(int pageNumber, int pageSize);
}
