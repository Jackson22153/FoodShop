package com.phucx.order.service.order.imp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.constant.NotificationStatus;
import com.phucx.order.constant.NotificationTopic;
import com.phucx.order.constant.NotificationTitle;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.Customer;
import com.phucx.order.model.Employee;
import com.phucx.order.model.OrderNotificationDTO;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.ProductStockTableType;
import com.phucx.order.service.customer.CustomerService;
import com.phucx.order.service.employee.EmployeeService;
import com.phucx.order.service.messageQueue.MessageQueueService;
import com.phucx.order.service.notification.NotificationService;
import com.phucx.order.service.order.EmployeeOrderService;
import com.phucx.order.service.order.OrderService;
import com.phucx.order.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeOrderServiceImp implements EmployeeOrderService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CustomerService customerService;

    @Override
    public void confirmOrder(String orderID, String userID) throws InvalidOrderException, JsonProcessingException, NotFoundException {
        log.info("confirmOrder(orderID={}, userID={})", orderID, userID);
        OrderWithProducts orderWithProducts = orderService.getPendingOrderDetail(orderID);
        // fetch employee
        Employee employee = employeeService.getEmployeeByUserID(userID);

        orderWithProducts.setEmployeeID(employee.getEmployeeID());
        // send to order message queue for validating
        messageQueueService.sendOrder(orderWithProducts);
        
    }

    @Override
    public void cancelPendingOrder(OrderWithProducts order, String userID) throws JsonProcessingException, NotFoundException {
        log.info("cancelPendingOrder(order={}, userID={})", order, userID);
        cancelOrder(order, userID, OrderStatus.Pending);
    }

    @Override
    public void cancelConfirmedOrder(OrderWithProducts order, String userID) throws JsonProcessingException, NotFoundException {
        log.info("cancelConfirmedOrder(order={}, userID={})", order, userID);
        cancelOrder(order, userID, OrderStatus.Confirmed);
        OrderDetails fetchedOrder = orderService.getOrder(order.getOrderID());
        // rollback product instocks
        List<ProductStockTableType> products = fetchedOrder.getProducts().stream()
            .map(product -> new ProductStockTableType(product.getProductID(), product.getQuantity()))
            .collect(Collectors.toList());
        productService.updateProductsInStocks(products);
    }


    private void cancelOrder(OrderWithProducts order, String userID, OrderStatus orderStatus) 
        throws JsonProcessingException, NotFoundException{

        log.info("cancelOrder(order={}, userID={}, orderStatus={})", order, userID, orderStatus);
        // fetch pending order
        OrderDetails orderDetail = orderService.getOrder(order.getOrderID(), orderStatus);
        // fetch employee
        Employee employee = employeeService.getEmployeeByUserID(userID);
        order.setEmployeeID(employee.getEmployeeID());
        Boolean check = orderService.updateOrderEmployee(order.getOrderID(), employee.getEmployeeID());
        if(!check) throw new RuntimeException("Order #" + order.getOrderID() + " can not be updated");
        // update order status as canceled
        Boolean status = orderService.updateOrderStatus(orderDetail.getOrderID(), OrderStatus.Canceled);
        if(!status) throw new RuntimeException("Order #" + order.getOrderID() + " can not be updated to canceled status");
        // notification
        // notification
        OrderNotificationDTO notification = new OrderNotificationDTO();
        notification.setTitle(NotificationTitle.CANCEL_ORDER);
        notification.setTopic(NotificationTopic.Order);
        if(OrderStatus.Pending.equals(orderStatus))
            notification.setOrderID(order.getOrderID());
        if(status){
            // send message to customer
            Customer fetchedCustomer = customerService.getCustomerByID(order.getCustomerID());
            notification.setReceiverID(fetchedCustomer.getUserID());
            notification.setMessage("Order #" + order.getOrderID() + " has been canceled");
            notification.setStatus(NotificationStatus.SUCCESSFUL);
        }else {
            // send message to employee
            notification.setReceiverID(userID);
            notification.setMessage("Error: Order #" + order.getOrderID() + " can not be canceled");
            notification.setStatus(NotificationStatus.ERROR);
        }
        notificationService.sendCustomerOrderNotification(notification);
    }

    @Override
    public void fulfillOrder(OrderWithProducts order, String userID) throws JsonProcessingException, NotFoundException {
        log.info("fullfillOrder(order={}, userID={})", order, userID);
        OrderDetails fetchedOrder = orderService.getOrder(order.getOrderID(), OrderStatus.Confirmed);
        Boolean status = orderService.updateOrderStatus(fetchedOrder.getOrderID(), OrderStatus.Shipping);
        if(!status) throw new RuntimeException("Order #" + order.getOrderID() + " can not be updated to shipping status");
        // notification
        OrderNotificationDTO notification = new OrderNotificationDTO();
        notification.setTitle(NotificationTitle.FULFILL_ORDER);
        notification.setTopic(NotificationTopic.Order);
        notification.setOrderID(order.getOrderID());
        if(status){
            // send message to customer
            Customer fetchedCustomer = customerService.getCustomerByID(order.getCustomerID());
            notification.setReceiverID(fetchedCustomer.getUserID());
            notification.setMessage("Order #" + order.getOrderID() + " has been fullfilled");
            notification.setStatus(NotificationStatus.SUCCESSFUL);
        }else {
            // send message to employee
            notification.setReceiverID(userID);
            notification.setMessage("Error: Order #" + order.getOrderID() + " can not be fulfilled");
            notification.setStatus(NotificationStatus.ERROR);
        }
        notificationService.sendCustomerOrderNotification(notification);
    }

    @Override
    public Page<OrderDetails> getOrders(String userID, OrderStatus status, int pageNumber, int pageSize)
            throws JsonProcessingException, NotFoundException {
        log.info("getOrders(userID={}, status={}, pageNumber={}, pageSize={})", userID, status, pageNumber, pageSize);
        Employee fetchedEmployee = employeeService.getEmployeeByUserID(userID);
        Page<OrderDetails> orders = null;
        if(status.equals(OrderStatus.All)){
            orders = orderService.getOrdersByEmployeeID(
                fetchedEmployee.getEmployeeID(), 
                pageNumber, pageSize);
        }else if(status.equals(OrderStatus.Pending)){
            orders = orderService.getOrders(OrderStatus.Pending, pageNumber, pageSize);
        }else{
            orders = orderService.getOrdersByEmployeeID(
                fetchedEmployee.getEmployeeID(), 
                status, pageNumber, pageSize);
        }
        return orders;
    }

    @Override
    public OrderWithProducts getOrder(String orderID, String userID, OrderStatus status) throws JsonProcessingException, NotFoundException {
        log.info("getOrder(orderID={}, userID={}, orderStatus={})", orderID, userID, status);
        Employee fetchedEmployee = employeeService.getEmployeeByUserID(userID);
        OrderWithProducts order = null;
        if(status.equals(OrderStatus.Pending)){
            order = orderService.getPendingOrderDetail(orderID);
        }else if(status.equals(OrderStatus.All)){
            order = orderService.getOrderByEmployeeID(fetchedEmployee.getEmployeeID(), orderID);
        }else {
            order = orderService.getOrderByEmployeeID(fetchedEmployee.getEmployeeID(), orderID, status);
        }
        return order;
    }
}
