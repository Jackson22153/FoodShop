package com.phucx.payment.service.paymentHandler.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.constant.NotificationBroadCast;
import com.phucx.constant.NotificationStatus;
import com.phucx.constant.NotificationTitle;
import com.phucx.constant.NotificationTopic;
import com.phucx.model.OrderNotificationDTO;
import com.phucx.payment.constant.PaymentStatusConstant;
import com.phucx.payment.exception.NotFoundException;
import com.phucx.payment.model.Customer;
import com.phucx.payment.model.OrderDetails;
import com.phucx.payment.model.Product;
import com.phucx.payment.service.customer.CustomerService;
import com.phucx.payment.service.notification.NotificationService;
import com.phucx.payment.service.order.OrderService;
import com.phucx.payment.service.payment.PaymentManagementService;
import com.phucx.payment.service.paymentHandler.PaymentHandlerService;
import com.phucx.payment.service.product.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentHandlerServiceImp implements PaymentHandlerService{
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PaymentManagementService paymentManagementService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    
    @Override
    public void paymentSuccessful(String orderId) throws JsonProcessingException {
        log.info("paymentSuccessful(orderId={})", orderId);
        // update payment status as successful
        paymentManagementService.updatePaymentStatusByOrderID(orderId, PaymentStatusConstant.SUCCESSFUL);
        // get order
        OrderDetails order = orderService.getOrder(orderId);
        Customer customerDetail = customerService.getCustomerByID(order.getCustomerID());
        String userId = customerDetail.getUserID();
        // get order
        Integer productID = order.getProducts().get(0).getProductID();
        Product fetchedProduct = productService.getProduct(productID);
        // customer
        // create and save notification back to user
        OrderNotificationDTO customerNotification = new OrderNotificationDTO(
            orderId, NotificationTitle.PLACE_ORDER, 
            userId, NotificationTopic.Order, 
            NotificationStatus.SUCCESSFUL);
        customerNotification.setMessage("Your order #" + orderId + " has been placed successfully");
        customerNotification.setPicture(fetchedProduct.getPicture());
        // send message back to user
        customerNotification.setFirstNotification(true);
        notificationService.sendCustomerOrderNotification(customerNotification);
        // employee
        // send message to notification message queue
        OrderNotificationDTO employeeNotification = new OrderNotificationDTO(
            orderId, NotificationTitle.PLACE_ORDER, 
            userId, NotificationBroadCast.ALL_EMPLOYEES.name(), 
            NotificationTopic.Order, NotificationStatus.SUCCESSFUL);
        employeeNotification.setPicture(fetchedProduct.getPicture());
        employeeNotification.setMessage("A new order #" + orderId + " has been placed");
        
        notificationService.sendEmployeeOrderNotification(employeeNotification);
    }

    @Override
    public void paymentFailed(String orderId) throws JsonProcessingException, NotFoundException {
        log.info("paymentFailed(orderId={})", orderId);
        // update payment status as canceled
        paymentManagementService.updatePaymentStatusByOrderID(orderId, PaymentStatusConstant.CANCELLED);
        // get order
        OrderDetails order = orderService.getOrder(orderId);
        Customer customerDetail = customerService.getCustomerByID(order.getCustomerID());
        String userId = customerDetail.getUserID();
        // update order status
        orderService.updateOrderStatusAsCanceled(orderId);
        // get order
        Integer productID = order.getProducts().get(0).getProductID();
        Product fetchedProduct = productService.getProduct(productID);
        // customer
        // create and save notification back to user
        OrderNotificationDTO customerNotification = new OrderNotificationDTO(
            orderId, NotificationTitle.CANCEL_ORDER, 
            userId, NotificationTopic.Order, 
            NotificationStatus.SUCCESSFUL);
        customerNotification.setMessage("Your order #" + orderId + " has been canceled");
        customerNotification.setPicture(fetchedProduct.getPicture());
        // send message back to user
        customerNotification.setFirstNotification(true);
        notificationService.sendCustomerOrderNotification(customerNotification);

    }    
}
