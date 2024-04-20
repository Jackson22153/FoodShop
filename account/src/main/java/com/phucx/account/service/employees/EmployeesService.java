package com.phucx.account.service.employees;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.Employees;
import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderWithProducts;

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public interface EmployeesService {
    public Employees getEmployeeDetailByID(String employeeID);
    public EmployeeDetail getEmployeeDetail(String username);
    public boolean createEmployee(Employees employee);
    public Boolean updateEmployeeInfo(EmployeeDetail employee);
    public Page<Employees> findAllEmployees(int pageNumber, int pageSize);
    public NotificationMessage placeOrder(OrderWithProducts order);

    public Page<OrderWithProducts> getPendingOrders(int pageNumber, int pageSize);
}
