package com.phucx.account.service.employees;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.account.config.MessageQueueConfig;
import com.phucx.account.model.EmployeeAccounts;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.Employees;
import com.phucx.account.model.NotificationMessage;
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
            // var fetchedEmployeeOp = employeesRepository
            //     .findById(employee.getEmployeeID());
            // if(fetchedEmployeeOp.isPresent()){
            //     Employees fetchedEmployee = fetchedEmployeeOp.get();
            //     String picture = employee.getPhoto();
            //     if(picture!=null){
            //         if(fetchedEmployee.getPhoto()==null){
            //             picture = githubService.uploadImage(picture);
            //         }else{
            //             int comparedPicture =fetchedEmployee.getPhoto()
            //                 .compareToIgnoreCase(picture);
            //             if(comparedPicture!=0){
            //                 picture = githubService.uploadImage(picture);
            //             }else if(comparedPicture==0){
            //                 picture = fetchedEmployee.getPhoto();
            //             }
            //         }
            //     }
            //     Integer check = employeesRepository.updateEmployee(
            //         employee.getFirstName(), employee.getLastName(),
            //         employee.getBirthDate(), employee.getAddress(), 
            //         employee.getCity(), employee.getHomePhone(), picture, 
            //         employee.getEmployeeID());
            //     if(check>0){
            //         return true;
            //     }
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
	}

	@Override
	public Page<Employees> findAllEmployees(int pageNumber, int pageSize) {
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
    public Page<OrderWithProducts> getPendingOrders(int pageNumber, int pageSize) {
        Page<OrderWithProducts> pendingOrders = orderService.getPendingOrders(pageNumber, pageSize);
        return pendingOrders;
    }
}
