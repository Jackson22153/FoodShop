package com.phucx.account.eventListener;

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
import com.phucx.account.constant.EventType;
import com.phucx.account.exception.EmployeeNotFoundException;
import com.phucx.account.model.EmployeeDTO;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.EventMessage;
import com.phucx.account.model.UserProfile;
import com.phucx.account.service.employee.EmployeeService;
import com.phucx.account.service.user.UserProfileService;

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
            TypeReference<EventMessage<EmployeeDTO>> typeRef = new TypeReference<EventMessage<EmployeeDTO>>() {};
            EventMessage<EmployeeDTO> employeeDTO = objectMapper.readValue(message, typeRef);
            EmployeeDTO payload = employeeDTO.getPayload();
            // fetch data
            if(employeeDTO.getEventType().equals(EventType.GetEmployeeByID)){
                // get employee by id
                String employeeID = payload.getEmployeeID();
                EmployeeDetail fetchedEmployee = employeeService.getEmployee(employeeID);
                // set response message
                responseMessage.setPayload(fetchedEmployee);
                responseMessage.setEventType(EventType.ReturnEmployeeByID);
            }else if(employeeDTO.getEventType().equals(EventType.GetEmployeeByUserID)){
                // get employee by id
                String userID = payload.getUserID();
                EmployeeDetail fetchedEmployee = employeeService.getEmployeeByUserID(userID);
                // set response message
                responseMessage.setPayload(fetchedEmployee);
                responseMessage.setEventType(EventType.ReturnEmployeeByUserID);
            }else if(employeeDTO.getEventType().equals(EventType.GetUserByEmployeeID)){
                // get user by employeeID
                String employeeID = payload.getEmployeeID();
                UserProfile fetchedUser = userProfileService.getUserProfileByEmployeeID(employeeID);
                // set response message
                responseMessage.setPayload(fetchedUser);
                responseMessage.setEventType(EventType.ReturnUserByEmployeeID);
            }else if(employeeDTO.getEventType().equals(EventType.GetEmployeesByUserIDs)){
                // get user by employeeID
                List<String> employeeIDs = payload.getUserIDs();
                List<EmployeeDetail> fetchedEmployees = employeeService.getEmployees(employeeIDs);
                // set response message
                responseMessage.setPayload(fetchedEmployees);
                responseMessage.setEventType(EventType.ReturnEmployeesByUserIDs);
            }
            String response = objectMapper.writeValueAsString(responseMessage);
            return response;
        } catch (EmployeeNotFoundException e){
            log.error("Error: {}", e.getMessage());
            try {
                responseMessage.setErrorMessage(e.getMessage());
                responseMessage.setEventType(EventType.NotFoundException);
                String responsemessage = objectMapper.writeValueAsString(responseMessage);
                return responsemessage;
            } catch (Exception exception) {
                log.error("Error: {}", e.getMessage());
                return null;
            }
        } catch (JsonProcessingException e) {
           log.error("Error: {}", e.getMessage());
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
