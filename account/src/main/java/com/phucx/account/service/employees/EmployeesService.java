package com.phucx.account.service.employees;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.phucx.account.constant.OrderStatus;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.Employees;
import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderWithProducts;

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public interface EmployeesService {
    public Employees getEmployeeDetailByID(String employeeID);
    public EmployeeDetail getEmployeeDetail(String username);
    public boolean createEmployee(Employees employee);
    public Boolean updateEmployeeInfo(EmployeeDetail employee);
    public Page<EmployeeAccount> getAllEmployees(int pageNumber, int pageSize);
    public NotificationMessage placeOrder(OrderWithProducts order);

    public Page<OrderDetailsDTO> getOrders(int pageNumber, int pageSize, String employeeID, OrderStatus status);
    public Page<OrderDetailsDTO> getPendingOrders(int pageNumber, int pageSize);
    public OrderWithProducts getPendingOrderDetail(int orderID) throws InvalidOrderException;
    public OrderWithProducts getOrderDetail(Integer orderID, String employeeID) throws InvalidOrderException;

    public Page<EmployeeAccount> searchEmployeesByEmployeeID(String employeeID, int pageNumber, int pageSize);
    public Page<EmployeeAccount> searchEmployeesByFirstName(String firstName, int pageNumber, int pageSize);
    public Page<EmployeeAccount> searchEmployeesByLastName(String lastName, int pageNumber, int pageSize);
    public Page<EmployeeAccount> searchEmployeesByUsername(String username, int pageNumber, int pageSize);
    public Page<EmployeeAccount> searchEmployeesByEmail(String email, int pageNumber, int pageSize);
}
