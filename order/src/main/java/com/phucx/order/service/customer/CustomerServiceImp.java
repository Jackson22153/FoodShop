package com.phucx.order.service.customer;

import java.sql.SQLException;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.phucx.order.constant.NotificationStatus;
import com.phucx.order.constant.NotificationTopic;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.CustomerAccount;
import com.phucx.order.model.Customer;
import com.phucx.order.model.Notification;
import com.phucx.order.model.OrderDetailsDTO;
import com.phucx.order.model.OrderItem;
import com.phucx.order.model.OrderItemDiscount;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Topic;
import com.phucx.order.model.Order;
import com.phucx.order.repository.CustomerAccountRepository;
import com.phucx.order.repository.CustomerRepository;
import com.phucx.order.service.messageQueue.sender.MessageSender;
import com.phucx.order.service.notification.NotificationService;
import com.phucx.order.service.order.OrderService;
import com.phucx.order.service.user.UserService;

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
    private CustomerAccountRepository customerAccountRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MessageSender messageSender;
	
	@Override
	public Customer getCustomerByID(String customerID) {
		Customer customer = customerRepository.findById(customerID)
            .orElseThrow(()-> new NotFoundException("Customer " + customerID + " does not found"));
        return customer;
	}
    // order processing
    // validating and saving customer's order 
    private OrderWithProducts orderProcessing(OrderWithProducts order) 
    throws InvalidDiscountException, InvalidOrderException, NotFoundException, SQLException, RuntimeException{
        logger.info("orderProcessing({})", order);
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
    public OrderDetailsDTO placeOrder(OrderWithProducts order, Authentication authentication) 
    throws InvalidDiscountException, InvalidOrderException, NotFoundException, SQLException, RuntimeException{

        logger.info("placeOrder(userID={}, order{})", authentication.getName(), order.toString());
        CustomerAccount customer = customerAccountRepository.findById(authentication.getName())
            .orElseThrow(()-> new NotFoundException("Customer with userID: "+authentication.getName()+" does not found"));

        order.setCustomerID(customer.getCustomerID());
        OrderWithProducts newOrder = this.orderProcessing(order);
        if(newOrder !=null){
            // create and save notification back to user
            Notification notificationToCustomer = new Notification(
                "Place Order",
                "Order #"+newOrder.getOrderID()+" has been placed successfully",
                null, customer.getUserID(), new Topic(NotificationTopic.Order.name()),
                NotificationStatus.SUCCESSFUL.name(), true);
            // send message back to user
            messageSender.sendNotification(notificationToCustomer);
            // messageSender.sendNotificationToUser(customer.getUserID(), notificationToUser);
            // send message to notification message queue
            Notification notificationToTopic = new Notification(
                "Place Order",
                "Order #" + newOrder.getOrderID() + " has been placed", 
                authentication.getName(), null, new Topic(NotificationTopic.Order.name()), 
                NotificationStatus.SUCCESSFUL.name(), true);
            messageSender.sendNotification(notificationToTopic);
            // 
            return orderService.getOrder(newOrder.getOrderID(), OrderStatus.Pending);
        }
        throw new RuntimeException("Error when placing an order");
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
    public CustomerAccount getCustomerByUsername(String username) {
        return customerAccountRepository.findByUsername(username)
            .orElseThrow(()-> new NotFoundException("Customer with username "+ username +" does not found"));
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
