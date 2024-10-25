package com.phucx.account.eventListener;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.account.config.MessageQueueConfig;
import com.phucx.account.exception.EmployeeNotFoundException;
import com.phucx.account.exception.InvalidUserException;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.UserProfile;
import com.phucx.account.service.employee.EmployeeService;
import com.phucx.account.service.user.UserProfileService;
import com.phucx.constant.EventType;
import com.phucx.model.EventMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.EMPLOYEE_QUEUE)
public class EmployeeMessageListener {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitHandler
    public String fetchEmployee(String message){
        log.info("fetchEmployee({})", message);
        // create response message
        EventMessage<Object> responseMessage = this.createResponseMessage(Object.class);
        try {
            TypeReference<EventMessage<LinkedHashMap<String, Object>>> typeRef = 
                new TypeReference<EventMessage<LinkedHashMap<String, Object>>>() {};
            EventMessage<LinkedHashMap<String, Object>> eventMessage = objectMapper.readValue(message, typeRef);
            LinkedHashMap<String, Object> employeeDTO = eventMessage.getPayload();
            // fetch data
            if(eventMessage.getEventType().equals(EventType.GetEmployeeByID)){
                // get employee by id
                String employeeID = employeeDTO.get("employeeID").toString();
                EmployeeDetail fetchedEmployee = employeeService.getEmployee(employeeID);
                // set response message
                responseMessage.setPayload(fetchedEmployee);
                responseMessage.setEventType(EventType.ReturnEmployeeByID);
            }else if(eventMessage.getEventType().equals(EventType.GetEmployeeByUserID)){
                // get employee by id
                String userID = employeeDTO.get("userID").toString();
                EmployeeDetail fetchedEmployee = employeeService.getEmployeeByUserID(userID);
                // set response message
                responseMessage.setPayload(fetchedEmployee);
                responseMessage.setEventType(EventType.ReturnEmployeeByUserID);
            }else if(eventMessage.getEventType().equals(EventType.GetUserByEmployeeID)){
                // get user by employeeID
                String employeeID = employeeDTO.get("employeeID").toString();
                UserProfile fetchedUser = userProfileService.getUserProfileByEmployeeID(employeeID);
                // set response message
                responseMessage.setPayload(fetchedUser);
                responseMessage.setEventType(EventType.ReturnUserByEmployeeID);
            }else if(eventMessage.getEventType().equals(EventType.GetEmployeesByUserIDs)){
                // get user by employeeID
                List<String> employeeIDs = (List<String>) employeeDTO.get("userIDs");
                List<EmployeeDetail> fetchedEmployees = employeeService.getEmployees(employeeIDs);
                // set response message
                responseMessage.setPayload(fetchedEmployees);
                responseMessage.setEventType(EventType.ReturnEmployeesByUserIDs);
            }else if(eventMessage.getEventType().equals(EventType.CreateEmployeeDetail)){
                // create new employee profile
                String userID = employeeDTO.get("userID").toString();
                EmployeeDetail newEmployeeDetail = employeeService.addNewEmployee(new EmployeeDetail(userID));
                responseMessage.setEventType(EventType.ReturnCreateEmployeeDetail);
                responseMessage.setPayload(newEmployeeDetail);
            }
            String response = objectMapper.writeValueAsString(responseMessage);
            return response;
        } catch (EmployeeNotFoundException e){
            log.error("Error: {}", e.getMessage());
            return handleNotFoundException(responseMessage, e.getMessage());
        } catch (JsonProcessingException e) {
            log.error("Error: {}", e.getMessage());
            return null;
        } catch (InvalidUserException e){
            log.error("Error: {}", e.getMessage());
            return handleInvalidUser(responseMessage, e.getMessage());
        }
    }

    private String handleInvalidUser(EventMessage<Object> responseMessage, String errorMessage){
        try {
            responseMessage.setErrorMessage(errorMessage);
            responseMessage.setEventType(EventType.InvalidUserException);
            String responsemessage = objectMapper.writeValueAsString(responseMessage);
            return responsemessage;
        } catch (Exception exception) {
            log.error("Error: {}", exception.getMessage());
            return null;
        }
    }

    private String handleNotFoundException(EventMessage<Object> responseMessage, String errorMessage){
        try {
            responseMessage.setErrorMessage(errorMessage);
            responseMessage.setEventType(EventType.NotFoundException);
            String responsemessage = objectMapper.writeValueAsString(responseMessage);
            return responsemessage;
        } catch (Exception exception) {
            log.error("Error: {}", exception.getMessage());
            return null;
        }
    }

    private <T> EventMessage<T> createResponseMessage(Class<T> type){
        EventMessage<T> responseMessage = new EventMessage<>();
        String eventID = UUID.randomUUID().toString();
        responseMessage.setEventId(eventID);
        return responseMessage;
    }
}
