package com.phucx.account.controller;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.annotations.LoggerAspect;
import com.phucx.account.config.WebSocketConfig;
import com.phucx.account.constant.Notification;
import com.phucx.account.constant.OrderStatus;
import com.phucx.account.constant.WebConstant;
import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.Customer;
import com.phucx.account.model.InvoiceDTO;
import com.phucx.account.model.NotificationMessage;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderItem;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.customer.CustomerService;
import com.phucx.account.service.discount.DiscountService;
import com.phucx.account.service.messageQueue.sender.MessageSender;
import com.phucx.account.service.user.UserService;

import jakarta.ws.rs.NotFoundException;


@RestController
@RequestMapping("customer")
public class CustomerController {
    private Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private DiscountService discountService;
    // GET CUSTOMER'S INFOMATION
    @GetMapping("info")
    public ResponseEntity<CustomerDetail> getUserInfo(Authentication authentication){
        String username = userService.getUsername(authentication);
        logger.info("username: {}", username);
        CustomerDetail customer = customerService.getCustomerDetail(username);
        return ResponseEntity.ok().body(customer);
    }
    // UPDATE CUSTOMER'S INFOMATION
    @PostMapping("info")
    public ResponseEntity<ResponseFormat> updateUserInfo(
        Authentication authentication,
        @RequestBody CustomerDetail customer
    ){
        boolean check = customerService.updateCustomerInfo(customer);
        return ResponseEntity.ok().body(new ResponseFormat(check));
    }

    @GetMapping("/discount/validate")
    public ResponseEntity<ResponseFormat> validateDiscount(
        @RequestBody OrderItem orderItem) throws InvalidDiscountException
    {
        Boolean status = discountService.validateDiscountsOfProduct(orderItem);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }
    // get INVOICE of customer
    @GetMapping("/orders/{orderID}")
    public ResponseEntity<InvoiceDTO> getOrderDetail(
        @PathVariable Integer orderID, Authentication authentication
    ) throws InvalidOrderException{    
        String username = userService.getUsername(authentication);
        Customer customer = customerService.getCustomerByUsername(username);
        InvoiceDTO order = customerService.getInvoice(orderID, customer.getCustomerID());
        return ResponseEntity.ok().body(order);
    }
    // GET ALL ORDERS OF CUSTOMER
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderDetailsDTO>> getOrders(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        @RequestParam(name = "type", required = false) String orderStatus,
        Authentication authentication
    ){    
        logger.info("OrderStatus: {}", orderStatus);
        pageNumber = pageNumber!=null?pageNumber:0;
        String username = userService.getUsername(authentication);
        Customer customer = customerService.getCustomerByUsername(username);
        OrderStatus status = null;
        if(orderStatus==null){
            status = OrderStatus.All;
        }else {
            status = OrderStatus.fromString(orderStatus.toUpperCase());
        }
        Page<OrderDetailsDTO> orders = customerService.getOrders(
            pageNumber, WebConstant.PAGE_SIZE, 
            customer.getCustomerID(), 
            status);
        return ResponseEntity.ok().body(orders);
    }

    // ENDPOINT TO PLACE AN ORDER
    @LoggerAspect
    @MessageMapping("/placeOrder")
    @SendTo("/topic/order")
    public OrderWithProducts placeOrder(Authentication authentication, @RequestBody OrderWithProducts order
    ) throws InvalidDiscountException, NotFoundException, RuntimeException, SQLException, InvalidOrderException{
        String username = userService.getUsername(authentication);
        Customer customer = customerService.getCustomerByUsername(username);
        logger.info("Customer {} has placed an order: {}",username, order.toString());
        if(customer!=null){
            order.setCustomerID(customer.getCustomerID());
            OrderWithProducts createdOrder = customerService.placeOrder(order);
            if(createdOrder !=null){
                // notificate back to user
                String userID = customer.getUser().getUserID();
                NotificationMessage notification = new NotificationMessage(
                    "Your order have been placed successfully", Notification.SUCCESS); 
                messageSender.sendMessageToUser(userID, notification);
                return createdOrder;
            }
            throw new RuntimeException("Error when placing an order");
        }
        throw new NotFoundException("Customer is not found");
    }

    // HANDLE MESSAGE EXCEPTION
    @MessageExceptionHandler(value = SQLException.class)
    public void handleSqlMessageException(Authentication authentication){
        String username = userService.getUsername(authentication);
        Customer customer = customerService.getCustomerByUsername(username);
        NotificationMessage notificationMessage = new NotificationMessage(
            "Error during processing order", Notification.CANCEL);
        simpMessagingTemplate.convertAndSendToUser(customer.getCustomerID(), 
            WebSocketConfig.QUEUE_MESSAGES, notificationMessage);
    }
    @MessageExceptionHandler(value = InvalidDiscountException.class)
    public void handleInvalidDiscountMessageException(Authentication authentication, Exception exception){
        String username = userService.getUsername(authentication);
        Customer customer = customerService.getCustomerByUsername(username);
        logger.info(exception.getMessage());
        NotificationMessage notificationMessage = new NotificationMessage(
            "Invalid discount", Notification.CANCEL);
        simpMessagingTemplate.convertAndSendToUser(customer.getCustomerID(), 
            WebSocketConfig.QUEUE_MESSAGES, notificationMessage);
    }

    @MessageExceptionHandler(value = InvalidOrderException.class)
    public void handleInvalidOrderMessageException(Authentication authentication){
        String username = userService.getUsername(authentication);
        Customer customer = customerService.getCustomerByUsername(username);
        NotificationMessage notificationMessage = new NotificationMessage(
            "Invalid order", Notification.CANCEL);
        simpMessagingTemplate.convertAndSendToUser(customer.getCustomerID(), 
            WebSocketConfig.QUEUE_MESSAGES, notificationMessage);
    }

    @MessageExceptionHandler(value = RuntimeException.class)
    public void handleRuntimeMessageException(Authentication authentication, Exception exception){
        String username = userService.getUsername(authentication);
        Customer customer = customerService.getCustomerByUsername(username);
        NotificationMessage notificationMessage = new NotificationMessage(
            exception.getMessage(), Notification.ERROR);
        simpMessagingTemplate.convertAndSendToUser(customer.getCustomerID(), 
            WebSocketConfig.QUEUE_MESSAGES, notificationMessage);
    }

    // @MessageExceptionHandler(value = Exception.class)
    // public void handleUndefinedMessageException(Authentication authentication){
    //     String username = userService.getUsername(authentication);
    //     Customer customer = customerService.getCustomerByUsername(username);
    //     NotificationMessage notificationMessage = new NotificationMessage(
    //         "Invalid order", Notification.CANCEL);
    //     simpMessagingTemplate.convertAndSendToUser(customer.getCustomerID(), 
    //         WebSocketConfig.QUEUE_MESSAGES, notificationMessage);
    // }
}
