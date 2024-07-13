package com.phucx.keycloakmanagement.service.employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.keycloakmanagement.constant.EventType;
import com.phucx.keycloakmanagement.constant.MessageQueueConstant;
import com.phucx.keycloakmanagement.constant.RoleConstant;
import com.phucx.keycloakmanagement.exception.NotFoundException;
import com.phucx.keycloakmanagement.model.DataDTO;
import com.phucx.keycloakmanagement.model.Employee;
import com.phucx.keycloakmanagement.model.EmployeeDTO;
import com.phucx.keycloakmanagement.model.EmployeeDetails;
import com.phucx.keycloakmanagement.model.EventMessage;
import com.phucx.keycloakmanagement.model.User;
import com.phucx.keycloakmanagement.service.keycloak.KeycloakService;
import com.phucx.keycloakmanagement.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImp implements EmployeeService{
    @Autowired
    private KeycloakService keycloakService;
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public Employee getEmployee(String userID) throws JsonProcessingException, NotFoundException {
        log.info("getEmployee(userID={})", userID);

        EmployeeDTO payload = new EmployeeDTO();
        payload.setUserID(userID);

        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<DataDTO>(
            eventID, EventType.GetEmployeeByUserID, payload);
        
        EventMessage<Employee> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ACCOUNT_EXCHANGE, 
            MessageQueueConstant.EMPLOYEE_ROUTING_KEY, Employee.class);
        if(response==null || response.getEventType().equals(EventType.NotFoundException)){
            throw new NotFoundException(response.getErrorMessage());
        }
        log.info("Response: {}", response);

        return response.getPayload();
    }

    @Override
    public EmployeeDetails getEmployeeDetails(String userID) throws JsonProcessingException, NotFoundException {
        log.info("getEmployeeDetails(userID={})", userID);
        // fetch employee
        Employee fetchedEmployee = this.getEmployee(userID);
        // fetch appropriate user
        User fetchedUser = keycloakService.getUser(fetchedEmployee.getUserID());

        return new EmployeeDetails(fetchedEmployee.getEmployeeID(), fetchedEmployee.getUserID(), fetchedEmployee.getBirthDate(), 
            fetchedEmployee.getHireDate(), fetchedEmployee.getPhone(), fetchedEmployee.getPicture(), 
            fetchedEmployee.getTitle(),fetchedEmployee.getAddress(), fetchedEmployee.getCity(), 
            fetchedEmployee.getNotes(), fetchedUser.getUsername(), fetchedUser.getFirstName(), 
            fetchedUser.getLastName(), fetchedUser.getEmail());
        
    }

    @Override
    public List<Employee> getEmployees(List<String> userIDs) throws JsonProcessingException {
        log.info("getEmployees(userIDs={})", userIDs);
        EmployeeDTO payload = new EmployeeDTO();
        payload.setUserIDs(userIDs);

        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<DataDTO>(
            eventID, EventType.GetEmployeesByUserIDs, payload);
        
        TypeReference<EventMessage<List<Employee>>> typeReference = new TypeReference<EventMessage<List<Employee>>>() {};

        EventMessage<List<Employee>> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ACCOUNT_EXCHANGE, 
            MessageQueueConstant.EMPLOYEE_ROUTING_KEY, typeReference);

        log.info("Response: {}", response);

        return response.getPayload();
    }

    @Override
    public List<User> getEmployees(Integer pageNumber, Integer pageSize) throws JsonProcessingException {
        log.info("getEmployees(pageNumber={}, pageSize={})", pageNumber, pageSize);
        List<User> users = keycloakService.getUsersByRole(RoleConstant.EMPLOYEE.name(), pageNumber, pageSize);
        return convertUser(users);
    }

    @Override
    public List<User> searchEmployeesByUsername(String username, Integer pageNumber, Integer pageSize)
            throws JsonProcessingException {
        log.info("searchEmployeesByUsername(username={}, pageNumber={}, pageSize={})", username, pageNumber, pageSize);
        List<User> users = keycloakService.searchUsersByUsernameAndRole(username, RoleConstant.EMPLOYEE.name(), pageNumber, pageSize);
        return convertUser(users);
    }

    @Override
    public List<User> searchEmployeesByFirstName(String firstName, Integer pageNumber, Integer pageSize)
            throws JsonProcessingException {
        log.info("searchEmployeesByFirstName(firstName={}, pageNumber={}, pageSize={})", firstName, pageNumber, pageSize);
        List<User> users = keycloakService.searchUsersByFirstNameAndRole(firstName, RoleConstant.EMPLOYEE.name(), pageNumber, pageSize);
        return convertUser(users);
    }

    @Override
    public List<User> searchEmployeesByLastName(String lastName, Integer pageNumber, Integer pageSize)
            throws JsonProcessingException {
        log.info("searchEmployeesByLastName(lastName={}, pageNumber={}, pageSize={})", lastName, pageNumber, pageSize);
        List<User> users = keycloakService.searchUsersByLastNameAndRole(lastName, RoleConstant.EMPLOYEE.name(), pageNumber, pageSize);
        return convertUser(users);
    }

    @Override
    public List<User> searchEmployeesByEmail(String email, Integer pageNumber, Integer pageSize)
            throws JsonProcessingException {
        log.info("searchEmployeesByEmail(email={}, pageNumber={}, pageSize={})", email, pageNumber, pageSize);
        List<User> users = keycloakService.searchUsersByEmailAndRole(email, RoleConstant.EMPLOYEE.name(), pageNumber, pageSize);
        return convertUser(users);
    }

    private List<User> convertUser(List<User> users) throws JsonProcessingException{
        List<String> userIds = users.stream().map(User::getUserID).collect(Collectors.toList());
        List<Employee> employees = this.getEmployees(userIds);
        
        for (User user : users) {
            Employee fetchEmployee = this.findEmployee(employees, user.getUserID()).orElse(new Employee());
            user.setPicture(fetchEmployee.getPicture());
        }
        return users;
    }

    private Optional<Employee> findEmployee(List<Employee> employees, String userID){
        return employees.stream().filter(employee -> employee.getUserID().equals(userID)).findFirst();
    }
    
}
