package com.phucx.order.service.order.imp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.phucx.order.model.Employee;
import com.phucx.order.model.InvoiceDetails;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderItem;
import com.phucx.order.model.OrderItemDiscount;
import com.phucx.order.model.OrderNotificationDTO;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.PaymentDTO;
import com.phucx.order.model.PaymentResponse;
import com.phucx.order.model.Product;
import com.phucx.order.service.customer.CustomerService;
import com.phucx.order.service.employee.EmployeeService;
import com.phucx.order.service.notification.NotificationService;
import com.phucx.order.service.order.CustomerOrderService;
import com.phucx.order.service.order.OrderService;
import com.phucx.order.service.payment.PaymentService;
import com.phucx.order.service.product.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerOrderServiceImp implements CustomerOrderService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PaymentService paymentService;
    @Value("${phucx.redirect-payment-url}")
    private String redirectPaymentUrl;
    
    @Override
    public PaymentResponse placeOrder(OrderWithProducts order, String userID) 
    throws JsonProcessingException, NotFoundException, InvalidDiscountException, InvalidOrderException {
        log.info("placeOrder(order={}, userID={})", order, userID);
        // fetch customer
        Customer customer = customerService.getCustomerByUserID(userID);
        order.setCustomerID(customer.getCustomerID());
        // process order
        OrderDetails newOrder = this.orderProcessing(order);
        // create a notification 
        if(newOrder ==null){
            throw new RuntimeException("Error when placing an order");
        }
        Integer productID = order.getProducts().get(0).getProductID();
        Product fetchedProduct = productService.getProduct(productID);

        // payment
        PaymentDTO payment = new PaymentDTO(newOrder.getTotalPrice().doubleValue(), 
            newOrder.getOrderID(), order.getMethod(), order.getCustomerID(), redirectPaymentUrl);
        PaymentResponse paymentResponse = paymentService.createPayment(payment);
        // customer
        // create and save notification back to user
        OrderNotificationDTO customerNotification = new OrderNotificationDTO(
            newOrder.getOrderID(), NotificationTitle.PLACE_ORDER, 
            userID, NotificationTopic.Order, NotificationStatus.SUCCESSFUL);
        customerNotification.setMessage("Your order #" + newOrder.getOrderID() + " has been placed successfully");
        customerNotification.setPicture(fetchedProduct.getPicture());
        // send message back to user
        notificationService.sendCustomerOrderNotification(customerNotification);
        // employee
        // send message to notification message queue
        OrderNotificationDTO employeeNotification = new OrderNotificationDTO(
            newOrder.getOrderID(), NotificationTitle.PLACE_ORDER, 
            userID, NotificationBroadCast.ALL_EMPLOYEES.name(), 
            NotificationTopic.Order, NotificationStatus.SUCCESSFUL);
        employeeNotification.setPicture(fetchedProduct.getPicture());
        employeeNotification.setMessage("A new order #" + newOrder.getOrderID() + " has been placed");
        notificationService.sendEmployeeOrderNotification(employeeNotification);

        return paymentResponse;
    }

    // order processing
    // validating and saving customer's order 
    private OrderDetails orderProcessing(OrderWithProducts order) 
        throws JsonProcessingException, InvalidDiscountException, InvalidOrderException, NotFoundException {
            
        log.info("orderProcessing({})", order);
        if(order.getCustomerID()==null)
            throw new InvalidOrderException("Customer does not found");
        if(order.getProducts()==null || order.getProducts().isEmpty())
            throw new InvalidOrderException("Your order does not have any products");

        LocalDateTime currenDateTime = LocalDateTime.now();
        order.setOrderDate(currenDateTime);
        // set applied date for discount;
        List<Integer> productIDs = new ArrayList<>();
        for (OrderItem product : order.getProducts()) {
            productIDs.add(product.getProductID());
            for(OrderItemDiscount discount : product.getDiscounts()){
                discount.setAppliedDate(currenDateTime);
            }
        }
        // validate products
        // fetch products
        List<Product> fetchedProducts = productService.getProducts(productIDs);
        // check product and set product's unitprice to customer's order
        for (OrderItem item : order.getProducts()) {
            Product product = this.findProduct(fetchedProducts, item.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + item.getProductID() + " does not found"));
            item.setUnitPrice(product.getUnitPrice());
        }

        // validate order
        boolean isValidOrder = orderService.validateOrder(order);
        if(!isValidOrder) throw new InvalidOrderException("Order of customer "+order.getCustomerID()+" is not valid");
        // save order
        String pendingOrderID = orderService.saveFullOrder(order);
        order.setOrderID(pendingOrderID);

        OrderDetails orderDetails = orderService.getOrder(pendingOrderID);
        return orderDetails;
    }

    private Optional<Product> findProduct(List<Product> products, Integer productID){
        return products.stream().filter(product -> product.getProductID()==productID).findFirst();
    }

    @Override
    public void receiveOrder(OrderWithProducts order) throws JsonProcessingException, NotFoundException {
        log.info("receiveOrder(orderID={})", order.getOrderID());
        // get order
        OrderDetails orderDetails = orderService.getOrder(order.getOrderID(), OrderStatus.Shipping);
        // get user 
        Employee employeeUser = employeeService.getEmployeeByID(orderDetails.getEmployeeID());
        // update order's status
        Boolean status = orderService.updateOrderStatus(orderDetails.getOrderID(), OrderStatus.Successful);
        // update payment as succcessful
        paymentService.updatePaymentByOrderIDAsSuccesful(orderDetails.getOrderID());
        
        // notification
        OrderNotificationDTO notification = new OrderNotificationDTO();
        notification.setTitle(NotificationTitle.RECEIVE_ORDER);
        notification.setTopic(NotificationTopic.Order);
        notification.setOrderID(orderDetails.getOrderID());
        if(status){
            notification.setMessage("Order #" + orderDetails.getOrderID() + 
                " is received successully by customer " + orderDetails.getCustomerID());
            notification.setStatus(NotificationStatus.SUCCESSFUL);
            notification.setReceiverID(employeeUser.getUserID());
        }else {
            notification.setMessage("Order #" + orderDetails.getOrderID() + 
                " can not received by customer " + orderDetails.getCustomerID());
            notification.setStatus(NotificationStatus.ERROR);
            notification.setReceiverID(employeeUser.getUserID());
        }
        notificationService.sendEmployeeOrderNotification(notification);
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
