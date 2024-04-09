package com.phucx.account.service.employees;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.phucx.account.model.Employees;
import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderWithProducts;

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public interface EmployeesService {
    public Employees getEmployeeDetailByID(String employeeID);
    public Employees getEmployeeDetail(String username);
    public boolean createEmployee(Employees employee);
    public boolean updateEmployeeInfo(Employees employee);
    public Page<Employees> findAllEmployees(int pageNumber, int pageSize);
    public NotificationMessage placeOrder(OrderWithProducts order);

    public List<OrderWithProducts> getPendingOrders();
}
