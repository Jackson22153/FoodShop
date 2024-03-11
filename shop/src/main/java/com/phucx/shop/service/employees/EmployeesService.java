package com.phucx.shop.service.employees;

import java.util.List;

import org.springframework.data.domain.Page;
import com.phucx.shop.model.Employees;

public interface EmployeesService {
    public Employees getEmployee(String employeeID);
    public List<Employees> getEmployees();
    public Page<Employees> getEmployees(int pageNumber, int pageSize);
}
