package com.phucx.account.service.employees;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.account.model.EmployeeAccounts;
import com.phucx.account.model.Employees;
import com.phucx.account.model.UserOrderProducts;
import com.phucx.account.repository.EmployeeAccountsRepository;
import com.phucx.account.repository.EmployeesRepository;
import com.phucx.account.service.github.GithubService;

@Service
public class EmployeesServiceImp implements EmployeesService {
    private Logger logger = LoggerFactory.getLogger(EmployeesServiceImp.class);
    @Autowired
    private GithubService githubService;
    @Autowired
    private EmployeesRepository employeesRepository;
    @Autowired
    private EmployeeAccountsRepository employeeAccountsRepository;
    @Autowired
    private ObjectMapper objectMapper;

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
	public boolean updateEmployeeInfo(Employees employee) {
		try {
            var fetchedEmployeeOp = employeesRepository
                .findById(employee.getEmployeeID());
            if(fetchedEmployeeOp.isPresent()){
                Employees fetchedEmployee = fetchedEmployeeOp.get();
                String picture = employee.getPhoto();
                if(picture!=null){
                    if(fetchedEmployee.getPhoto()==null){
                        picture = githubService.uploadImage(picture);
                    }else{
                        int comparedPicture =fetchedEmployee.getPhoto()
                            .compareToIgnoreCase(picture);
                        if(comparedPicture!=0){
                            picture = githubService.uploadImage(picture);
                        }else if(comparedPicture==0){
                            picture = fetchedEmployee.getPhoto();
                        }
                    }
                }
                Integer check = employeesRepository.updateEmployee(
                    employee.getFirstName(), employee.getLastName(),
                    employee.getBirthDate(), employee.getAddress(), 
                    employee.getCity(), employee.getRegion(), employee.getCountry(),
                    employee.getHomePhone(), picture, employee.getEmployeeID());
                if(check>0){
                    return true;
                }
            }
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
	public Employees getEmployeeDetail(String username) {
        EmployeeAccounts employeeAcc = employeeAccountsRepository.findByUsername(username);
        if(employeeAcc!=null){
            String employeeID = employeeAcc.getEmployeeID();
            var employeeOp = employeesRepository.findById(employeeID);
            if(employeeOp.isPresent()) return employeeOp.get();
        }else{
            String employeeID = UUID.randomUUID().toString();
            employeeAccountsRepository.createEmployeeInfo(employeeID, username, username, username);
            return this.getEmployeeDetailByID(employeeID);
        }
		return null;
	}

    @Override
    @RabbitListener(queues = "#{orderQueue.name}")
    public void receiveOrder(String order) throws JsonMappingException, JsonProcessingException {
        logger.info("Employee has recieved : {}", order.toString());
        UserOrderProducts userOrderProducts = objectMapper.readValue(order, UserOrderProducts.class);
        logger.info("Order receiver: " + userOrderProducts.toString());
        // return true;
    }
    
}
