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
import com.phucx.account.constant.NotificationStatus;
import com.phucx.account.constant.NotificationTitle;
import com.phucx.account.constant.NotificationTopic;
import com.phucx.account.constant.WebConstant;
import com.phucx.account.exception.EmployeeNotFoundException;
import com.phucx.account.exception.InvalidUserException;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.EmployeeDetails;
import com.phucx.account.model.UserNotificationDTO;
import com.phucx.account.repository.EmployeeDetailRepostiory;
import com.phucx.account.service.image.EmployeeImageService;
import com.phucx.account.service.image.ImageService;
import com.phucx.account.service.notification.NotificationService;
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

	@Override
	public EmployeeDetail updateEmployeeInfo(EmployeeDetail employee) throws EmployeeNotFoundException, JsonProcessingException {
        log.info("updateEmployeeInfo({})", employee.toString());
        EmployeeDetail fetchedEmployee =employeeDetailRepostiory.findById(employee.getEmployeeID())
            .orElseThrow(()-> new EmployeeNotFoundException("Employee " + employee.getEmployeeID() + " does not found")); 
        String picture = imageService.getImageName(employee.getPicture());

        // update employee 
        Boolean result = employeeDetailRepostiory.updateEmployeeInfo(
            fetchedEmployee.getEmployeeID(), employee.getBirthDate(), employee.getAddress(), employee.getCity(), 
            employee.getPhone(), picture); 
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
        // create a notification
        UserNotificationDTO notification = new UserNotificationDTO();
        notification.setTitle(NotificationTitle.USER_INFO_UPDATE);
        notification.setTopic(NotificationTopic.Account);
        notification.setUserID(fetchedEmployee.getUserID());
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

        employee.setPicture(picture);
        employeeImageService.setEmployeeDetailImage(employee);
        return employee;
    }

    @Override
    public EmployeeDetail getEmployee(String employeeID) throws EmployeeNotFoundException {
        EmployeeDetail employee = employeeDetailRepostiory.findById(employeeID)
            .orElseThrow(()-> new EmployeeNotFoundException("Employee " + employeeID + " does not found"));
        employeeImageService.setEmployeeDetailImage(employee);
        return employee;
    }


    private Boolean addNewEmployee(EmployeeDetail employeedDetail) throws InvalidUserException {
        log.info("addNewEmployee({})", employeedDetail);
        if(employeedDetail.getUserID()==null)
            throw new InvalidUserException("UserId is missing");

        Optional<EmployeeDetail> fetchedEmployee= employeeDetailRepostiory.findByUserID(employeedDetail.getUserID());
        if(fetchedEmployee.isPresent()) throw new EntityExistsException("User " + employeedDetail.getUserID() + " already exists");
        // add new employee
        String profileID = UUID.randomUUID().toString();
        String employeeID = UUID.randomUUID().toString();

       return employeeDetailRepostiory.addNewEmployee(profileID, employeedDetail.getUserID(), employeeID);
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
    public EmployeeDetail getEmployeeDetail(String userID) throws InvalidUserException, EmployeeNotFoundException {
        log.info("getEmployeeDetail(userID={})", userID);
        Optional<EmployeeDetail> fetchedEmployeeOptional = employeeDetailRepostiory.findByUserID(userID);
        if(fetchedEmployeeOptional.isPresent()){
            EmployeeDetail fetchedEmployee = fetchedEmployeeOptional.get();
            employeeImageService.setEmployeeDetailImage(fetchedEmployee);
            return fetchedEmployee;
        }else{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication==null) throw new RuntimeException("User is not authenticated");
            EmployeeDetail newEmployeeDetail = new EmployeeDetail();
            newEmployeeDetail.setUserID(userID);

            Boolean result = this.addNewEmployee(newEmployeeDetail);
            if(!result) throw new RuntimeException("Can not add new employee with userID " + userID);
            return this.getEmployeeByUserID(userID);
        }
    }
    
    @Override
    public EmployeeDetails getEmployeeDetails(String userID) throws InvalidUserException, EmployeeNotFoundException {
        log.info("getEmployeeDetails(userID={})", userID);
        EmployeeDetail employeeDetail = this.getEmployeeDetail(userID);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String firstname = jwt.getClaimAsString(WebConstant.FIRST_NAME);
        String lastname = jwt.getClaimAsString(WebConstant.LAST_NAME);
        String username = jwt.getClaimAsString(WebConstant.PREFERRED_USERNAME);
        String email = jwt.getClaimAsString(WebConstant.EMAIL);

        return new EmployeeDetails(employeeDetail.getEmployeeID(), userID, 
            employeeDetail.getBirthDate(), employeeDetail.getHireDate(), 
            employeeDetail.getPhone(), employeeDetail.getPicture(), 
            employeeDetail.getTitle(), employeeDetail.getAddress(), 
            employeeDetail.getCity(), employeeDetail.getNotes(), 
            username, firstname, lastname, email);
    }

    @Override
    public List<EmployeeDetail> getEmployees(List<String> userIds) {
        log.info("getEmployees(userIds={})", userIds);
        List<EmployeeDetail> employees = employeeDetailRepostiory.findAllByUserID(userIds);
        employeeImageService.setEmployeeDetailImage(employees);
        return employees;
    }
}
