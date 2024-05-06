package com.phucx.account.service.customer;

import java.sql.SQLException;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.phucx.account.constant.OrderStatus;
import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.CustomerDetailDTO;
import com.phucx.account.model.Customer;
import com.phucx.account.model.InvoiceDTO;
import com.phucx.account.model.Notification;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderWithProducts;

import jakarta.ws.rs.NotFoundException;

@PreAuthorize("hasRole('ROLE_CUSTOMER')")
public interface CustomerService {
    // get customer
    public Customer getCustomerByID(String customerID);
    public Customer getCustomerByUsername(String username);
    public CustomerDetailDTO getCustomerDetailByCustomerID(String customerID);
    public CustomerDetail getCustomerDetail(String username);
    public Page<CustomerAccount> getAllCustomers(int pageNumber, int pageSize);
    // create/ update customer
    public boolean createCustomer(Customer customer);
    public boolean updateCustomerInfo(CustomerDetail customer);
    // search customers
    public Page<CustomerAccount> searchCustomersByCustomerID(String customerID, int pageNumber, int pageSize);
    public Page<CustomerAccount> searchCustomersByContactName(String contactName, int pageNumber, int pageSize);
    public Page<CustomerAccount> searchCustomersByUsername(String username, int pageNumber, int pageSize);
    public Page<CustomerAccount> searchCustomersByEmail(String email, int pageNumber, int pageSize);
    // get customer's order
    public Page<OrderDetailsDTO> getOrders(int pageNumber, int pageSize, String customerID, OrderStatus orderStatus);
    public InvoiceDTO getInvoice(int orderID, String customerID) throws InvalidOrderException;

    // place an order by customer
    public OrderWithProducts placeOrder(OrderWithProducts order) 
        throws InvalidDiscountException, InvalidOrderException, NotFoundException, SQLException, RuntimeException;
    public Notification receiveOrder(OrderWithProducts order);

    // notification
    Page<Notification> getNotifications(String userID, int pageNumber, int pageSize);
    Boolean turnOffNotification(String notificationID, String userID);
}
