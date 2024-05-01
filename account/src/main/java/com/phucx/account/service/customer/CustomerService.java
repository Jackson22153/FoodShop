package com.phucx.account.service.customer;

import java.sql.SQLException;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.phucx.account.constant.OrderStatus;
import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.Customer;
import com.phucx.account.model.InvoiceDTO;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderWithProducts;
import jakarta.ws.rs.NotFoundException;

@PreAuthorize("hasRole('ROLE_CUSTOMER')")
public interface CustomerService {
    public Customer getCustomerDetailByID(String customerID);
    public Customer getCustomerByUsername(String username);
    public CustomerDetail getCustomerDetail(String username);
    public boolean createCustomer(Customer customer);
    public boolean updateCustomerInfo(CustomerDetail customer);
    public Page<CustomerAccount> getAllCustomers(int pageNumber, int pageSize);

    public Page<CustomerAccount> searchCustomersByCustomerID(String customerID, int pageNumber, int pageSize);
    public Page<CustomerAccount> searchCustomersByContactName(String contactName, int pageNumber, int pageSize);
    public Page<CustomerAccount> searchCustomersByUsername(String username, int pageNumber, int pageSize);
    public Page<CustomerAccount> searchCustomersByEmail(String email, int pageNumber, int pageSize);

    public Page<OrderDetailsDTO> getOrders(int pageNumber, int pageSize, String customerID, OrderStatus orderStatus);
    public InvoiceDTO getInvoice(int orderID, String customerID) throws InvalidOrderException;
    
    public OrderWithProducts placeOrder(OrderWithProducts order) 
        throws InvalidDiscountException, NotFoundException, RuntimeException, SQLException,  InvalidOrderException;
}
