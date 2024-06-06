package com.phucx.order.service.employee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.model.Employee;

public interface EmployeeService {
    // get employee
    public Employee getEmployeeByID(String employeeID) throws JsonProcessingException;
    public Employee getEmployeeByUserID(String userID) throws JsonProcessingException;
}
