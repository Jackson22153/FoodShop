package com.phucx.account.service.employee;

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
    public Employee getEmployee(String employeeID) throws EmployeeNotFoundException;
    public Employee getEmployeeByUserID(String userID) throws EmployeeNotFoundException;
    public EmployeeDetails getEmployeeByID(String employeeID) throws EmployeeNotFoundException, UserNotFoundException;
    public EmployeeDetail getEmployeeDetail(String username) throws EmployeeNotFoundException, UserNotFoundException;
    public Page<EmployeeAccount> getAllEmployees(int pageNumber, int pageSize);
    // update / create employee
    public Boolean updateEmployeeInfo(EmployeeDetail employee) throws EmployeeNotFoundException;
    public Boolean updateAdminEmployeeInfo(Employee employee) throws JsonProcessingException, EmployeeNotFoundException;
    public Boolean addNewEmployee(EmployeeAccount employeeAccount) throws InvalidUserException;
    // search
    public Page<EmployeeAccount> searchEmployeesByEmployeeID(String employeeID, int pageNumber, int pageSize);
    public Page<EmployeeAccount> searchEmployeesByFirstName(String firstName, int pageNumber, int pageSize);
    public Page<EmployeeAccount> searchEmployeesByLastName(String lastName, int pageNumber, int pageSize);
    public Page<EmployeeAccount> searchEmployeesByUsername(String username, int pageNumber, int pageSize);
    public Page<EmployeeAccount> searchEmployeesByEmail(String email, int pageNumber, int pageSize);
}
