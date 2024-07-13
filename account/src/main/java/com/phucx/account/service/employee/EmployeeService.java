package com.phucx.account.service.employee;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;

import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.EmployeeDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.account.exception.EmployeeNotFoundException;
import com.phucx.account.exception.InvalidUserException;

public interface EmployeeService {
    // get employee
    @Cacheable(value = "employeedetail", key = "#employeeID")
    public EmployeeDetail getEmployee(String employeeID) throws EmployeeNotFoundException;
    @Cacheable(value = "employeedetail", key = "#userID")
    public EmployeeDetail getEmployeeByUserID(String userID) throws EmployeeNotFoundException;
    @Cacheable(value = "employeedetail", key = "#pageNumber")
    public Page<EmployeeDetail> getEmployees(Integer pageNumber, Integer pageSize);

    public List<EmployeeDetail> getEmployees(List<String> userIds);

    @Cacheable(value = "employeedetail", key = "#userID")
    public EmployeeDetail getEmployeeDetail(String userID) throws InvalidUserException, EmployeeNotFoundException;
    public EmployeeDetails getEmployeeDetails(String userID) throws InvalidUserException, EmployeeNotFoundException;
    // update / create employee
    @Caching(
        put = {
            @CachePut(cacheNames = "employeedetail", key = "#employee.employeeID"),
            @CachePut(cacheNames = "employeedetail", key = "#employee.userID")
        },
        evict = {
            @CacheEvict(cacheNames = {"userinfo"}, key = "#employee.userID"),
            @CacheEvict(cacheNames = {"userinfo"}, key = "#employee.employeeID")
        }
    )
    public EmployeeDetail updateEmployeeInfo(EmployeeDetail employee) throws EmployeeNotFoundException, JsonProcessingException;

    @Caching(
        put = {
            @CachePut(cacheNames = "employeedetail", key = "#employee.employeeID"),
            @CachePut(cacheNames = "employeedetail", key = "#employee.userID")
        },
        evict = {
            @CacheEvict(cacheNames = {"userinfo"}, key = "#employee.userID"),
            @CacheEvict(cacheNames = {"userinfo"}, key = "#employee.employeeID")
        }
    )
    public EmployeeDetail updateAdminEmployeeInfo(EmployeeDetail employee) throws JsonProcessingException, EmployeeNotFoundException;
}
