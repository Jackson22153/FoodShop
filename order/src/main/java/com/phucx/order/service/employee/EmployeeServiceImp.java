package com.phucx.order.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.phucx.order.constant.NotificationStatus;
import com.phucx.order.constant.NotificationTopic;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.EmployeeAccount;
import com.phucx.order.model.Notification;
import com.phucx.order.model.Employee;
import com.phucx.order.model.OrderDetailsDTO;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Topic;
import com.phucx.order.repository.EmployeeAccountRepository;
import com.phucx.order.repository.EmployeeRepository;
import com.phucx.order.service.messageQueue.sender.MessageSender;
import com.phucx.order.service.notification.NotificationService;
import com.phucx.order.service.order.OrderService;
import com.phucx.order.service.user.UserService;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImp implements EmployeeService {
    @Autowired
    private NotificationService notificationService;
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
    public EmployeeAccount getEmployeeAccount(String username) {
        return employeeAccountRepository.findByUsername(username)
            .orElseThrow(()-> new NotFoundException("Employee username "+ username +" does not found"));
    }
}
