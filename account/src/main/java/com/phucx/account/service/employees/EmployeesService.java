package com.phucx.account.service.employees;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.phucx.account.model.Employees;

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public interface EmployeesService {
    public Employees getEmployeeDetailByID(String employeeID);
    public Employees getEmployeeDetail(String username);
    public boolean createEmployee(Employees employee);
    public boolean updateEmployeeInfo(Employees employee);
    public Page<Employees> findAllEmployees(int pageNumber, int pageSize);

    public void receiveOrder(String order) throws JsonMappingException, JsonProcessingException;
}
