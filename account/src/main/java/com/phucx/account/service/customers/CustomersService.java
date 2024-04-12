package com.phucx.account.service.customers;

import java.sql.SQLException;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.Customers;
import com.phucx.account.model.OrderWithProducts;

import jakarta.ws.rs.NotFoundException;

@PreAuthorize("hasRole('ROLE_CUSTOMER')")
public interface CustomersService {
    public Customers getCustomerDetailByID(String customerID);
    public Customers getCustomerDetail(String username);
    public boolean createCustomer(Customers customer);
    public boolean updateCustomerInfo(Customers customer);
    public Page<Customers> findAllCustomers(int pageNumber, int pageSize);
    public OrderWithProducts placeOrder(OrderWithProducts order) 
        throws InvalidDiscountException, NotFoundException, RuntimeException, SQLException,  InvalidOrderException ;
}
