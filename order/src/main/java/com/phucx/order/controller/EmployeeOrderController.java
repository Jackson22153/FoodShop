package com.phucx.order.controller;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.annotations.LoggerAspect;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.constant.WebConstant;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderSummary;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.service.order.EmployeeOrderService;
import com.phucx.order.service.order.OrderService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/employee")
public class EmployeeOrderController {
    @Autowired
    private EmployeeOrderService employeeOrderService;
    @Autowired
    private OrderService orderService;
    // CONFIRM AN ORDER
    @LoggerAspect
    @Operation(summary = "Confirm a pending order", tags = {"tutorials", "post", "employee"})
    @PostMapping("/order/confirm")
    public ResponseEntity<Void> confirmOrder(
        @RequestBody OrderWithProducts order, 
        Authentication authentication
    ) throws InvalidOrderException, JsonProcessingException, NotFoundException{
        // // validate order
        employeeOrderService.confirmOrder(order.getOrderID(), authentication.getName());
        return ResponseEntity.ok().build();
    }
    // CANCEL AN ORDER
    @LoggerAspect
    @Operation(summary = "Cancel an order", tags = {"tutorials", "post", "employee"})
    @PostMapping("/order/cancel")
    public ResponseEntity<Void> cancelOrder(
        @RequestBody OrderWithProducts order, 
        @RequestParam(name = "type") String ordertype,
        Authentication authentication
    ) throws JsonProcessingException, NotFoundException{
        // accept pending and confirmed for order type
        if(OrderStatus.Pending.name().equalsIgnoreCase(ordertype)){
            // cancel order
            employeeOrderService.cancelPendingOrder(order, authentication.getName());
        }else if(OrderStatus.Confirmed.name().equalsIgnoreCase(ordertype)){
            // cancel order
            employeeOrderService.cancelConfirmedOrder(order, authentication.getName());
        }
        return ResponseEntity.ok().build();
    }
    // FULFILL ORDER
    @LoggerAspect
    @Operation(summary = "Fulfill an order", tags = {"tutorials", "post", "employee"})
    @PostMapping("/order/fulfill")
    public ResponseEntity<Void> fulfillOrder(
        @RequestBody OrderWithProducts order, Authentication authentication
    ) throws JsonProcessingException, NotFoundException{
        // update order status
        employeeOrderService.fulfillOrder(order, authentication.getName());
        return ResponseEntity.ok().build();
    }


    // get order of emloyee
    @Operation(summary = "Get an order by id", tags = {"tutorials", "get", "employee"})
    @GetMapping("/orders/{orderID}")
    public ResponseEntity<OrderWithProducts> getOrder(
        @PathVariable(name = "orderID") String orderID, 
        @RequestParam(name = "type", required = false) String orderStatus,
        Authentication authentication
    ) throws JsonProcessingException, NotFoundException{    
        // get order's status
        OrderStatus status = orderStatus!=null?OrderStatus.fromString(orderStatus.toUpperCase()):OrderStatus.All;
        OrderWithProducts order = employeeOrderService.getOrder(orderID, authentication.getName(), status);
        return ResponseEntity.ok().body(order);
    }

    // GET ALL ORDERS WHICH EMPLOYEE HAS APPROVED
    @Operation(summary = "Get orders", tags = {"tutorials", "get", "employee"})
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderDetails>> getOrders(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        @RequestParam(name = "type", required = false) String orderStatus,
        Authentication authentication
    ) throws JsonProcessingException, NotFoundException{    
        pageNumber = pageNumber!=null?pageNumber:0;
        // get order's status
        OrderStatus status = orderStatus!=null?OrderStatus.fromString(orderStatus.toUpperCase()):OrderStatus.All;
        // get orders
        Page<OrderDetails> orders = employeeOrderService.getOrders(
            authentication.getName(), status, pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(orders);
    }

    // get order summary
    @Operation(summary = "Get order summary", tags = {"tutorials", "get", "employee"},
        description = "Get number of pending orders")
    @GetMapping("/summary")
    public ResponseEntity<OrderSummary> getSummaryOrders(){
        OrderSummary summary = orderService.getOrderSummary();
        return ResponseEntity.ok().body(summary);
    }
}
