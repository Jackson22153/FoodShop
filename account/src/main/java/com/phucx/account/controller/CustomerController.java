package com.phucx.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.constant.OrderStatus;
import com.phucx.account.constant.WebConstant;
import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.Customer;
import com.phucx.account.model.InvoiceDTO;
import com.phucx.account.model.Notification;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderItem;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.customer.CustomerService;
import com.phucx.account.service.discount.DiscountService;
import com.phucx.account.service.user.UserService;


@RestController
@RequestMapping("customer")
public class CustomerController {
    private Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
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
        logger.info("getOrders(pageNumber={}, type=${})", pageNumber, orderStatus);
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
            pageNumber, WebConstant.PAGE_SIZE, customer.getCustomerID(), status);
        return ResponseEntity.ok().body(orders);
    }

    // notification
    @GetMapping("/notifications")
    public ResponseEntity<Page<Notification>> getNotificationByReceiverID(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        Authentication authentication
    ){
        pageNumber=pageNumber!=null?pageNumber:0;
        Page<Notification> notifications = customerService.getNotifications(
            authentication.getName(), pageNumber, WebConstant.NOTIFICATION_PAGE_SIZE);
        return ResponseEntity.ok().body(notifications);
    }
    @PostMapping("/notifications")
    public ResponseEntity<ResponseFormat> turnOffNotification(
        @RequestBody Notification notification, Authentication authentication
    ){
        Boolean status = customerService.turnOffNotification(
            notification.getNotificationID(), authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }
}
