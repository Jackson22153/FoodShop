package com.phucx.account.service.employee;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.phucx.account.constant.NotificationStatus;
import com.phucx.account.constant.NotificationTopic;
import com.phucx.account.constant.OrderStatus;
import com.phucx.account.constant.WebConstant;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.model.EmployeeDetailDTO;
import com.phucx.account.model.Notification;
import com.phucx.account.model.Employee;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.Topic;
import com.phucx.account.model.User;
import com.phucx.account.model.UserInfo;
import com.phucx.account.repository.EmployeeAccountRepository;
import com.phucx.account.repository.EmployeeDetailRepostiory;
import com.phucx.account.repository.EmployeeRepository;
import com.phucx.account.service.messageQueue.sender.MessageSender;
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
    private MessageSender messageSender;
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
    public Page<OrderDetailsDTO> getOrders(int pageNumber, int pageSize, String employeeID, OrderStatus status) {
        Page<OrderDetailsDTO> pendingOrders = new PageImpl<>(new ArrayList<>());
        if(status.equals(OrderStatus.All)){
            pendingOrders = orderService.getEmployeeOrders(pageNumber, pageSize, employeeID);
        }else {
            pendingOrders = orderService.getEmployeeOrders(pageNumber, pageSize, employeeID, status);
        }
        return pendingOrders;
    }

    @Override
    public Page<OrderDetailsDTO> getPendingOrders(int pageNumber, int pageSize) {
        return orderService.getPendingOrders(pageNumber, pageSize);
    }

    @Override
    public OrderWithProducts getOrderDetail(Integer orderID, String employeeID) throws InvalidOrderException {
        OrderWithProducts order = orderService.getEmployeeOrderDetail(orderID, employeeID);
        log.info("order: {}", order.toString());
        return order;
    }

    @Override
    public OrderWithProducts getPendingOrderDetail(int orderID) throws InvalidOrderException {
        return orderService.getPendingOrderDetail(orderID);
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

    // PROCESSING ORDER
    @Override
    public Notification confirmOrder(OrderWithProducts order, String employeeID) throws InvalidOrderException {
        OrderWithProducts orderWithProducts = orderService.getPendingOrderDetail(order.getOrderID());
        orderWithProducts.setEmployeeID(employeeID);
        // send order for server to validate it
        Notification response = messageSender.sendAndReceiveOrder(orderWithProducts);
        return response;
    }

    @Override
    public Notification cancelOrder(OrderWithProducts order, String employeeID) {
        // fetch pending order
        OrderDetailsDTO orderDetail = orderService.getOrder(
            order.getOrderID(), OrderStatus.Pending);
        Boolean check = orderService.updateOrderEmployee(order.getOrderID(), employeeID);
        if(!check) throw new RuntimeException("Order #" + order.getOrderID() + " can not be updated");
        // update order status as canceled
        Boolean status = orderService.updateOrderStatus(orderDetail.getOrderID(), OrderStatus.Canceled);
        // notification
        Notification notification = new Notification();
        notification.setTitle("Cancel Order");
        notification.setTopic(new Topic(NotificationTopic.Order.name()));
        if(status){
            String userID = userService.getUserIdOfCustomerID(order.getCustomerID());
            notification.setReceiverID(userID);
            notification.setMessage("Order #" + order.getOrderID() + " has been canceled successfully");
            notification.setStatus(NotificationStatus.SUCCESSFUL.name());
        }else {
            String userID = userService.getUserIdOfEmployeeID(employeeID);
            notification.setReceiverID(userID);
            notification.setMessage("Order #" + order.getOrderID() + " can not be canceled");
            notification.setStatus(NotificationStatus.ERROR.name());
        }
        return notification;
    }

    @Override
    public Notification fulfillOrder(OrderWithProducts order) {
        log.info("fulfillOrder(orderID={})", order.getOrderID());
        OrderDetailsDTO fetchedOrder = orderService.getOrder(order.getOrderID(), OrderStatus.Confirmed);
        Boolean status = orderService.updateOrderStatus(fetchedOrder.getOrderID(), OrderStatus.Shipping);
        // notification
        Notification notification = new Notification();
        notification.setTitle("Fulfill Order");
        notification.setTopic(new Topic(NotificationTopic.Order.name()));
        if(status){
            String userID = userService.getUserIdOfCustomerID(order.getCustomerID());
            notification.setReceiverID(userID);
            notification.setMessage("Order #" + order.getOrderID() + " has been fulfilled");
            notification.setStatus(NotificationStatus.SUCCESSFUL.name());
        }else {
            String userID = userService.getUserIdOfEmployeeID(order.getEmployeeID());
            notification.setReceiverID(userID);
            notification.setMessage("Order #" + order.getOrderID() + " can not be fulfilled");
            notification.setStatus(NotificationStatus.ERROR.name());
        }
        return notification;
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
}
