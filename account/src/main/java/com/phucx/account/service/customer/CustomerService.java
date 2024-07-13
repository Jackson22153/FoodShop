package com.phucx.account.service.customer;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.CustomerDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.account.exception.CustomerNotFoundException;
import com.phucx.account.exception.InvalidUserException;
import com.phucx.account.exception.UserNotFoundException;

public interface CustomerService {
    // get customer
    @Cacheable(value = "customerdetail", key = "#customerID")
    public CustomerDetail getCustomerByID(String customerID) throws CustomerNotFoundException;
    @Cacheable(value = "customerdetail", key = "#userID")
    public CustomerDetail getCustomerByUserID(String userID) throws CustomerNotFoundException;
    public List<CustomerDetail> getCustomersByUserIDs(List<String> userIDs);

    public List<CustomerDetail> getCustomersByIDs(List<String> customerIDs);

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
    public CustomerDetail updateCustomerInfo(CustomerDetail customer) throws CustomerNotFoundException, JsonProcessingException;

    @Cacheable(value = "customerdetail", key = "#userID")
    public CustomerDetail getCustomerDetail(String userID) throws InvalidUserException, UserNotFoundException;
    public CustomerDetails getCustomerDetails(String userID) throws InvalidUserException, UserNotFoundException;
}
