package com.phucx.account.service.customer;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    @Cacheable(value = "customer", key = "#customerID")
    public Customer getCustomerByID(String customerID) throws CustomerNotFoundException;
    @Cacheable(value = "customer", key = "#username")
    public Customer getCustomerByUsername(String username) throws CustomerNotFoundException;
    @Cacheable(value = "customer", key = "#userID")
    public Customer getCustomerByUserID(String userID) throws CustomerNotFoundException;

    public List<Customer> getCustomersByIDs(List<String> customerIDs);
    @Cacheable(value = "customerdetails", key = "#customerID")
    public CustomerDetails getCustomerDetailByCustomerID(String customerID) throws CustomerNotFoundException, UserNotFoundException;
    @Cacheable(value = "customerdetail", key = "#username")
    public CustomerDetail getCustomerDetail(String username) throws CustomerNotFoundException, UserNotFoundException;
    @Cacheable(value = "customeraccount", key = "#pageNumber")
    public Page<CustomerAccount> getAllCustomers(int pageNumber, int pageSize);
    // create/ update customer
    public boolean addNewCustomer(CustomerAccount customer) throws InvalidUserException;

    @Caching(
        evict = {
            @CacheEvict(cacheNames = {"customeraccount", "customer", "customerdetails", "customerdetail"}, key = "#customer.customerID"),
            @CacheEvict(cacheNames = {"customeraccount", "customerdetail"}, key = "#customer.username"),
            @CacheEvict(cacheNames = {"userinfo"}, key = "#customer.userID"),
        }
    )
    public boolean updateCustomerInfo(CustomerDetail customer) throws CustomerNotFoundException;


    // search customers
    @Cacheable(value = "customeraccount", key = "#customerID + ':' + #pageNumber")
    public Page<CustomerAccount> searchCustomersByCustomerID(String customerID, int pageNumber, int pageSize);
    @Cacheable(value = "customeraccount", key = "#contactName + ':' + #pageNumber")
    public Page<CustomerAccount> searchCustomersByContactName(String contactName, int pageNumber, int pageSize);
    @Cacheable(value = "customeraccount", key = "#username + ':' + #pageNumber")
    public Page<CustomerAccount> searchCustomersByUsername(String username, int pageNumber, int pageSize);
    @Cacheable(value = "customeraccount", key = "#email + ':' + #pageNumber")
    public Page<CustomerAccount> searchCustomersByEmail(String email, int pageNumber, int pageSize);
}
