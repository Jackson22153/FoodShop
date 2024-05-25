package com.phucx.account.service.employee;

import org.springframework.data.domain.Page;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.EmployeeDetailDTO;
import com.phucx.account.model.Notification;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.constant.OrderStatus;
import com.phucx.account.model.Employee;

public interface EmployeeService {
    // get employee
    public Employee getEmployee(String employeeID);
    public EmployeeDetailDTO getEmployeeByID(String employeeID);
    public EmployeeDetail getEmployeeDetail(String username);
    public Page<EmployeeAccount> getAllEmployees(int pageNumber, int pageSize);
    // update / create employee
    public boolean createEmployee(Employee employee);
    public Boolean updateEmployeeInfo(EmployeeDetail employee);
    public Boolean updateAdminEmployeeInfo(Employee employee);
    public Boolean addNewEmployee(EmployeeAccount employeeAccount);
    // search
    public Page<EmployeeAccount> searchEmployeesByEmployeeID(String employeeID, int pageNumber, int pageSize);
    public Page<EmployeeAccount> searchEmployeesByFirstName(String firstName, int pageNumber, int pageSize);
    public Page<EmployeeAccount> searchEmployeesByLastName(String lastName, int pageNumber, int pageSize);
    public Page<EmployeeAccount> searchEmployeesByUsername(String username, int pageNumber, int pageSize);
    public Page<EmployeeAccount> searchEmployeesByEmail(String email, int pageNumber, int pageSize);
    // get order
    public Page<OrderDetailsDTO> getOrders(String employeeID, OrderStatus status, int pageNumber, int pageSize);
    public OrderWithProducts getOrder(String orderID, String employeeID);
    // notification
    public Page<Notification> getNotifications(String userID, int pageNumber, int pageSize);
    Boolean turnOffNotification(String notificationID, String userID);
}
