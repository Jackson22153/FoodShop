package com.phucx.account.service.employee;

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
	public EmployeeDetail updateEmployeeInfo(EmployeeDetail employee) throws EmployeeNotFoundException {
        log.info("updateEmployeeInfo({})", employee.toString());
        EmployeeDetail fetchedEmployee =employeeDetailRepostiory.findById(employee.getEmployeeID())
            .orElseThrow(()-> new EmployeeNotFoundException("Employee " + employee.getEmployeeID() + " does not found"));  
        String picture = imageService.getImageName(employee.getPicture());

        // update employee 
        Boolean result = employeeDetailRepostiory.updateEmployeeInfo(
            fetchedEmployee.getEmployeeID(), employee.getFirstName(), employee.getLastName(), 
            employee.getBirthDate(), employee.getAddress(), employee.getCity(), 
            employee.getPhone(), picture); 
        if(!result) throw new RuntimeException("Employee " + employee.getEmployeeID() + " can not be updated!");

        employee.setPicture(picture);
        employeeImageService.setEmployeeDetailImage(employee);
        return employee;
	}

    @Override
    public Page<EmployeeDetail> searchEmployeesByEmployeeID(String employeeID, int pageNumber, int pageSize) {
        String searchParam = "%" + employeeID +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeDetail> employees = employeeDetailRepostiory.findByEmployeeIDLike(searchParam, page);
        employeeImageService.setEmployeeDetailImage(employees.getContent());
        return employees;
    }

    @Override
    public Page<EmployeeDetail> searchEmployeesByFirstName(String firstName, int pageNumber, int pageSize) {
        String searchParam = "%" + firstName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeDetail> employees = employeeDetailRepostiory.findByFirstNameLike(searchParam, page);
        employeeImageService.setEmployeeDetailImage(employees.getContent());
        return employees;
    }

    @Override
    public Page<EmployeeDetail> searchEmployeesByLastName(String lastName, int pageNumber, int pageSize) {
        String searchParam = "%" + lastName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeDetail> employees = employeeDetailRepostiory.findByLastNameLike(searchParam, page);
        employeeImageService.setEmployeeDetailImage(employees.getContent());
        return employees;
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
            fetchedEmployee.getEmployeeID(), employee.getFirstName(), 
            employee.getLastName(), employee.getHireDate(), picture, 
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
        if(employeedDetail.getUsername()==null) throw new InvalidUserException("Missing username");
        if(employeedDetail.getEmail()==null) throw new InvalidUserException("Missing email");
        if(employeedDetail.getFirstName()==null)
            throw new InvalidUserException("Employee last name is missing");
        if(employeedDetail.getLastName()==null)
            throw new InvalidUserException("Employee last name is missing");
        if(employeedDetail.getUserID()==null)
            throw new InvalidUserException("UserId is missing");

        Optional<EmployeeDetail> fetchedEmployee= employeeDetailRepostiory.findByUserID(employeedDetail.getUserID());
        if(fetchedEmployee.isPresent()) throw new EntityExistsException("User " + employeedDetail.getUserID() + " already exists");
        // add new employee
        String profileID = UUID.randomUUID().toString();
        String employeeID = UUID.randomUUID().toString();

       return employeeDetailRepostiory.addNewEmployee(
            profileID, employeedDetail.getUserID(), employeedDetail.getUsername(), 
            employeedDetail.getEmail(), employeeID, employeedDetail.getFirstName(), 
            employeedDetail.getLastName());
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
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String firstname = jwt.getClaimAsString(WebConstant.FIRST_NAME);
            String lastname = jwt.getClaimAsString(WebConstant.LAST_NAME);
            Boolean result = this.addNewEmployee(new EmployeeDetail(userID, firstname, lastname));
            if(!result) throw new RuntimeException("Can not add new employee with userID " + userID);
            return this.getEmployeeByUserID(userID);
        }
    }

    @Override
    public Page<EmployeeDetail> searchEmployeesByUsername(String username, int pageNumber, int pageSize) {
        log.info("searchEmployeesByUsername(username={}, pageNumber={}, pageSize={})", username, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeDetail> employees = employeeDetailRepostiory.findByUsernameLike(username, pageable);
        employeeImageService.setEmployeeDetailImage(employees.getContent());
        return employees;
    }
    @Override
    public Page<EmployeeDetail> searchEmployeesByEmail(String email, int pageNumber, int pageSize) {
        log.info("searchEmployeesByEmail(email={}, pageNumber={}, pageSize={})", email, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeDetail> employees = employeeDetailRepostiory.findByEmailLike(email, pageable);
        employeeImageService.setEmployeeDetailImage(employees.getContent());
        return employees;
    }
}
