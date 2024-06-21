package com.phucx.order.service.employee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.Employee;

public interface EmployeeService {
    // get employee
    public Employee getEmployeeByID(String employeeID) throws JsonProcessingException, NotFoundException;
    public Employee getEmployeeByUserID(String userID) throws JsonProcessingException, NotFoundException;
}
