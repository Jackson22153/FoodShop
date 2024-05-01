package com.phucx.account.service.employee;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.account.config.MessageQueueConfig;
import com.phucx.account.constant.OrderStatus;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.Employee;
import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.repository.EmployeeAccountRepository;
import com.phucx.account.repository.EmployeeDetailRepostiory;
import com.phucx.account.repository.EmployeeRepository;
import com.phucx.account.service.github.GithubService;
import com.phucx.account.service.messageQueue.sender.MessageSender;
import com.phucx.account.service.order.OrderService;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImp implements EmployeeService {
    // private Logger logger = LoggerFactory.getLogger(EmployeeServiceImp.class);
    @Autowired
    private GithubService githubService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private EmployeeAccountRepository employeeAccountRepository;
    @Autowired
    private EmployeeDetailRepostiory employeeDetailRepostiory;

	@Override
	public Employee getEmployeeDetailByID(String employeeID) {
        Employee employee = employeeRepository.findById(employeeID)
            .orElseThrow(()-> new NotFoundException("Employee " + employeeID + " does not found"));
        return employee;
    }

	@Override
	public boolean createEmployee(Employee employee) {
		var employeeOp = employeeRepository.findById(employee.getEmployeeID());
        if(employeeOp.isEmpty()){
            var e = employeeRepository.save(employee);
            if(e!=null) return true;
        }
        return false;
	} 

	@Override
	public Boolean updateEmployeeInfo(EmployeeDetail employee) {
        log.info("updateEmployeeInfo({})", employee.toString());
		try {
            Employee fetchedEmployee =employeeRepository.findById(employee.getEmployeeID())
                .orElseThrow(()-> new NotFoundException("Employee " + employee.getEmployeeID() + " does not found"));


            Boolean status = employeeDetailRepostiory.updateEmployeeInfo(
                fetchedEmployee.getEmployeeID(), employee.getEmail(), employee.getFirstName(), 
                employee.getLastName(), employee.getBirthDate(), employee.getAddress(), 
                employee.getCity(), employee.getHomePhone(), employee.getPhoto());   

            return status;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
	}

	@Override
	public Page<EmployeeAccount> getAllEmployees(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return employeeAccountRepository.findAll(pageable);
	}

	@Override
	public EmployeeDetail getEmployeeDetail(String username) {
        EmployeeAccount employeeAcc = employeeAccountRepository.findByUsername(username);
        if(employeeAcc!=null){
            String employeeID = employeeAcc.getEmployeeID();
            EmployeeDetail employee = employeeDetailRepostiory.findById(employeeID)
                .orElseThrow(()-> new NotFoundException("EmployeeID: " + employeeID + " does not found"));
            return employee;
        }else{
            String employeeID = UUID.randomUUID().toString();
            employeeAccountRepository.createEmployeeInfo(employeeID, username, username, username);
            EmployeeDetail employee = employeeDetailRepostiory.findById(employeeID)
                .orElseThrow(()-> new NotFoundException("EmployeeID: " + employeeID + " does not found"));
            return employee;
        }
	}

    @Override
    public NotificationMessage placeOrder(OrderWithProducts order) {
        NotificationMessage response = messageSender.sendAndReceiveOrder(
            MessageQueueConfig.ORDER_QUEUE, MessageQueueConfig.ORDER_ROUTING_KEY, order);
        return response;
    }

    @Override
    public Page<OrderDetailsDTO> getOrders(int pageNumber, int pageSize, String employeeID, OrderStatus status) {
        Page<OrderDetailsDTO> pendingOrders = new PageImpl<>(new ArrayList<>());
        if(status.equals(OrderStatus.All)){
            pendingOrders = orderService.getEmployeeOrders(pageNumber, pageSize, employeeID);
        }else {
            pendingOrders = orderService.getEmployeeOrders(pageNumber, pageSize, employeeID, status);
        }
        return pendingOrders;
    }

    @Override
    public Page<OrderDetailsDTO> getPendingOrders(int pageNumber, int pageSize) {
        return orderService.getPendingOrders(pageNumber, pageSize);
    }

    @Override
    public OrderWithProducts getOrderDetail(Integer orderID, String employeeID) throws InvalidOrderException {
        OrderWithProducts order = orderService.getEmployeeOrderDetail(orderID, employeeID);
        log.info("order: {}", order.toString());
        return order;
    }

    @Override
    public OrderWithProducts getPendingOrderDetail(int orderID) throws InvalidOrderException {
        return orderService.getPendingOrderDetail(orderID);
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByEmployeeID(String employeeID, int pageNumber, int pageSize) {
        String searchParam = "%" + employeeID +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByEmployeeIDLike(searchParam, page);
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByFirstName(String firstName, int pageNumber, int pageSize) {
        String searchParam = "%" + firstName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByFirstNameLike(searchParam, page);
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByLastName(String lastName, int pageNumber, int pageSize) {
        String searchParam = "%" + lastName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByLastNameLike(searchParam, page);
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByUsername(String username, int pageNumber, int pageSize) {
        String searchParam = "%" + username +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByUsernameLike(searchParam, page);
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByEmail(String email, int pageNumber, int pageSize) {
        String searchParam = "%" + email +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByEmailLike(searchParam, page);
        return employees;
    }
}
