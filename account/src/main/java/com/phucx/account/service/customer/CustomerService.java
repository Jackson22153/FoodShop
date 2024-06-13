package com.phucx.account.service.customer;

import java.util.List;

import org.springframework.data.domain.Page;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.CustomerDetails;
import com.phucx.account.model.Customer;

public interface CustomerService {
    // get customer
    public Customer getCustomerByID(String customerID);
    public List<Customer> getCustomersByIDs(List<String> customerIDs);
    public Customer getCustomerByUsername(String username);
    public CustomerDetails getCustomerDetailByCustomerID(String customerID);
    public CustomerDetail getCustomerDetail(String username);
    public Page<CustomerAccount> getAllCustomers(int pageNumber, int pageSize);
    public Customer getCustomerByUserID(String userID);
    // create/ update customer
    public boolean addNewCustomer(CustomerAccount customer);
    public boolean updateCustomerInfo(CustomerDetail customer);
    // search customers
    public Page<CustomerAccount> searchCustomersByCustomerID(String customerID, int pageNumber, int pageSize);
    public Page<CustomerAccount> searchCustomersByContactName(String contactName, int pageNumber, int pageSize);
    public Page<CustomerAccount> searchCustomersByUsername(String username, int pageNumber, int pageSize);
    public Page<CustomerAccount> searchCustomersByEmail(String email, int pageNumber, int pageSize);
}
