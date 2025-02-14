package com.phucx.account.service.employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.account.config.MessageQueueConfig;
import com.phucx.account.constant.WebConstant;
import com.phucx.account.exception.EmployeeNotFoundException;
import com.phucx.account.exception.InvalidUserException;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.EmployeeDetails;
import com.phucx.account.model.EmployeeDetailsBuilder;
import com.phucx.account.repository.EmployeeDetailRepostiory;
import com.phucx.account.service.image.EmployeeImageService;
import com.phucx.account.service.image.ImageService;
import com.phucx.account.service.messageQueue.MessageQueueService;
import com.phucx.account.service.notification.NotificationService;
import com.phucx.constant.EventType;
import com.phucx.constant.NotificationStatus;
import com.phucx.constant.NotificationTitle;
import com.phucx.constant.NotificationTopic;
import com.phucx.model.DataDTO;
import com.phucx.model.EmployeeDTO;
import com.phucx.model.EventMessage;
import com.phucx.model.UserNotificationDTO;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImp implements EmployeeService {
    @Autowired
    private EmployeeDetailRepostiory employeeDetailRepostiory;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private EmployeeImageService employeeImageService;
    @Autowired
    private MessageQueueService messageQueueService;

	@Override
	public EmployeeDetail updateEmployeeInfo(EmployeeDetail employee) throws EmployeeNotFoundException, JsonProcessingException {
        log.info("updateEmployeeInfo({})", employee.toString());
        EmployeeDetail fetchedEmployee =employeeDetailRepostiory.findById(employee.getEmployeeID())
            .orElseThrow(()-> new EmployeeNotFoundException("Employee " + employee.getEmployeeID() + " does not found")); 
        String picture = imageService.getImageName(employee.getPicture());

        // update employee 
        Boolean result = employeeDetailRepostiory.updateEmployeeInfo(
            fetchedEmployee.getEmployeeID(), 
            employee.getBirthDate(), 
            employee.getAddress(), 
            employee.getCity(),
            employee.getDistrict(),
            employee.getWard(), 
            employee.getPhone(), 
            picture); 
        if(!result) throw new RuntimeException("Employee " + employee.getEmployeeID() + " can not be updated!");

        employee.setPicture(picture);
        employeeImageService.setEmployeeDetailImage(employee);
        return employee;
	}

    @Override
    public EmployeeDetail updateAdminEmployeeInfo(EmployeeDetail employee) throws JsonProcessingException, EmployeeNotFoundException {
        log.info("updateAdminEmployeeInfo({})", employee.toString());
        EmployeeDetail fetchedEmployee = employeeDetailRepostiory.findById(employee.getEmployeeID())
            .orElseThrow(()-> new EmployeeNotFoundException("Employee " + employee.getEmployeeID() + " does not found"));
        // get image
        String picture = this.imageService.getImageName(employee.getPicture());
        // update employee's information
        Boolean status = employeeDetailRepostiory.updateAdminEmployeeInfo(
            fetchedEmployee.getEmployeeID(), employee.getHireDate(), picture, 
            employee.getTitle(), employee.getNotes());
        // set employee picture
        employee.setPicture(picture);
        employeeImageService.setEmployeeDetailImage(employee);
        // create a notification
        UserNotificationDTO notification = new UserNotificationDTO();
        notification.setTitle(NotificationTitle.USER_INFO_UPDATE);
        notification.setTopic(NotificationTopic.Account);
        notification.setUserID(fetchedEmployee.getUserID());
        notification.setPicture(employee.getPicture());
        if(status){
            notification.setMessage("Your information has been updated by Admin");
            notification.setStatus(NotificationStatus.SUCCESSFUL);
            notification.setReceiverID(fetchedEmployee.getUserID());
        }else{
            notification.setMessage("Error: Your information can not be updated by Admin");
            notification.setStatus(NotificationStatus.ERROR);
            notification.setReceiverID(fetchedEmployee.getUserID());
        }
        // send notification
        notificationService.sendEmployeeNotification(notification);
        if(!status) throw new RuntimeException("Employee " + employee.getEmployeeID() + " can not be updated!");


        return employee;
    }

    @Override
    public EmployeeDetail getEmployee(String employeeID) throws EmployeeNotFoundException {
        EmployeeDetail employee = employeeDetailRepostiory.findById(employeeID)
            .orElseThrow(()-> new EmployeeNotFoundException("Employee " + employeeID + " does not found"));
        employeeImageService.setEmployeeDetailImage(employee);
        return employee;
    }


    @Override
    public EmployeeDetail addNewEmployee(EmployeeDetail employeedDetail) throws InvalidUserException {
        log.info("addNewEmployee({})", employeedDetail);
        if(employeedDetail.getUserID()==null)
            throw new InvalidUserException("UserId is missing");

        Optional<EmployeeDetail> fetchedEmployee= employeeDetailRepostiory.findByUserID(employeedDetail.getUserID());
        if(fetchedEmployee.isPresent()) throw new EntityExistsException("User " + employeedDetail.getUserID() + " already exists");
        // add new employee
        String profileID = UUID.randomUUID().toString();
        String employeeID = UUID.randomUUID().toString();

       Boolean status = employeeDetailRepostiory.addNewEmployee(profileID, employeedDetail.getUserID(), employeeID);
       if(!status) throw new RuntimeException("Error when creating new employee profile!");

       return new EmployeeDetail(employeeID, employeedDetail.getUserID());
    }

    @Override
    public EmployeeDetail getEmployeeByUserID(String userID) throws EmployeeNotFoundException {
        EmployeeDetail fetchedEmployee = employeeDetailRepostiory.findByUserID(userID)
            .orElseThrow(()-> new EmployeeNotFoundException("Employee with UserID: " + userID + " does not found"));
        return employeeImageService.setEmployeeDetailImage(fetchedEmployee);
    }

    @Override
    public Page<EmployeeDetail> getEmployees(Integer pageNumber, Integer pageSize) {
        log.info("getEmployees(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeDetail> employees = employeeDetailRepostiory.findAll(pageable);
        employeeImageService.setEmployeeDetailImage(employees.getContent());
        return employees;
    }

    @Override
    public EmployeeDetail getEmployeeDetail(String userID) throws JsonProcessingException, InvalidUserException{
        log.info("getEmployeeDetail(userID={})", userID);
        Optional<EmployeeDetail> fetchedEmployeeOptional = employeeDetailRepostiory.findByUserID(userID);
        if(fetchedEmployeeOptional.isPresent()){
            EmployeeDetail fetchedEmployee = fetchedEmployeeOptional.get();
            employeeImageService.setEmployeeDetailImage(fetchedEmployee);
            return fetchedEmployee;
        }else{
            return this.requestCreatingEmployeeDetail(userID);
        }
    }
    // send a request for creating a new employeedetail 
    private EmployeeDetail requestCreatingEmployeeDetail(String userID) throws JsonProcessingException, InvalidUserException{
        String eventID = UUID.randomUUID().toString();
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUserID(userID);
        EventMessage<DataDTO> eventMessage= new EventMessage<DataDTO>(
            eventID, EventType.CreateEmployeeDetail, employeeDTO);
        EventMessage<EmployeeDetail> responseMessage = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConfig.ACCOUNT_EXCHANGE, 
            MessageQueueConfig.EMPLOYEE_ROUTING_KEY, 
            EmployeeDetail.class);
        log.info("response: {}", responseMessage);
        if(responseMessage.getEventType().equals(EventType.InvalidUserException)){
            throw new InvalidUserException(responseMessage.getErrorMessage());
        }
        return responseMessage.getPayload();
    }
    
    @Override
    public EmployeeDetails getEmployeeDetails(String userID) throws JsonProcessingException, InvalidUserException {
        log.info("getEmployeeDetails(userID={})", userID);
        EmployeeDetail employeeDetail = this.getEmployeeDetail(userID);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String firstname = jwt.getClaimAsString(WebConstant.FIRST_NAME);
        String lastname = jwt.getClaimAsString(WebConstant.LAST_NAME);
        String username = jwt.getClaimAsString(WebConstant.PREFERRED_USERNAME);
        String email = jwt.getClaimAsString(WebConstant.EMAIL);

        return new EmployeeDetailsBuilder()
            .withEmployeeID(employeeDetail.getEmployeeID())
            .withUserID(userID)
            .withBirthDate(employeeDetail.getBirthDate())
            .withHireDate(employeeDetail.getHireDate())
            .withPhone(employeeDetail.getPhone())
            .withPicture(employeeDetail.getPicture())
            .withTitle(employeeDetail.getTitle())
            .withAddress(employeeDetail.getAddress())
            .withCity(employeeDetail.getCity())
            .withDistrict(employeeDetail.getDistrict())
            .withWard(employeeDetail.getWard())
            .withNotes(employeeDetail.getNotes())
            .withUsername(username)
            .withFirstName(firstname)
            .withLastName(lastname)
            .withEmail(email)
            .build();
    }

    @Override
    public List<EmployeeDetail> getEmployees(List<String> userIds) {
        log.info("getEmployees(userIds={})", userIds);
        List<EmployeeDetail> employees = employeeDetailRepostiory.findAllByUserID(userIds);
        employeeImageService.setEmployeeDetailImage(employees);
        return employees;
    }
}
