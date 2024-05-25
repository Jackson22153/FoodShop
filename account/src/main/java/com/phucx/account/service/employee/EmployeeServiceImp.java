package com.phucx.account.service.employee;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.phucx.account.constant.OrderStatus;
import com.phucx.account.constant.WebConstant;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.EmployeeDetailDTO;
import com.phucx.account.model.Notification;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.Employee;
import com.phucx.account.model.User;
import com.phucx.account.model.UserInfo;
import com.phucx.account.repository.EmployeeAccountRepository;
import com.phucx.account.repository.EmployeeDetailRepostiory;
import com.phucx.account.repository.EmployeeRepository;
import com.phucx.account.service.notification.NotificationService;
import com.phucx.account.service.order.OrderService;
import com.phucx.account.service.user.UserService;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImp implements EmployeeService {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private EmployeeAccountRepository employeeAccountRepository;
    @Autowired
    private EmployeeDetailRepostiory employeeDetailRepostiory;

	@Override
	public EmployeeDetailDTO getEmployeeByID(String employeeID) {
        log.info("getEmployeeByID(employeeID={})", employeeID);
        Employee employee = employeeRepository.findById(employeeID)
            .orElseThrow(()-> new NotFoundException("Employee " + employeeID + " does not found"));
        UserInfo user = userService.getUserInfo(employee.getUser().getUserID());
    
        EmployeeDetailDTO employeeDetailDTO = new EmployeeDetailDTO(
            employee.getEmployeeID(), user, employee.getFirstName(), employee.getLastName(), 
            employee.getBirthDate(), employee.getHireDate(), employee.getHomePhone(), 
            employee.getPhoto(), employee.getTitle(), employee.getAddress(), employee.getCity(), 
            employee.getNotes());

        return employeeDetailDTO;
    }

	@Override
	public boolean createEmployee(Employee employee) {
		var employeeOp = employeeRepository.findById(employee.getEmployeeID());
        if(employeeOp.isEmpty()){
            var e = employeeRepository.save(employee);
            if(e!=null) return true;
        }
        return false;
	} 

	@Override
	public Boolean updateEmployeeInfo(EmployeeDetail employee) {
        log.info("updateEmployeeInfo({})", employee.toString());
		try {
            Employee fetchedEmployee =employeeRepository.findById(employee.getEmployeeID())
                .orElseThrow(()-> new NotFoundException("Employee " + employee.getEmployeeID() + " does not found"));
            // update employee info
            Boolean status = employeeDetailRepostiory.updateEmployeeInfo(
                fetchedEmployee.getEmployeeID(), employee.getEmail(), employee.getFirstName(), 
                employee.getLastName(), employee.getBirthDate(), employee.getAddress(), 
                employee.getCity(), employee.getHomePhone(), employee.getPhoto());   

            return status;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
	}

	@Override
	public Page<EmployeeAccount> getAllEmployees(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return employeeAccountRepository.findAll(pageable);
	}

	@Override
	public EmployeeDetail getEmployeeDetail(String username) {
        User user = userService.getUser(username);
        Optional<EmployeeAccount> employeeAccOp = employeeAccountRepository.findByUserID(user.getUserID());
        if(employeeAccOp.isPresent()){
            EmployeeAccount employeeAcc = employeeAccOp.get();
            String employeeID = employeeAcc.getEmployeeID();
            EmployeeDetail employee = employeeDetailRepostiory.findById(employeeID)
                .orElseThrow(()-> new NotFoundException("EmployeeID: " + employeeID + " does not found"));
            return employee;
        }else{
            String employeeID = UUID.randomUUID().toString();
            employeeAccountRepository.createEmployeeInfo(employeeID, username, username, username);
            EmployeeDetail employee = employeeDetailRepostiory.findById(employeeID)
                .orElseThrow(()-> new NotFoundException("EmployeeID: " + employeeID + " does not found"));
            return employee;
        }
	}

    @Override
    public Page<EmployeeAccount> searchEmployeesByEmployeeID(String employeeID, int pageNumber, int pageSize) {
        String searchParam = "%" + employeeID +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByEmployeeIDLike(searchParam, page);
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByFirstName(String firstName, int pageNumber, int pageSize) {
        String searchParam = "%" + firstName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByFirstNameLike(searchParam, page);
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByLastName(String lastName, int pageNumber, int pageSize) {
        String searchParam = "%" + lastName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByLastNameLike(searchParam, page);
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByUsername(String username, int pageNumber, int pageSize) {
        String searchParam = "%" + username +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByUsernameLike(searchParam, page);
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByEmail(String email, int pageNumber, int pageSize) {
        String searchParam = "%" + email +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByEmailLike(searchParam, page);
        return employees;
    }

    @Override
    public Boolean updateAdminEmployeeInfo(Employee employee) {
        log.info("updateAdminEmployeeInfo({})", employee.toString());
        Employee fetchedEmployee = employeeRepository.findById(employee.getEmployeeID())
            .orElseThrow(()-> new NotFoundException("Employee " + employee.getEmployeeID() + " does not found"));
        Boolean status = employeeRepository.updateAdminEmployeeInfo(
            fetchedEmployee.getEmployeeID(), employee.getFirstName(), employee.getLastName(), 
            employee.getHireDate(), employee.getPhoto(), employee.getNotes());
        return status;
    }

    @Override
    public Page<Notification> getNotifications(String userID, int pageNumber, int pageSize) {
        return notificationService.getNotificationsByReceiverIDOrNull(userID, pageNumber, pageSize);
    }

    @Override
    public Employee getEmployee(String employeeID) {
        return employeeRepository.findById(employeeID)
            .orElseThrow(()-> new NotFoundException("Employee " + employeeID + " does not found"));
    }

    @Override
    public Boolean turnOffNotification(String notificationID, String userID) {
        Notification notification = notificationService
            .getNotificationByUserIDOrNullAndNotificationID(userID, notificationID);
        return notificationService.updateNotificationActive(
            notification.getNotificationID(), false);
    }

    @Override
    public Boolean addNewEmployee(EmployeeAccount employeeAccount) {
        log.info("addNewEmployee({})", employeeAccount);
        if(employeeAccount.getUsername() == null || employeeAccount.getEmail()==null || 
            employeeAccount.getFirstName()==null || employeeAccount.getLastName()==null)
            throw new RuntimeException("Missing some employee's information");
        EmployeeAccount fetchedEmployeeByUsername= employeeAccountRepository.findByUsername(employeeAccount.getUsername());
        if(fetchedEmployeeByUsername!=null) throw new RuntimeException("Employee " + fetchedEmployeeByUsername.getUsername() + " already exists");
        var fetchedCustomerByEmail= employeeAccountRepository.findByEmail(employeeAccount.getEmail());
        if(fetchedCustomerByEmail.isPresent()) throw new RuntimeException("Employee " + employeeAccount.getEmail() + " already exists");
        // add new employee
        String userID = UUID.randomUUID().toString();
        String employeeID = UUID.randomUUID().toString();
        return employeeAccountRepository.addNewEmployee(
            userID, employeeAccount.getUsername(), 
            passwordEncoder.encode( WebConstant.DEFUALT_PASSWORD), 
            employeeAccount.getEmail(), 
            employeeID, employeeAccount.getFirstName(), 
            employeeAccount.getLastName());
    }

    @Override
    public Page<OrderDetailsDTO> getOrders(String employeeID, OrderStatus status, int pageNumber, int pageSize) {
        log.info("getOrders(employeeID={}, status={}, pageNumber={}, pageSize={})", employeeID, status, pageNumber, pageSize);
        return orderService.getEmployeeOrders(employeeID, status, pageNumber, pageSize);
    }

    @Override
    public OrderWithProducts getOrder(String orderID, String employeeID){
        log.info("getOrderDetail(orderID={}, employeeID={})", orderID, employeeID);
        return orderService.getEmployeeOrder(orderID, employeeID);
    }
}
