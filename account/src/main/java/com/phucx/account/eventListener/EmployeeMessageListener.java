package com.phucx.account.eventListener;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.account.config.MessageQueueConfig;
import com.phucx.account.constant.EventType;
import com.phucx.account.model.Employee;
import com.phucx.account.model.EmployeeDTO;
import com.phucx.account.model.EventMessage;
import com.phucx.account.model.User;
import com.phucx.account.service.employee.EmployeeService;
import com.phucx.account.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.EMPLOYEE_QUEUE)
public class EmployeeMessageListener {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitHandler
    public String fetchEmployee(String message){
        log.info("fetchEmployee({})", message);
        // create response message
        String eventID = UUID.randomUUID().toString();
        EventMessage<Object> responseMessage = new EventMessage<>();
        responseMessage.setEventId(eventID);
        try {
            TypeReference<EventMessage<EmployeeDTO>> typeRef = new TypeReference<EventMessage<EmployeeDTO>>() {};
            EventMessage<EmployeeDTO> employeeDTO = objectMapper.readValue(message, typeRef);
            EmployeeDTO payload = employeeDTO.getPayload();
            // fetch data
            if(employeeDTO.getEventType().equals(EventType.GetEmployeeByID)){
                // get employee by id
                String employeeID = payload.getEmployeeID();
                Employee fetchedEmployee = employeeService.getEmployee(employeeID);
                // set response message
                responseMessage.setPayload(fetchedEmployee);
                responseMessage.setEventType(EventType.ReturnEmployeeByID);
            }else if(employeeDTO.getEventType().equals(EventType.GetEmployeeByUserID)){
                // get employee by id
                String userID = payload.getUserID();
                Employee fetchedEmployee = employeeService.getEmployeeByUserID(userID);
                // set response message
                responseMessage.setPayload(fetchedEmployee);
                responseMessage.setEventType(EventType.ReturnEmployeeByUserID);
            }else if(employeeDTO.getEventType().equals(EventType.GetUserByEmployeeID)){
                // get user by employeeID
                String employeeID = payload.getEmployeeID();
                User fetchedUser = userService.getUserByEmployeeID(employeeID);
                // set response message
                responseMessage.setPayload(fetchedUser);
                responseMessage.setEventType(EventType.ReturnUserByEmployeeID);
            }
            String response = objectMapper.writeValueAsString(responseMessage);
            return response;
        } catch (Exception e) {
           log.error("Error: {}", e.getMessage());
           return null;
        }
    }
}
