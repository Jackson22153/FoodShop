package com.phucx.account.service.customer;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.exception.CustomerNotFoundException;
import com.phucx.account.exception.InvalidUserException;
import com.phucx.account.exception.UserNotFoundException;

public interface CustomerService {
    // get customer
    @Cacheable(value = "customerdetail", key = "#customerID")
    public CustomerDetail getCustomerByID(String customerID) throws CustomerNotFoundException;
    @Cacheable(value = "customerdetail", key = "#userID")
    public CustomerDetail getCustomerByUserID(String userID) throws CustomerNotFoundException;
    @Cacheable(value = "customerdetail", key = "#pageNumber")
    public Page<CustomerDetail> getCustomers(Integer pageNumber, Integer pageSize);
    
    public List<CustomerDetail> getCustomersByIDs(List<String> customerIDs);
   
    @Cacheable(value = "customerdetail", key = "#userID")
    public CustomerDetail getCustomerDetail(String userID) throws InvalidUserException, UserNotFoundException;

    @Caching(
        evict = {
            @CacheEvict(cacheNames = {"userinfo"}, key = "#customer.userID"),
            @CacheEvict(cacheNames = {"userinfo"}, key = "#customer.customerID"),
        },
        put = {
            @CachePut(cacheNames = {"customerdetail"}, key = "#customer.customerID"),
            @CachePut(cacheNames = {"customerdetail"}, key = "#customer.userID")
        }
    )
    public CustomerDetail updateCustomerInfo(CustomerDetail customer) throws CustomerNotFoundException;
    // search customers
    @Cacheable(value = "customerdetail", key = "#customerID + ':' + #pageNumber")
    public Page<CustomerDetail> searchCustomersByCustomerID(String customerID, int pageNumber, int pageSize);
    @Cacheable(value = "customerdetail", key = "#contactName + ':' + #pageNumber")
    public Page<CustomerDetail> searchCustomersByContactName(String contactName, int pageNumber, int pageSize);
    @Cacheable(value = "customerdetail", key = "#username + ':' + #pageNumber")
    public Page<CustomerDetail> searchCustomersByUsername(String username, int pageNumber, int pageSize);
    @Cacheable(value = "customerdetail", key = "#email + ':' + #pageNumber")
    public Page<CustomerDetail> searchCustomersByEmail(String email, int pageNumber, int pageSize);
}
