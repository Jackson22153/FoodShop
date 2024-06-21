package com.phucx.order.service.order.imp;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.NotificationBroadCast;
import com.phucx.order.constant.NotificationStatus;
import com.phucx.order.constant.NotificationTopic;
import com.phucx.order.constant.NotificationTitle;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.Customer;
import com.phucx.order.model.InvoiceDetails;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderItem;
import com.phucx.order.model.OrderItemDiscount;
import com.phucx.order.model.OrderNotificationDTO;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.User;
import com.phucx.order.service.customer.CustomerService;
import com.phucx.order.service.notification.NotificationService;
import com.phucx.order.service.order.CustomerOrderService;
import com.phucx.order.service.order.OrderService;
import com.phucx.order.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerOrderServiceImp implements CustomerOrderService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    
    @Override
    public OrderDetails placeOrder(OrderWithProducts order, String userID) 
        throws JsonProcessingException, InvalidDiscountException, InvalidOrderException, NotFoundException {
        log.info("placeOrder(order={}, userID={})", order, userID);
        // fetch customer
        Customer customer = customerService.getCustomerByUserID(userID);
        order.setCustomerID(customer.getCustomerID());
        // process order
        OrderWithProducts newOrder = this.orderProcessing(order);
        // create a notification 
        if(newOrder ==null){
            throw new RuntimeException("Error when placing an order");
        }

        // customer
        // create and save notification back to user
        OrderNotificationDTO customerNotification = new OrderNotificationDTO(
            newOrder.getOrderID(), NotificationTitle.PLACE_ORDER, 
            userID, NotificationTopic.Order, NotificationStatus.SUCCESSFUL);
        customerNotification.setMessage("Your order #" + newOrder.getOrderID() + " has been placed successfully");
        // send message back to user
        notificationService.sendNotification(customerNotification);

        // employee
        // send message to notification message queue
        OrderNotificationDTO employeeNotification = new OrderNotificationDTO(
            newOrder.getOrderID(), NotificationTitle.PLACE_ORDER, 
            userID, NotificationBroadCast.ALL_EMPLOYEES.name(), 
            NotificationTopic.Order, NotificationStatus.SUCCESSFUL);
        employeeNotification.setMessage("A new order #" + newOrder.getOrderID() + " has been placed");
        notificationService.sendNotification(employeeNotification);

        return orderService.getOrder(newOrder.getOrderID(), OrderStatus.Pending);
    }

        // order processing
    // validating and saving customer's order 
    private OrderWithProducts orderProcessing(OrderWithProducts order) 
    throws JsonProcessingException, InvalidDiscountException, InvalidOrderException, NotFoundException {
        log.info("orderProcessing({})", order);
        if(order.getCustomerID()==null){
            throw new NotFoundException("Customer does not found");
        }
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
        if(!isValidOrder) throw new InvalidOrderException("Order of customer "+order.getCustomerID()+" is not valid");
        // save order
        String pendingOrderID = orderService.saveFullOrder(order);
        order.setOrderID(pendingOrderID);
        return order;
    }

    @Override
    public void receiveOrder(OrderWithProducts order) throws JsonProcessingException, NotFoundException {
        log.info("receiveOrder(orderID={})", order.getOrderID());
        // get order
        OrderDetails orderDetails = orderService.getOrder(order.getOrderID(), OrderStatus.Shipping);
        // get user 
        User employeeUser = userService.getUserByEmployeeID(orderDetails.getEmployeeID());
        // update order's status
        Boolean status = orderService.updateOrderStatus(orderDetails.getOrderID(), OrderStatus.Successful);
        // notification
        OrderNotificationDTO notification = new OrderNotificationDTO();
        notification.setTitle(NotificationTitle.RECEIVE_ORDER);
        notification.setTopic(NotificationTopic.Order);
        notification.setOrderID(orderDetails.getOrderID());
        if(status){
            notification.setMessage("Order #" + orderDetails.getOrderID() + " is received successully by customer " + orderDetails.getCustomerID());
            notification.setStatus(NotificationStatus.SUCCESSFUL);
            notification.setReceiverID(employeeUser.getUserID());
        }else {
            notification.setMessage("Order #" + orderDetails.getOrderID() + " can not received by customer " + orderDetails.getCustomerID());
            notification.setStatus(NotificationStatus.ERROR);
            notification.setReceiverID(employeeUser.getUserID());
        }
        notificationService.sendNotification(notification);
    }

    @Override
    public Page<OrderDetails> getOrders(int pageNumber, int pageSize, String userID, OrderStatus orderStatus)
            throws JsonProcessingException, NotFoundException {
        log.info("getOrders(pageNumber={}, pageSize={}, userID={}, orderStatus={})", pageNumber, pageSize, userID, orderStatus);
        // fetch customer
        Customer fetchedCustomer = customerService.getCustomerByUserID(userID);
        Page<OrderDetails> orders = null;
        if(orderStatus.equals(OrderStatus.All)){
            orders = orderService.getOrdersByCustomerID(
                fetchedCustomer.getCustomerID(), pageNumber, pageSize);
        }else{
            orders = orderService.getOrdersByCustomerID(
                fetchedCustomer.getCustomerID(), orderStatus, pageNumber, pageSize);
        }
        return orders;
    }

    @Override
    public InvoiceDetails getInvoice(String orderID, String userID) throws JsonProcessingException, NotFoundException {
        log.info("getInvoice(orderID={}, userID={})", orderID, userID);
        // fetch customer
        Customer fetchedCustomer = customerService.getCustomerByUserID(userID);
        InvoiceDetails invoice = orderService.getInvoiceByCustomerID(fetchedCustomer.getCustomerID(), orderID);
        return invoice;
    }
    
}
