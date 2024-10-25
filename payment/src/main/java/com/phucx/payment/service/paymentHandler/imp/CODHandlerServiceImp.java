package com.phucx.payment.service.paymentHandler.imp;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.constant.NotificationBroadCast;
import com.phucx.constant.NotificationStatus;
import com.phucx.constant.NotificationTitle;
import com.phucx.constant.NotificationTopic;
import com.phucx.model.OrderNotificationDTO;
import com.phucx.model.PaymentDTO;
import com.phucx.payment.constant.PaymentStatusConstant;
import com.phucx.payment.exception.PaymentNotFoundException;
import com.phucx.payment.model.Customer;
import com.phucx.payment.model.Payment;
import com.phucx.payment.service.customer.CustomerService;
import com.phucx.payment.service.notification.NotificationService;
import com.phucx.payment.service.payment.PaymentManagementService;
import com.phucx.payment.service.paymentHandler.CODHandlerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CODHandlerServiceImp implements CODHandlerService{
    @Autowired
    private PaymentManagementService paymentManagementService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private CustomerService customerService;


    @Override
    public Payment createPayment(PaymentDTO paymentDTO) {
        log.info("createPayment(paymentDTO={})", paymentDTO);
        try {
            // create payment details
            String paymentID = UUID.randomUUID().toString();
            LocalDateTime createdDateTime = LocalDateTime.now();
            String state = PaymentStatusConstant.PENDING.name().toLowerCase();
            String method = paymentDTO.getMethod();
    
            Payment payment = new Payment(paymentID, createdDateTime, paymentDTO.getAmount(), 
                state, paymentDTO.getCustomerID(), paymentDTO.getOrderID());
            // save payment
            Boolean result = paymentManagementService.savePayment(paymentID, createdDateTime, 
                paymentDTO.getAmount(), state, paymentDTO.getCustomerID(), paymentDTO.getOrderID(), method);
    
            Customer fetchedCustomer = this.customerService.getCustomerByID(paymentDTO.getCustomerID());
            this.sendNotification(paymentDTO.getOrderID(), fetchedCustomer.getUserID());
    
            if(!result){
                throw new RuntimeException("Error while saving payment: "+ paymentDTO.toString());
            }
            return payment;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean paymentSuccessfully(String paymentID) {
        log.info("paymentSuccessfully(paymentID={})", paymentID);
        try {
            String state = PaymentStatusConstant.SUCCESSFUL.name().toLowerCase();
            Boolean result = paymentManagementService.updatePaymentStatus(paymentID, state);
            return result;
        } catch (PaymentNotFoundException e) {
            log.error("Error: {}", e.getMessage());
            return false;
        } 
    }

    @Override
    public Boolean paymentCancelled(String paymentID) {
        log.info("paymentCancelled(paymentID={})", paymentID);
        try {
            String state = PaymentStatusConstant.FAILED.name().toLowerCase();
            Boolean result = paymentManagementService.updatePaymentStatus(paymentID, state);
            return result;
        } catch (PaymentNotFoundException e) {
            log.error("Error: {}", e.getMessage());
            return false;
        } 
    }

    // send notification to users
    private void sendNotification(String orderId, String userId) throws JsonProcessingException{
        // get order
        // OrderDetails order = orderService.getOrder(orderId);
        // CustomerDetail customerDetail = customerService.getCustomerByID(order.getCustomerID());
        // String userId = customerDetail.getUserID();
        // // get order
        // Integer productID = order.getProducts().get(0).getProductID();
        // Product fetchedProduct = productService.getProduct(productID);

        // customer
        // create and save notification back to user
        OrderNotificationDTO customerNotification = new OrderNotificationDTO(
            orderId, NotificationTitle.PLACE_ORDER, 
            userId, NotificationTopic.Order, NotificationStatus.SUCCESSFUL);
        customerNotification.setMessage("Your order #" + orderId + " has been placed successfully");
        // customerNotification.setPicture(fetchedProduct.getPicture());
        // send message back to user
        customerNotification.setFirstNotification(true);
        notificationService.sendCustomerOrderNotification(customerNotification);
        // employee
        // send message to notification message queue
        OrderNotificationDTO employeeNotification = new OrderNotificationDTO(
            orderId, NotificationTitle.PLACE_ORDER, 
            userId, NotificationBroadCast.ALL_EMPLOYEES.name(), 
            NotificationTopic.Order, NotificationStatus.SUCCESSFUL);
        // employeeNotification.setPicture(fetchedProduct.getPicture());
        employeeNotification.setMessage("A new order #" + orderId + " has been placed");

        notificationService.sendEmployeeOrderNotification(employeeNotification);
    }
    
}
