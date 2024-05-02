package com.phucx.account.service.employee;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.phucx.account.constant.OrderStatus;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.EmployeeDetailDTO;
import com.phucx.account.model.Employee;
import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderWithProducts;

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public interface EmployeeService {
    public EmployeeDetailDTO getEmployeeByID(String employeeID);
    public EmployeeDetail getEmployeeDetail(String username);
    public boolean createEmployee(Employee employee);
    public Boolean updateEmployeeInfo(EmployeeDetail employee);
    public Boolean updateAdminEmployeeInfo(Employee employee);
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
