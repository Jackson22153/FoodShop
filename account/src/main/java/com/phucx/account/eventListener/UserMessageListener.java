package com.phucx.account.eventListener;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.account.config.MessageQueueConfig;
import com.phucx.account.constant.EventType;
import com.phucx.account.model.Customer;
import com.phucx.account.model.Employee;
import com.phucx.account.model.EventMessage;
import com.phucx.account.model.Shipper;
import com.phucx.account.model.User;
import com.phucx.account.model.UserRequest;
import com.phucx.account.service.customer.CustomerService;
import com.phucx.account.service.employee.EmployeeService;
import com.phucx.account.service.shipper.ShipperService;
import com.phucx.account.service.user.UserService;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.USER_QUEUE)
public class UserMessageListener {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShipperService shipperService;
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitHandler
    public String fetchUser(String message){
        log.info("fetchUser({})", message);
        // create response message
        String eventID = UUID.randomUUID().toString();
        EventMessage<Object> responseMessage = new EventMessage<>();
        responseMessage.setEventId(eventID);
        try {
            TypeReference<EventMessage<UserRequest>> typeRef = new TypeReference<EventMessage<UserRequest>>() {};
            EventMessage<UserRequest> userRequest = objectMapper.readValue(message, typeRef);
            UserRequest payload = userRequest.getPayload();
            // fetch data
            if(userRequest.getEventType().equals(EventType.GetCustomerByID)){
                // get customer by id
                String customerID = payload.getCustomerID();
                Customer fetchedCustomer = customerService.getCustomerByID(customerID);
                // set response message
                responseMessage.setPayload(fetchedCustomer);
                responseMessage.setEventType(EventType.ReturnCustomerByID);
            }else if(userRequest.getEventType().equals(EventType.GetCustomerByUserID)){
                // get customer by userID
                String userID = payload.getUserID();
                Customer fetchedCustomer = customerService.getCustomerByUserID(userID);
                // set response message
                responseMessage.setPayload(fetchedCustomer);
                responseMessage.setEventType(EventType.ReturnCustomerByUserID);
            }else if(userRequest.getEventType().equals(EventType.GetCustomersByID)){
                // get customers by ids
                List<String> customerIDs = payload.getCustomerIDs();
                List<Customer> fetchedCustomers = customerService.getCustomersByIDs(customerIDs);
                // set response message
                responseMessage.setPayload(fetchedCustomers);
                responseMessage.setEventType(EventType.ReturnCustomersByID);
            }else if(userRequest.getEventType().equals(EventType.GetEmployeeByID)){
                // get employee by id
                String employeeID = payload.getEmployeeID();
                Employee fetchedEmployee = employeeService.getEmployee(employeeID);
                // set response message
                responseMessage.setPayload(fetchedEmployee);
                responseMessage.setEventType(EventType.ReturnEmployeeByID);
            }else if(userRequest.getEventType().equals(EventType.GetUserByCustomerID)){
                // get user by customerID
                String customerID = payload.getCustomerID();
                User fetchedUser = userService.getUserByCustomerID(customerID);
                // set response message
                responseMessage.setPayload(fetchedUser);
                responseMessage.setEventType(EventType.ReturnUserByCustomerID);
            }else if(userRequest.getEventType().equals(EventType.GetUserByEmployeeID)){
                // get user by employeeID
                String employeeID = payload.getEmployeeID();
                User fetchedUser = userService.getUserByEmployeeID(employeeID);
                // set response message
                responseMessage.setPayload(fetchedUser);
                responseMessage.setEventType(EventType.ReturnUserByEmployeeID);
            }else if(userRequest.getEventType().equals(EventType.GetShipperByID)){
                // get shipper by id
                Integer shipperID = payload.getShipperID();
                Shipper fetchedShipper = shipperService.getShipperByID(shipperID);
                // set response message
                responseMessage.setPayload(fetchedShipper);
                responseMessage.setEventType(EventType.ReturnShipperByID);
            }
            String response = objectMapper.writeValueAsString(responseMessage);
            return response;
        } catch (Exception e) {
           log.error("Error: {}", e.getMessage());
           return null;
        }
    }
}
