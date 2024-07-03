package com.phucx.account.service.employee;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.EmployeeDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.account.exception.EmployeeNotFoundException;
import com.phucx.account.exception.InvalidUserException;
import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.Employee;

public interface EmployeeService {
    // get employee
    @Cacheable(value = "employee", key = "#employeeID")
    public Employee getEmployee(String employeeID) throws EmployeeNotFoundException;
    @Cacheable(value = "employee", key = "#userID")
    public Employee getEmployeeByUserID(String userID) throws EmployeeNotFoundException;
    @Cacheable(value = "employeedetails", key = "#employeeID")
    public EmployeeDetails getEmployeeByID(String employeeID) throws EmployeeNotFoundException, UserNotFoundException;
    @Cacheable(value = "employeedetail", key = "#username")
    public EmployeeDetail getEmployeeDetail(String username) throws EmployeeNotFoundException, UserNotFoundException;
    @Cacheable(value = "employeeaccount", key = "#pageNumber")
    public Page<EmployeeAccount> getAllEmployees(int pageNumber, int pageSize);
    // update / create employee
    @Caching(
        put = {
            @CachePut(cacheNames = "employeedetail", key = "#employee.employeeID")
        },
        evict = {
            @CacheEvict(cacheNames = {"employee", "employeedetails", "employeeaccount"}, key = "#employee.employeeID"),
            @CacheEvict(cacheNames = {"employeedetail", "employeeaccount"}, key = "#employee.username"),
            @CacheEvict(cacheNames = {"userinfo"}, key = "#employee.userID")
        }
    )
    public EmployeeDetail updateEmployeeInfo(EmployeeDetail employee) throws EmployeeNotFoundException;

    @Caching(
        evict = {
            @CacheEvict(cacheNames = {"employee", "employeedetails", "employeedetail", "employeeaccount"}, key = "#employee.employeeID"),
            @CacheEvict(cacheNames = {"employeedetail", "employeeaccount"}, key = "#employee.userInfo.user.username"),
            @CacheEvict(cacheNames = {"userinfo"}, key = "#employee.userInfo.user.userID")
        }
    )
    public Boolean updateAdminEmployeeInfo(EmployeeDetails employee) throws JsonProcessingException, EmployeeNotFoundException;
    public Boolean addNewEmployee(EmployeeAccount employeeAccount) throws InvalidUserException;
    // search
    @Cacheable(value = "employeeaccount", key = "#employeeID + ':' + #pageNumber")
    public Page<EmployeeAccount> searchEmployeesByEmployeeID(String employeeID, int pageNumber, int pageSize);
    @Cacheable(value = "employeeaccount", key = "#firstName + ':' + #pageNumber")
    public Page<EmployeeAccount> searchEmployeesByFirstName(String firstName, int pageNumber, int pageSize);
    @Cacheable(value = "employeeaccount", key = "#lastName + ':' + #pageNumber")
    public Page<EmployeeAccount> searchEmployeesByLastName(String lastName, int pageNumber, int pageSize);
    @Cacheable(value = "employeeaccount", key = "#username + ':' + #pageNumber")
    public Page<EmployeeAccount> searchEmployeesByUsername(String username, int pageNumber, int pageSize);
    @Cacheable(value = "employeeaccount", key = "#email + ':' + #pageNumber")
    public Page<EmployeeAccount> searchEmployeesByEmail(String email, int pageNumber, int pageSize);
}
