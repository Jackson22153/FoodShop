package com.phucx.account.service.customer;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.phucx.account.constant.NotificationStatus;
import com.phucx.account.constant.NotificationTopic;
import com.phucx.account.constant.OrderStatus;
import com.phucx.account.constant.WebConstant;
import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.CustomerDetailDTO;
import com.phucx.account.model.Customer;
import com.phucx.account.model.InvoiceDTO;
import com.phucx.account.model.Notification;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderItem;
import com.phucx.account.model.OrderItemDiscount;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.Topic;
import com.phucx.account.model.User;
import com.phucx.account.model.UserInfo;
import com.phucx.account.model.Order;
import com.phucx.account.repository.CustomerAccountRepository;
import com.phucx.account.repository.CustomerDetailRepository;
import com.phucx.account.repository.CustomerRepository;
import com.phucx.account.service.github.GithubService;
import com.phucx.account.service.notification.NotificationService;
import com.phucx.account.service.order.OrderService;
import com.phucx.account.service.user.UserService;

import jakarta.ws.rs.NotFoundException;


@Service
public class CustomerServiceImp implements CustomerService {
    private Logger logger = LoggerFactory.getLogger(CustomerServiceImp.class);
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private GithubService githubService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerAccountRepository customerAccountRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerDetailRepository customerDetailRepository;

	@Override
	public boolean updateCustomerInfo(CustomerDetail customer) {
        // return false;
        logger.info("updateCustomerInfo({})", customer.toString());
        Customer fetchedCustomer = customerRepository.findById(customer.getCustomerID())
            .orElseThrow(()->new NotFoundException("Customer " + customer.getCustomerID() + " does not found"));
        // Boolean result = false;
        Boolean result = customerDetailRepository.updateCustomerInfo(fetchedCustomer.getCustomerID(), customer.getEmail(),
            customer.getContactName(), customer.getAddress(), customer.getCity(), customer.getPhone(),
            customer.getPicture());
            logger.info("Result: {}", result);
        return result;
	}
	@Override
	public CustomerDetail getCustomerDetail(String username) {
        User user = userService.getUser(username);
        Optional<CustomerAccount> customerAccOp = customerAccountRepository.findByUserID(user.getUserID());
        if(customerAccOp.isPresent()){
            CustomerAccount customerAcc = customerAccOp.get();
            String customerID = customerAcc.getCustomerID();
            CustomerDetail customer = customerDetailRepository.findById(customerID)
                .orElseThrow(()-> new NotFoundException("CustomerID: " + customerID + " does not found"));
            return customer;
        }else{
            String customerID = UUID.randomUUID().toString();
            customerAccountRepository.createCustomerInfo(customerID, username, username);
            CustomerDetail customer = customerDetailRepository.findById(customerID)
                .orElseThrow(()-> new NotFoundException("CustomerID: " + customerID + " does not found"));
            return customer;
        }
    }
    
    @Override
    public boolean addNewCustomer(CustomerAccount customer){
        logger.info("addNewCustomer({})", customer);
        if(customer.getUsername() == null || customer.getEmail()==null || customer.getContactName()==null)
            throw new RuntimeException("Missing some customer's information");
        CustomerAccount fetchedCustomer= customerAccountRepository.findByUsername(customer.getUsername());
        if(fetchedCustomer!=null) throw new RuntimeException("Customer " + customer.getUsername() + " already exists");
        var fetchedCustomerByEmail= customerAccountRepository.findByEmail(customer.getEmail());
        if(fetchedCustomerByEmail.isPresent()) throw new RuntimeException("Customer " + customer.getEmail() + " already exists");
        // add new customer
        String userID = UUID.randomUUID().toString();
        String customerID = UUID.randomUUID().toString();
        return customerAccountRepository.addNewCustomer(
            userID, customer.getUsername(), 
            passwordEncoder.encode( WebConstant.DEFUALT_PASSWORD), 
            customer.getEmail(), customerID, 
            customer.getContactName());
    }
	
    @Override
	public Page<CustomerAccount> getAllCustomers(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> result = customerAccountRepository.findAll(page);
		return result;
	}
	@Override
	public Customer getCustomerByID(String customerID) {
		Customer customer = customerRepository.findById(customerID)
            .orElseThrow(()-> new NotFoundException("Customer " + customerID + " does not found"));
        return customer;
	}
    @Override
    public OrderWithProducts placeOrder(OrderWithProducts order) 
    throws InvalidDiscountException, InvalidOrderException, NotFoundException, SQLException, RuntimeException{
        logger.info("placeOrder({})", order);
        if(order.getCustomerID()!=null){
            LocalDateTime currenDateTime = LocalDateTime.now();
            order.setOrderDate(currenDateTime);
            // set applieddate for discount;
            for (OrderItem product : order.getProducts()) {
                for(OrderItemDiscount discount : product.getDiscounts()){
                    discount.setAppliedDate(currenDateTime);
                }
            }

            // validate order
            boolean isValidOrder = orderService.validateOrder(order);
            if(!isValidOrder) throw new InvalidOrderException("Order is not valid");
            // save order
            Order pendingOrder = orderService.saveFullOrder(order);
            order.setOrderID(pendingOrder.getOrderID());
            return order;
        }
        throw new NotFoundException("Customer is not found");
    }
    @Override
    public Notification receiveOrder(OrderWithProducts order) {
        logger.info("receiveOrder(orderID={})", order.getOrderID());
        
        OrderDetailsDTO orderDetailsDTO = orderService.getOrder(order.getOrderID(), OrderStatus.Shipping);
        Boolean status = orderService.updateOrderStatus(orderDetailsDTO.getOrderID(), OrderStatus.Successful);
        // notification
        Notification notification = new Notification();
        notification.setTitle("Receive Order");
        notification.setTopic(new Topic(NotificationTopic.Order.name()));
        logger.info("status: {}", status);
        if(status){
            logger.info("ssnotification: {}", notification);
            notification.setMessage("Order #" + orderDetailsDTO.getOrderID() + " is received successully by customer " + orderDetailsDTO.getCustomerID());
            notification.setStatus(NotificationStatus.SUCCESSFUL.name());
            notification.setReceiverID(userService.getUserIdOfEmployeeID(orderDetailsDTO.getEmployeeID()));
            logger.info("ssnotification: {}", notification);
        }else {
            notification.setMessage("Order #" + orderDetailsDTO.getOrderID() + " can not received by customer " + orderDetailsDTO.getCustomerID());
            notification.setStatus(NotificationStatus.ERROR.name());
            notification.setReceiverID(userService.getUserIdOfEmployeeID(orderDetailsDTO.getEmployeeID()));
        }
        logger.info("notification: {}", notification);
        return notification;
    }
    
    @Override
    public Customer getCustomerByUsername(String username) {
        CustomerAccount customerAccount = customerAccountRepository.findByUsername(username);
        if(customerAccount!=null){
            String customerID = customerAccount.getCustomerID();
            Customer customer = customerRepository.findById(customerID)
                .orElseThrow(()-> new NotFoundException("Customer: " + customerID + " does not found"));
            return customer;
        }else throw new NotFoundException(username + "does not found");
    }
    @Override
    public Page<OrderDetailsDTO> getOrders(int pageNumber, int pageSize, String customerID, OrderStatus orderStatus) {
        Page<OrderDetailsDTO> orders = null;
        if(orderStatus.equals(OrderStatus.All)) {
            orders = orderService.getCustomerOrders(pageNumber, pageSize, customerID);
        }else {
            orders = orderService.getCustomerOrders(pageNumber, pageSize, customerID, orderStatus);
        }
        return orders;
    }
    @Override
    public InvoiceDTO getInvoice(int orderID, String customerID) throws InvalidOrderException {
        return orderService.getCustomerInvoice(orderID, customerID);
    }
    @Override
    public Page<CustomerAccount> searchCustomersByCustomerID(String customerID, int pageNumber, int pageSize) {
        String searchParam = "%" + customerID +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> customers = customerAccountRepository.findByCustomerIDLike(searchParam, page);
        return customers;
    }
    @Override
    public Page<CustomerAccount> searchCustomersByContactName(String contactName, int pageNumber, int pageSize) {
        String searchParam = "%" + contactName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> customers = customerAccountRepository.findByContactNameLike(searchParam, page);
        return customers;
    }
    @Override
    public Page<CustomerAccount> searchCustomersByUsername(String username, int pageNumber, int pageSize) {
        String searchParam = "%" + username +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> customers = customerAccountRepository.findByUsernameLike(searchParam, page);
        return customers;
    }
    @Override
    public Page<CustomerAccount> searchCustomersByEmail(String email, int pageNumber, int pageSize) {
        String searchParam = "%" + email +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> customers = customerAccountRepository.findByEmailLike(searchParam, page);
        return customers;
    }
    @Override
    public CustomerDetailDTO getCustomerDetailByCustomerID(String customerID) {
        Customer customer = customerRepository.findById(customerID)
            .orElseThrow(()-> new NotFoundException("Customer " + customerID + " does not found"));
        UserInfo user = userService.getUserInfo(customer.getUser().getUserID());
        
        CustomerDetailDTO customerDetail = new CustomerDetailDTO(
            customer.getCustomerID(), customer.getContactName(), customer.getPicture(), user);
        return customerDetail;
    }
    @Override
    public Page<Notification> getNotifications(String userID, int pageNumber, int pageSize) {
        return notificationService.getNotificationsByReceiverID(userID, pageNumber, pageSize);
    }
    @Override
    public Boolean turnOffNotification(String notificationID, String userID) {
        Notification notification = notificationService
            .getNotificationByUserIDAndNotificationID(userID, notificationID);
        return notificationService.updateNotificationActive(
            notification.getNotificationID(), false);
    }
}
