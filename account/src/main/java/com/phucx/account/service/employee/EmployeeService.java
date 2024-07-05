package com.phucx.account.service.employee;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;

import com.phucx.account.model.EmployeeDetail;
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

    @Cacheable(value = "employeedetail", key = "#userID")
    public EmployeeDetail getEmployeeDetail(String userID) throws InvalidUserException, EmployeeNotFoundException;
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
    public EmployeeDetail updateEmployeeInfo(EmployeeDetail employee) throws EmployeeNotFoundException;

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
    // search
    @Cacheable(value = "employeeDetail", key = "#employeeID + ':' + #pageNumber")
    public Page<EmployeeDetail> searchEmployeesByEmployeeID(String employeeID, int pageNumber, int pageSize);
    @Cacheable(value = "employeeDetail", key = "#firstName + ':' + #pageNumber")
    public Page<EmployeeDetail> searchEmployeesByFirstName(String firstName, int pageNumber, int pageSize);
    @Cacheable(value = "employeeDetail", key = "#lastName + ':' + #pageNumber")
    public Page<EmployeeDetail> searchEmployeesByLastName(String lastName, int pageNumber, int pageSize);
    @Cacheable(value = "employeeDetail", key = "#username + ':' + #pageNumber")
    public Page<EmployeeDetail> searchEmployeesByUsername(String username, int pageNumber, int pageSize);
    @Cacheable(value = "employeeDetail", key = "#email + ':' + #pageNumber")
    public Page<EmployeeDetail> searchEmployeesByEmail(String email, int pageNumber, int pageSize);
}
