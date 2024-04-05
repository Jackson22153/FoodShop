package com.phucx.account.service.customers;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.phucx.account.model.Customers;
import com.phucx.account.model.UserOrderProducts;

@PreAuthorize("hasRole('ROLE_CUSTOMER')")
public interface CustomersService {
    public Customers getCustomerDetailByID(String customerID);
    public Customers getCustomerDetail(String username);
    public boolean createCustomer(Customers customer);
    public boolean updateCustomerInfo(Customers customer);
    public Page<Customers> findAllCustomers(int pageNumber, int pageSize);
    public boolean placeOrder(UserOrderProducts userOrderProducts);
}
