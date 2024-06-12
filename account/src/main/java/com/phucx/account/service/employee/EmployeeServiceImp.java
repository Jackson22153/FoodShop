package com.phucx.account.service.employee;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.phucx.account.constant.WebConstant;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.EmployeeDetails;
import com.phucx.account.model.Employee;
import com.phucx.account.model.User;
import com.phucx.account.model.UserInfo;
import com.phucx.account.repository.EmployeeAccountRepository;
import com.phucx.account.repository.EmployeeDetailRepostiory;
import com.phucx.account.repository.EmployeeRepository;
import com.phucx.account.service.image.ImageService;
import com.phucx.account.service.user.UserService;

import jakarta.ws.rs.NotFoundException;
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
    private ImageService imageService;

	@Override
	public EmployeeDetails getEmployeeByID(String employeeID) {
        log.info("getEmployeeByID(employeeID={})", employeeID);
        Employee employee = employeeRepository.findById(employeeID)
            .orElseThrow(()-> new NotFoundException("Employee " + employeeID + " does not found"));
        UserInfo user = userService.getUserInfo(employee.getUserID());
        
        imageService.setEmployeeImage(employee);

        EmployeeDetails employeeDetails = new EmployeeDetails(
            employee.getEmployeeID(), user, employee.getFirstName(), employee.getLastName(), 
            employee.getBirthDate(), employee.getHireDate(), employee.getHomePhone(), 
            employee.getPhoto(), employee.getTitle(), employee.getAddress(), employee.getCity(), 
            employee.getNotes());

        return employeeDetails;
    }

	@Override
	public Boolean updateEmployeeInfo(EmployeeDetail employee) {
        log.info("updateEmployeeInfo({})", employee.toString());
        Employee fetchedEmployee =employeeRepository.findById(employee.getEmployeeID())
            .orElseThrow(()-> new NotFoundException("Employee " + employee.getEmployeeID() + " does not found"));
        
        String picture = imageService.getImageName(employee.getPhoto());
        
        // update employee info
        Boolean status = employeeDetailRepostiory.updateEmployeeInfo(
            fetchedEmployee.getEmployeeID(), employee.getEmail(), employee.getFirstName(), 
            employee.getLastName(), employee.getBirthDate(), employee.getAddress(), 
            employee.getCity(), employee.getHomePhone(), picture);   

        return status;
	}

	@Override
	public Page<EmployeeAccount> getAllEmployees(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findAll(pageable);
        imageService.setEmployeeAccountImage(employees.getContent());
        return employees;
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
            imageService.setEmployeeDetailImage(employee);
            return employee;
        }else{
            String employeeID = UUID.randomUUID().toString();
            employeeAccountRepository.createEmployeeInfo(employeeID, username, username, username);
            EmployeeDetail employee = employeeDetailRepostiory.findById(employeeID)
                .orElseThrow(()-> new NotFoundException("EmployeeID: " + employeeID + " does not found"));
            imageService.setEmployeeDetailImage(employee);
            return employee;
        }
	}

    @Override
    public Page<EmployeeAccount> searchEmployeesByEmployeeID(String employeeID, int pageNumber, int pageSize) {
        String searchParam = "%" + employeeID +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByEmployeeIDLike(searchParam, page);
        imageService.setEmployeeAccountImage(employees.getContent());
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByFirstName(String firstName, int pageNumber, int pageSize) {
        String searchParam = "%" + firstName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByFirstNameLike(searchParam, page);
        imageService.setEmployeeAccountImage(employees.getContent());
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByLastName(String lastName, int pageNumber, int pageSize) {
        String searchParam = "%" + lastName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByLastNameLike(searchParam, page);
        imageService.setEmployeeAccountImage(employees.getContent());
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByUsername(String username, int pageNumber, int pageSize) {
        String searchParam = "%" + username +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByUsernameLike(searchParam, page);
        imageService.setEmployeeAccountImage(employees.getContent());
        return employees;
    }

    @Override
    public Page<EmployeeAccount> searchEmployeesByEmail(String email, int pageNumber, int pageSize) {
        String searchParam = "%" + email +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeAccount> employees = employeeAccountRepository.findByEmailLike(searchParam, page);
        imageService.setEmployeeAccountImage(employees.getContent());
        return employees;
    }

    @Override
    public Boolean updateAdminEmployeeInfo(Employee employee) {
        log.info("updateAdminEmployeeInfo({})", employee.toString());
        Employee fetchedEmployee = employeeRepository.findById(employee.getEmployeeID())
            .orElseThrow(()-> new NotFoundException("Employee " + employee.getEmployeeID() + " does not found"));

        String picture = this.imageService.getImageName(employee.getPhoto());

        Boolean status = employeeRepository.updateAdminEmployeeInfo(
            fetchedEmployee.getEmployeeID(), employee.getFirstName(), employee.getLastName(), 
            employee.getHireDate(), picture, employee.getNotes());
        return status;
    }

    @Override
    public Employee getEmployee(String employeeID) {
        Employee employee = employeeRepository.findById(employeeID)
            .orElseThrow(()-> new NotFoundException("Employee " + employeeID + " does not found"));
        imageService.setEmployeeImage(employee);
        return employee;
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
            passwordEncoder.encode(WebConstant.DEFUALT_PASSWORD), 
            employeeAccount.getEmail(), 
            employeeID, employeeAccount.getFirstName(), 
            employeeAccount.getLastName());
    }

    @Override
    public Employee getEmployeeByUserID(String userID) {
        Employee fetchedEmployee = employeeRepository.findByUserID(userID)
            .orElseThrow(()-> new NotFoundException("Employee with UserID: " + userID + " does not found"));
        return imageService.setEmployeeImage(fetchedEmployee);
    }
}
