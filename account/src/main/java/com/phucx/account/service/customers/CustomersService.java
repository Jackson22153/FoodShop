package com.phucx.account.service.customers;

import java.sql.SQLException;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.phucx.account.constant.OrderStatus;
import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.Customers;
import com.phucx.account.model.InvoiceDTO;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderWithProducts;
import jakarta.ws.rs.NotFoundException;

@PreAuthorize("hasRole('ROLE_CUSTOMER')")
public interface CustomersService {
    public Customers getCustomerDetailByID(String customerID);
    public Customers getCustomerByUsername(String username);
    public CustomerDetail getCustomerDetail(String username);
    public boolean createCustomer(Customers customer);
    public boolean updateCustomerInfo(CustomerDetail customer);
    public Page<Customers> getAllCustomers(int pageNumber, int pageSize);

    public Page<OrderDetailsDTO> getOrders(int pageNumber, int pageSize, String customerID, OrderStatus orderStatus);
    public InvoiceDTO getInvoice(int orderID, String customerID) throws InvalidOrderException;
    
    public OrderWithProducts placeOrder(OrderWithProducts order) 
        throws InvalidDiscountException, NotFoundException, RuntimeException, SQLException,  InvalidOrderException ;
}
