package com.phucx.order.service.employee;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.EventType;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.model.DataDTO;
import com.phucx.order.model.Employee;
import com.phucx.order.model.EmployeeDTO;
import com.phucx.order.model.EventMessage;
import com.phucx.order.service.messageQueue.MessageQueueService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImp implements EmployeeService {
    @Autowired
    private MessageQueueService messageQueueService;
   
    @Override
    public Employee getEmployeeByID(String employeeID) throws JsonProcessingException {
        log.info("getEmployeeByID(employeeID={})", employeeID);
        // create a request for user
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeID(employeeID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetEmployeeByID);
        eventMessage.setPayload(employeeDTO);
        // receive data
        EventMessage<Employee> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ACCOUNT_EXCHANGE, 
            MessageQueueConstant.EMPLOYEE_ROUTING_KEY,
            Employee.class);
        log.info("response={}", response);
        return  response.getPayload();
    }

    @Override
    public Employee getEmployeeByUserID(String userID) throws JsonProcessingException {
        log.info("getEmployeeByUserID(userID={})", userID);
        // create a request for user
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUserID(userID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetEmployeeByUserID);
        eventMessage.setPayload(employeeDTO);
        // receive data
        EventMessage<Employee> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.ACCOUNT_EXCHANGE, 
            MessageQueueConstant.EMPLOYEE_ROUTING_KEY,
            Employee.class);
        log.info("response={}", response);
        return  response.getPayload();
    }
}
