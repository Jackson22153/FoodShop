package com.phucx.account.service.employees;

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
import com.phucx.account.model.EmployeeAccounts;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.Employees;
import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.repository.EmployeeAccountsRepository;
import com.phucx.account.repository.EmployeeDetailRepostiory;
import com.phucx.account.repository.EmployeesRepository;
import com.phucx.account.service.github.GithubService;
import com.phucx.account.service.messageQueue.sender.MessageSender;
import com.phucx.account.service.order.OrderService;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeesServiceImp implements EmployeesService {
    // private Logger logger = LoggerFactory.getLogger(EmployeesServiceImp.class);
    @Autowired
    private GithubService githubService;
    @Autowired
    private EmployeesRepository employeesRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private EmployeeAccountsRepository employeeAccountsRepository;
    @Autowired
    private EmployeeDetailRepostiory employeeDetailRepostiory;

	@Override
	public Employees getEmployeeDetailByID(String employeeID) {
		var employeeOP = employeesRepository.findById(employeeID);
        if(employeeOP.isPresent()) return employeeOP.get();
        return null;
    }

	@Override
	public boolean createEmployee(Employees employee) {
		var employeeOp = employeesRepository.findById(employee.getEmployeeID());
        if(employeeOp.isEmpty()){
            var e = employeesRepository.save(employee);
            if(e!=null) return true;
        }
        return false;
	} 

	@Override
	public Boolean updateEmployeeInfo(EmployeeDetail employee) {
        log.info("updateEmployeeInfo({})", employee.toString());
		try {
            Employees fetchedEmployee =employeesRepository.findById(employee.getEmployeeID())
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
	public Page<Employees> getAllEmployees(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
        var employees = employeesRepository.findAll(pageable);
        return employees;
	}

	@Override
	public EmployeeDetail getEmployeeDetail(String username) {
        EmployeeAccounts employeeAcc = employeeAccountsRepository.findByUsername(username);
        if(employeeAcc!=null){
            String employeeID = employeeAcc.getEmployeeID();
            EmployeeDetail employee = employeeDetailRepostiory.findById(employeeID)
                .orElseThrow(()-> new NotFoundException("EmployeeID: " + employeeID + " does not found"));
            return employee;
        }else{
            String employeeID = UUID.randomUUID().toString();
            employeeAccountsRepository.createEmployeeInfo(employeeID, username, username, username);
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
}
