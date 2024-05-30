package com.phucx.order.service.customer;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.order.constant.EventType;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.model.Customer;
import com.phucx.order.model.DataDTO;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.Notification;
import com.phucx.order.model.UserDTO;
import com.phucx.order.service.messageQueue.MessageQueueService;
import com.phucx.order.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImp implements CustomerService {
    @Autowired
    private NotificationService notificationService;
    // @Autowired
    // private OrderService orderService;
    @Autowired
    private MessageQueueService messageQueueService;
	
	@Override
	public Customer getCustomerByID(String customerID) throws JsonProcessingException {
		log.info("getCustomerByID(customerID={})", customerID);
        // create a request for user
        UserDTO userDTO = new UserDTO();
        userDTO.setCustomerID(customerID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetCustomerByID);
        eventMessage.setPayload(userDTO);
        // receive data
        EventMessage<Customer> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.USER_QUEUE, 
            MessageQueueConstant.USER_ROUTING_KEY,
            Customer.class);
        log.info("response={}", response);
        return response.getPayload();
	}
    // order processing
    // validating and saving customer's order 
    // private OrderWithProducts orderProcessing(OrderWithProducts order) 
    // throws InvalidDiscountException, InvalidOrderException, NotFoundException, SQLException, RuntimeException{
    //     log.info("orderProcessing({})", order);
    //     if(order.getCustomerID()!=null){
    //         LocalDateTime currenDateTime = LocalDateTime.now();
    //         order.setOrderDate(currenDateTime);
    //         // set applieddate for discount;
    //         for (OrderItem product : order.getProducts()) {
    //             for(OrderItemDiscount discount : product.getDiscounts()){
    //                 discount.setAppliedDate(currenDateTime);
    //             }
    //         }
            
    //         // validate order
    //         boolean isValidOrder = orderService.validateOrder(order);
    //         if(!isValidOrder) throw new InvalidOrderException("Order of customer "+order.getCustomerID()+" is not valid");
    //         // save order
    //         String pendingOrderID = orderService.saveFullOrder(order);
    //         order.setOrderID(pendingOrderID);
    //         return order;
    //     }
    //     throw new NotFoundException("Customer is not found");
    // }
    
    // @Override
    // public OrderDetailDTO placeOrder(OrderWithProducts order, String customerID, String userID) 
    // throws InvalidDiscountException, InvalidOrderException, NotFoundException, SQLException, RuntimeException{
    //     log.info("placeOrder(order={}, customerID={}, userID={})", order, customerID, userID);
    //     order.setCustomerID(customerID);
    //     OrderWithProducts newOrder = this.orderProcessing(order);
    //     log.info("newOrder: {}", newOrder);
    //     if(newOrder !=null){
    //         // create and save notification back to user
    //         Notification notificationToCustomer = new Notification(
    //             "Place Order",
    //             "Order #"+newOrder.getOrderID()+" has been placed successfully",
    //             null, userID, new Topic(NotificationTopic.Order.name()),
    //             NotificationStatus.SUCCESSFUL.name(), true);
    //         // send message back to user
    //         messageQueueService.sendNotification(notificationToCustomer);
    //         // messageQueueService.sendNotificationToUser(customer.getUserID(), notificationToUser);
    //         // send message to notification message queue
    //         Notification notificationToTopic = new Notification(
    //             "Place Order",
    //             "Order #" + newOrder.getOrderID() + " has been placed", 
    //             userID, null, new Topic(NotificationTopic.Order.name()), 
    //             NotificationStatus.SUCCESSFUL.name(), true);
    //         messageQueueService.sendNotification(notificationToTopic);
    //         // 
    //         return orderService.getOrder(newOrder.getOrderID(), OrderStatus.Pending);
    //     }
    //     throw new RuntimeException("Error when placing an order");
    // }
    
    // @Override
    // public Notification receiveOrder(OrderWithProducts order) {
    //     log.info("receiveOrder(orderID={})", order.getOrderID());
        
    //     OrderDetailDTO OrderDetailDTO = orderService.getOrder(order.getOrderID(), OrderStatus.Shipping);
    //     Boolean status = orderService.updateOrderStatus(OrderDetailDTO.getOrderID(), OrderStatus.Successful);
    //     // notification
    //     Notification notification = new Notification();
    //     notification.setTitle("Receive Order");
    //     notification.setTopic(new Topic(NotificationTopic.Order.name()));
    //     log.info("status: {}", status);
    //     // if(status){
    //     //     log.info("ssnotification: {}", notification);
    //     //     notification.setMessage("Order #" + OrderDetailDTO.getOrderID() + " is received successully by customer " + OrderDetailDTO.getCustomerID());
    //     //     notification.setStatus(NotificationStatus.SUCCESSFUL.name());
    //     //     notification.setReceiverID(userService.getUserIdOfEmployeeID(OrderDetailDTO.getEmployeeID()));
    //     //     log.info("ssnotification: {}", notification);
    //     // }else {
    //     //     notification.setMessage("Order #" + OrderDetailDTO.getOrderID() + " can not received by customer " + OrderDetailDTO.getCustomerID());
    //     //     notification.setStatus(NotificationStatus.ERROR.name());
    //     //     notification.setReceiverID(userService.getUserIdOfEmployeeID(OrderDetailDTO.getEmployeeID()));
    //     // }
    //     log.info("notification: {}", notification);
    //     return notification;
    // }

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

    @Override
    public List<Customer> getCustomersByIDs(List<String> customerIDs) throws JsonProcessingException{
        log.info("getCustomersByIDs(customerIDs={})", customerIDs);
        // create a request for user
        UserDTO userDTO = new UserDTO();
        userDTO.setCustomerIDs(customerIDs);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetCustomersByID);
        eventMessage.setPayload(userDTO);
        // receive data
        TypeReference<List<Customer>> typeReference = new TypeReference<List<Customer>>() {};
        EventMessage<List<Customer>> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.USER_QUEUE, 
            MessageQueueConstant.USER_ROUTING_KEY,
            typeReference);
        log.info("response={}", response);
        return response.getPayload();
    }
}
