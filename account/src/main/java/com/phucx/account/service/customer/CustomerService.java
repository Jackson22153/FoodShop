package com.phucx.account.service.customer;

import java.util.List;

import org.springframework.data.domain.Page;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.CustomerDetails;
import com.phucx.account.exception.CustomerNotFoundException;
import com.phucx.account.exception.InvalidUserException;
import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.Customer;

public interface CustomerService {
    // get customer
    public Customer getCustomerByID(String customerID) throws CustomerNotFoundException;
    public Customer getCustomerByUsername(String username) throws CustomerNotFoundException;
    public Customer getCustomerByUserID(String userID) throws CustomerNotFoundException;
    public List<Customer> getCustomersByIDs(List<String> customerIDs);
    public CustomerDetails getCustomerDetailByCustomerID(String customerID) throws CustomerNotFoundException, UserNotFoundException;
    public CustomerDetail getCustomerDetail(String username) throws CustomerNotFoundException, UserNotFoundException;
    public Page<CustomerAccount> getAllCustomers(int pageNumber, int pageSize);
    // create/ update customer
    public boolean addNewCustomer(CustomerAccount customer) throws InvalidUserException;
    public boolean updateCustomerInfo(CustomerDetail customer) throws CustomerNotFoundException;
    // search customers
    public Page<CustomerAccount> searchCustomersByCustomerID(String customerID, int pageNumber, int pageSize);
    public Page<CustomerAccount> searchCustomersByContactName(String contactName, int pageNumber, int pageSize);
    public Page<CustomerAccount> searchCustomersByUsername(String username, int pageNumber, int pageSize);
    public Page<CustomerAccount> searchCustomersByEmail(String email, int pageNumber, int pageSize);
}
