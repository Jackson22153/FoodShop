package com.phucx.account.service.employee;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.account.constant.EmailVerified;
import com.phucx.account.constant.NotificationStatus;
import com.phucx.account.constant.NotificationTitle;
import com.phucx.account.constant.NotificationTopic;
import com.phucx.account.constant.UserStatus;
import com.phucx.account.constant.WebConstant;
import com.phucx.account.exception.EmployeeNotFoundException;
import com.phucx.account.exception.InvalidUserException;
import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.EmployeeDetails;
import com.phucx.account.model.UserNotificationDTO;
import com.phucx.account.model.Employee;
import com.phucx.account.model.User;
import com.phucx.account.model.UserInfo;
import com.phucx.account.repository.EmployeeAccountRepository;
import com.phucx.account.repository.EmployeeDetailRepostiory;
import com.phucx.account.repository.EmployeeRepository;
import com.phucx.account.service.image.EmployeeImageService;
import com.phucx.account.service.image.ImageService;
import com.phucx.account.service.notification.NotificationService;
import com.phucx.account.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImp implements EmployeeService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeAccountRepository employeeAccountRepository;
    @Autowired
    private EmployeeDetailRepostiory employeeDetailRepostiory;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private EmployeeImageService employeeImageService;

	@Override
	public EmployeeDetails getEmployeeByID(String employeeID) throws UserNotFoundException {
        log.info("getEmployeeByID(employeeID={})", employeeID);
        Employee employee = employeeRepository.findById(employeeID)
            .orElseThrow(()-> new EmployeeNotFoundException("Employee " + employeeID + " does not found"));
        UserInfo user = userService.getUserInfo(employee.getUserID());
        
        employeeImageService.setEmployeeImage(employee);

        EmployeeDetails employeeDetails = new EmployeeDetails(
            employee.getEmployeeID(), user, employee.getFirstName(), employee.getLastName(), 
            employee.getBirthDate(), employee.getHireDate(), employee.getHomePhone(), 
            employee.getPhoto(), employee.getTitle(), employee.getAddress(), employee.getCity(), 
            employee.getNotes());

        return employeeDetails;
    }

	@Override
	public EmployeeDetail updateEmployeeInfo(EmployeeDetail employee) throws EmployeeNotFoundException {
        log.info("updateEmployeeInfo({})", employee.toString());
        Employee fetchedEmployee =employeeRepository.findById(employee.getEmployeeID())
            .orElseThrow(()-> new EmployeeNotFoundException("Employee " + employee.getEmployeeID() + " does not found"));  
        String picture = imageService.getImageName(employee.getPhoto());
        
        // update employee info
        Boolean status = employeeDetailRepostiory.updateEmployeeInfo(
            fetchedEmployee.getEmployeeID(), employee.getEmail(), employee.getFirstName(), 
            employee.getLastName(), employee.getBirthDate(), employee.getAddress(), 
            employee.getCity(), employee.getHomePhone(), picture);   
        if(!status) throw new RuntimeException("Employee " + employee.getEmployeeID() + " can not be updated!");
        return employeeDetailRepostiory.findById(employee.getEmployeeID())
        .orElseThrow(()-> new EmployeeNotFoundException("Employee " + employee.getEmployeeID() + " does not found")); 
	}

	@Override
	public Page<EmployeeAccount> getAllEmployees(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findAll(pageable);
        employeeImageService.setEmployeeAccountImage(employees.getContent());
        return employees;
	}

	@Override
	public EmployeeDetail getEmployeeDetail(String username) throws UserNotFoundException {
        User user = userService.getUser(username);
        Optional<EmployeeAccount> employeeAccOp = employeeAccountRepository.findByUserID(user.getUserID());
        if(employeeAccOp.isPresent()){
            EmployeeAccount employeeAcc = employeeAccOp.get();
            String employeeID = employeeAcc.getEmployeeID();
            EmployeeDetail employee = employeeDetailRepostiory.findById(employeeID)
                .orElseThrow(()-> new EmployeeNotFoundException("EmployeeID: " + employeeID + " does not found"));
            employeeImageService.setEmployeeDetailImage(employee);
            return employee;
        }else{
            String employeeID = UUID.randomUUID().toString();
            employeeAccountRepository.createEmployeeInfo(employeeID, username, username, username);
            EmployeeDetail employee = employeeDetailRepostiory.findById(employeeID)
                .orElseThrow(()-> new EmployeeNotFoundException("EmployeeID: " + employeeID + " does not found"));
            employeeImageService.setEmployeeDetailImage(employee);
            return employee;
        }
	}

    @Override
    public Page<EmployeeAccount> searchEmployeesByEmployeeID(String employeeID, int pageNumber, int pageSize) {
        String searchParam = "%" + employeeID +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByEmployeeIDLike(searchParam, page);
        employeeImageService.setEmployeeAccountImage(employees.getContent());
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByFirstName(String firstName, int pageNumber, int pageSize) {
        String searchParam = "%" + firstName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByFirstNameLike(searchParam, page);
        employeeImageService.setEmployeeAccountImage(employees.getContent());
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByLastName(String lastName, int pageNumber, int pageSize) {
        String searchParam = "%" + lastName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByLastNameLike(searchParam, page);
        employeeImageService.setEmployeeAccountImage(employees.getContent());
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByUsername(String username, int pageNumber, int pageSize) {
        String searchParam = "%" + username +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByUsernameLike(searchParam, page);
        employeeImageService.setEmployeeAccountImage(employees.getContent());
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByEmail(String email, int pageNumber, int pageSize) {
        String searchParam = "%" + email +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByEmailLike(searchParam, page);
        employeeImageService.setEmployeeAccountImage(employees.getContent());
        return employees;
    }

    @Override
    public Boolean updateAdminEmployeeInfo(EmployeeDetails employee) throws JsonProcessingException, EmployeeNotFoundException {
        log.info("updateAdminEmployeeInfo({})", employee.toString());
        Employee fetchedEmployee = employeeRepository.findById(employee.getEmployeeID())
            .orElseThrow(()-> new EmployeeNotFoundException("Employee " + employee.getEmployeeID() + " does not found"));
        // get image
        String picture = this.imageService.getImageName(employee.getPhoto());
        // update employee's information
        Boolean status = employeeRepository.updateAdminEmployeeInfo(
            fetchedEmployee.getEmployeeID(), employee.getFirstName(), employee.getLastName(), 
            employee.getHireDate(), picture, employee.getNotes());
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

        return status;
    }

    @Override
    public Employee getEmployee(String employeeID) throws EmployeeNotFoundException {
        Employee employee = employeeRepository.findById(employeeID)
            .orElseThrow(()-> new EmployeeNotFoundException("Employee " + employeeID + " does not found"));
        employeeImageService.setEmployeeImage(employee);
        return employee;
    }

    @Override
    public Boolean addNewEmployee(EmployeeAccount employeeAccount) throws InvalidUserException {
        log.info("addNewEmployee({})", employeeAccount);
        if(employeeAccount.getUsername() == null)
            throw new InvalidUserException("user's username is null");
        if(employeeAccount.getEmail()==null)
            throw new InvalidUserException("user's email is null");
        if(employeeAccount.getFirstName()==null || employeeAccount.getLastName()==null)
            throw new InvalidUserException("User's first name or last name is null");
        EmployeeAccount fetchedEmployeeByUsername= employeeAccountRepository.findByUsername(employeeAccount.getUsername());
        if(fetchedEmployeeByUsername!=null) throw new InvalidUserException("User " + fetchedEmployeeByUsername.getUsername() + " already exists");
        var fetchedCustomerByEmail= employeeAccountRepository.findByEmail(employeeAccount.getEmail());
        if(fetchedCustomerByEmail.isPresent()) throw new InvalidUserException("User with email " + employeeAccount.getEmail() + " already exists");
        // add new employee
        String userID = UUID.randomUUID().toString();
        String employeeID = UUID.randomUUID().toString();
        // hash password
        String hashedPassword = passwordEncoder.encode(WebConstant.DEFUALT_PASSWORD);
        // add new employee account
        return employeeAccountRepository.addNewEmployee(
            userID, employeeAccount.getUsername(), hashedPassword, employeeAccount.getEmail(), 
            EmailVerified.YES.getValue(), UserStatus.ENABLED.getValue(), employeeID, 
            employeeAccount.getFirstName(), employeeAccount.getLastName());
    }

    @Override
    public Employee getEmployeeByUserID(String userID) throws EmployeeNotFoundException {
        Employee fetchedEmployee = employeeRepository.findByUserID(userID)
            .orElseThrow(()-> new EmployeeNotFoundException("Employee with UserID: " + userID + " does not found"));
        return employeeImageService.setEmployeeImage(fetchedEmployee);
    }
}
