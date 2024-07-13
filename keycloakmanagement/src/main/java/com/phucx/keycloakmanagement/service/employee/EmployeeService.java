package com.phucx.keycloakmanagement.service.employee;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.keycloakmanagement.exception.NotFoundException;
import com.phucx.keycloakmanagement.model.Employee;
import com.phucx.keycloakmanagement.model.EmployeeDetails;
import com.phucx.keycloakmanagement.model.User;

public interface EmployeeService {
    public Employee getEmployee(String userID) throws JsonProcessingException, NotFoundException;
    public List<Employee> getEmployees(List<String> userIDs) throws JsonProcessingException;
    public EmployeeDetails getEmployeeDetails(String userID) throws JsonProcessingException, NotFoundException;

    public List<User> getEmployees(Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public List<User> searchEmployeesByUsername(String username, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public List<User> searchEmployeesByFirstName(String firstName, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public List<User> searchEmployeesByLastName(String lastName, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
    public List<User> searchEmployeesByEmail(String email, Integer pageNumber, Integer pageSize) throws JsonProcessingException;
}
