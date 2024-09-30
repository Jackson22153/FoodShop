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
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.InvoiceDetails;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.PaymentResponse;
import com.phucx.order.service.order.CustomerOrderService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/customer")
public class CustomerOrderController {
    @Autowired
    private CustomerOrderService customerOrderService;

    // ENDPOINT TO PLACE AN ORDER
    @Operation(summary = "Place an order", tags = {"tutorials", "post", "customer"})
    @LoggerAspect
    @PostMapping("/order/place")
    public ResponseEntity<PaymentResponse> placeOrder(@RequestBody OrderWithProducts order, Authentication authentication) 
        throws JsonProcessingException, InvalidDiscountException, InvalidOrderException, NotFoundException{

        PaymentResponse paymentResponse = customerOrderService.placeOrder(order, authentication.getName());
        return ResponseEntity.ok().body(paymentResponse);
    }

    @LoggerAspect
    @Operation(summary = "Receive customer order", tags = {"tutorials", "post", "customer"})
    @PostMapping("/order/receive")
    public ResponseEntity<Void> receiveOrder(@RequestBody OrderWithProducts order, Authentication authentication) 
        throws JsonProcessingException, NotFoundException{
        customerOrderService.receiveOrder(order);
        return ResponseEntity.ok().build();
    }
    
    // get INVOICE of customer
    @Operation(summary = "Get order by id", tags = {"tutorials", "get", "customer"})
    @GetMapping("/orders/{orderID}")
    public ResponseEntity<InvoiceDetails> getOrder(@PathVariable String orderID, Authentication authentication
    ) throws JsonProcessingException, NotFoundException{    
        InvoiceDetails order = customerOrderService.getInvoice(orderID, authentication.getName());
        return ResponseEntity.ok().body(order);
    }
    // GET ALL ORDERS OF CUSTOMER
    @Operation(summary = "Get orders", tags = {"tutorials", "get", "customer"})
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderDetails>> getOrders(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        @RequestParam(name = "type", required = false) String orderStatus,
        Authentication authentication
    ) throws JsonProcessingException, NotFoundException{    
        pageNumber = pageNumber!=null?pageNumber:0;
        OrderStatus status = null;
        if(orderStatus!=null){
            status = OrderStatus.fromString(orderStatus.toUpperCase());
        }else {
            status=OrderStatus.All;
        }
        Page<OrderDetails> orders = customerOrderService.getOrders(
            pageNumber, WebConstant.PAGE_SIZE, authentication.getName(), status);
        return ResponseEntity.ok().body(orders);
    }
}
