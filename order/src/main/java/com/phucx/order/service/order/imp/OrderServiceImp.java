package com.phucx.order.service.order.imp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.compositeKey.OrderDetailKey;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.Customer;
import com.phucx.order.model.Invoice;
import com.phucx.order.model.InvoiceDetails;
import com.phucx.order.model.OrderItem;
import com.phucx.order.model.OrderItemDiscount;
import com.phucx.order.model.OrderSummary;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Order;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderDetailExtended;
import com.phucx.order.model.ProductDiscountsDTO;
import com.phucx.order.model.ResponseFormat;
import com.phucx.order.repository.InvoiceRepository;
import com.phucx.order.repository.OrderDetailDiscountRepository;
import com.phucx.order.repository.OrderDetailExtendedRepository;
import com.phucx.order.repository.OrderDetailRepository;
import com.phucx.order.repository.OrderRepository;
import com.phucx.order.service.bigdecimal.BigDecimalService;
import com.phucx.order.service.customer.CustomerService;
import com.phucx.order.service.order.ConvertOrderService;
import com.phucx.order.service.order.OrderService;
import com.phucx.order.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImp implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailDiscountRepository orderDetailDiscountRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderDetailExtendedRepository orderDetailExtendedRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private BigDecimalService bigDecimalService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ConvertOrderService convertOrderService;

    @Override
    public Boolean updateOrderStatus(String orderID, OrderStatus status) throws NotFoundException {
        log.info("updateOrderStatus(orderID={}, status={})", orderID, status);
        Order order = orderRepository.findById(orderID)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " does not found"));
        Integer check = orderRepository.updateOrderStatus(order.getOrderID(), status); 
        if(check>0) return true;
        return false;
    }

    @Override
    @Transactional
    public String saveFullOrder(OrderWithProducts order) throws JsonProcessingException, NotFoundException {
        log.info("saveFullOrder({})", order);
        String orderID = this.saveOrder(order);
        // // save OrderDetail
        List<OrderItem> orderItems = order.getProducts();
        for(OrderItem orderItem : orderItems) {
            OrderDetailKey orderDetailID = this.saveOrderDetail(orderID, orderItem);
            for(OrderItemDiscount discount: orderItem.getDiscounts()){
                if(discount.getDiscountID()!=null)
                    this.saveOrderDetailDiscounts(orderDetailID, discount);
            }
        }
        return orderID;
    }
    // save order
    private String saveOrder(OrderWithProducts order) throws JsonProcessingException, NotFoundException{
        log.info("saveOrder({})", order);
        String orderID = UUID.randomUUID().toString();
        Customer fetchedCustomer = customerService.getCustomerByID(order.getCustomerID());
        if(fetchedCustomer==null){
            throw new NotFoundException("Customer does not found when saving order");
        }
        Boolean status = orderRepository.insertOrder(
            orderID, order.getOrderDate(), order.getRequiredDate(), 
            order.getShippedDate(), order.getFreight(), order.getShipName(), 
            order.getShipAddress(), order.getShipCity(), order.getPhone(), 
            OrderStatus.Pending.name(), order.getCustomerID(), 
            order.getEmployeeID(), order.getShipVia());
        if(!status) throw new RuntimeException("Error while saving order for customer " + order.getCustomerID());
        return orderID;
    }
    // save OrderDetail
    private OrderDetailKey saveOrderDetail(String orderID, OrderItem orderItem){
        log.info("saveOrderDetail(orderID={}, orderItem={})", orderID, orderItem.toString());
        Boolean status = orderDetailRepository.insertOrderDetail(
            orderItem.getProductID(), orderID, 
            bigDecimalService.formatter(orderItem.getUnitPrice()), 
            orderItem.getQuantity());
        if(!status) throw new RuntimeException("Error while saving orderDetail for order " + orderID + " and product " + orderItem.getProductID());
        return new OrderDetailKey(orderItem.getProductID(), orderID);
    }
    // save orderDiscount
    private void saveOrderDetailDiscounts(OrderDetailKey orderDetailID, OrderItemDiscount orderItemDiscount){
        log.info("saveOrderDetailDiscounts(orderDetailID={}, orderItemDiscount={})", orderDetailID, orderItemDiscount);
        LocalDateTime currentTime = LocalDateTime.now();
        Boolean status = orderDetailDiscountRepository.insertOrderDetailDiscount(
            orderDetailID.getOrderID(), orderDetailID.getProductID(), 
            orderItemDiscount.getDiscountID(), orderItemDiscount.getDiscountPercent(), 
            currentTime);
        if(!status) throw new RuntimeException("Error while saving orderDetailDiscount for orderDetail " + orderDetailID.toString() + " and discount " + orderItemDiscount.getDiscountID());
    }

    @Override
    public ResponseFormat validateAndProcessOrder(OrderWithProducts order) throws JsonProcessingException{
        log.info("validateAndProcessOrder({})", order.toString());
        if(order==null || order.getProducts().size()==0) {
            ResponseFormat responseFormat = new ResponseFormat();
            responseFormat.setStatus(false);
            responseFormat.setError("Your order does not have any products");
            return responseFormat;
        }

        List<ProductDiscountsDTO> productDiscountsDTOs = order.getProducts().stream().map((product) -> {
            // get discountIDs
            List<String> discountIDs = product.getDiscounts().stream()
                .map(OrderItemDiscount::getDiscountID).collect(Collectors.toList());
            // get applieddate
            LocalDateTime currenTime = null;
            if(!discountIDs.isEmpty()){
                currenTime = product.getDiscounts().get(0).getAppliedDate();
            }
            return new ProductDiscountsDTO(product.getProductID(), product.getQuantity(), discountIDs, currenTime);
        }).collect(Collectors.toList());

        // validate products
        ResponseFormat responseFormat = productService.validateProducts(productDiscountsDTOs);
        return responseFormat;
    }
    
    
    @Override
    public Boolean updateOrderEmployee(String orderID, String employeeID) {
        log.info("updateOrderEmployee(orderID={}, employeeID={})", orderID, employeeID);
        Integer check = orderRepository.updateOrderEmployeeID(orderID, employeeID);
        if(check>0) return true;
        return false;
    }

    @Override
    public Boolean validateOrder(OrderWithProducts order) throws JsonProcessingException, InvalidDiscountException {
        log.info("validateOrder({})", order);
        // product discount DTOs
        List<ProductDiscountsDTO> productDiscountsDTOs = new ArrayList<>();
        // extract products and discounts from order
        for (OrderItem item : order.getProducts()) {
            List<String> discountIds = item.getDiscounts().stream()
                .filter(discount -> discount.getDiscountID()!=null)
                .map(OrderItemDiscount::getDiscountID)
                .collect(Collectors.toList());

            LocalDateTime appliedDate = item.getDiscounts().get(0).getAppliedDate();
            ProductDiscountsDTO productDiscountsDTO = new ProductDiscountsDTO(
                item.getProductID(), item.getQuantity(), discountIds, appliedDate);
            productDiscountsDTOs.add(productDiscountsDTO);
        }
        // validate products and discounts
        ResponseFormat isValidDiscount = productService.validateProducts(productDiscountsDTOs);       
        if(!isValidDiscount.getStatus()) throw new InvalidDiscountException(isValidDiscount.getError());
        return isValidDiscount.getStatus();
    }

    @Override
    public OrderWithProducts getPendingOrderDetail(String orderID) throws JsonProcessingException, NotFoundException{
        log.info("getPendingOrderDetail(orderID={})", orderID);
        Order order = orderRepository.findByOrderIDAndStatus( orderID, OrderStatus.Pending)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " with pending status does not found"));
        OrderWithProducts convertedOrder = convertOrderService.convertOrderWithProducts(order);
        return convertedOrder;
    }

    @Override
    public OrderDetails getOrder(String orderID, OrderStatus status) throws JsonProcessingException, NotFoundException {
        log.info("getOrder(orderID={}, status={})", orderID, status.name());
        List<OrderDetailExtended> orderDetailExtendeds = orderDetailExtendedRepository
            .findByOrderIDAndStatus(orderID, status);
        if(orderDetailExtendeds==null || orderDetailExtendeds.size()==0) 
            throw new NotFoundException("Order "+ orderID +" with status "+ status +" does not found");
        return convertOrderService.convertOrderDetail(orderDetailExtendeds);
    }

    @Override
    public OrderDetails getOrder(String orderID) throws JsonProcessingException, NotFoundException {
        log.info("getOrder(orderID={})", orderID);
        List<OrderDetailExtended> orderDetailExtended = orderDetailExtendedRepository
            .findByOrderID(orderID);
        if(orderDetailExtended==null || orderDetailExtended.size()==0) 
            throw new NotFoundException("Order "+ orderID +" does not found");
        return convertOrderService.convertOrderDetail(orderDetailExtended);
    }

    @Override
    public Boolean isPendingOrder(String orderID) throws NotFoundException {
        log.info("isPendingOrder(OrderID={})", orderID);
        Order fetchedOrder = orderRepository.findById(orderID)
            .orElseThrow(()-> new NotFoundException("Order ID " + orderID + " does not found"));
        if(fetchedOrder.getStatus().equals(OrderStatus.Pending)) return true;
        return false;
    }

    @Override
    public Page<OrderDetails> getOrdersByCustomerID(String customerID, OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException, NotFoundException {
        log.info("getOrdersByCustomerID(customerID={}, orderStatus={}, pageNumber={}, pageSize={})", 
            customerID, status, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByCustomerIDAndStatusOrderByOrderDateDesc(customerID, status, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }

    @Override
    public InvoiceDetails getInvoiceByCustomerID(String customerID, String orderID) throws JsonProcessingException, NotFoundException {
        log.info("getInvoiceByCustomerID(customerID={}, orderID={})", customerID, orderID);
        List<Invoice> invoices = invoiceRepository.findByOrderIDAndCustomerID(orderID, customerID);
        if(invoices==null || invoices.isEmpty()) 
            throw new NotFoundException("Invoice " + orderID + " of customer "+ customerID + " does not found");
        log.info("Invoices: {}", invoices);
        InvoiceDetails invoice = convertOrderService.convertInvoiceDetails(invoices);
        return invoice;
    }

    @Override
    public Page<OrderDetails> getOrdersByEmployeeID(String employeeID, OrderStatus status, Integer pageNumber,
            Integer pageSize) throws JsonProcessingException, NotFoundException {
        log.info("getOrdersByEmployeeID(employeeID={}, status={}, pageNumber={}, pageSize={})", 
            employeeID, status, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByEmployeeIDAndStatusOrderByOrderDateDesc(employeeID, status, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }

    @Override
    public OrderWithProducts getOrderByEmployeeID(String employeeID, String orderID) throws JsonProcessingException, NotFoundException {
        log.info("getOrderByEmployeeID(employeeID={}, orderID={})", employeeID, orderID);
        Order order = orderRepository.findByEmployeeIDAndOrderID(employeeID, orderID)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " of employee "+ employeeID + " does not found"));
        return convertOrderService.convertOrderWithProducts(order);
    }

    @Override
    public Page<OrderDetails> getOrdersByCustomerID(String customerID, Integer pageNumber, Integer pageSize)
            throws JsonProcessingException, NotFoundException {
        log.info("getOrdersByCustomerID(customerID={}, pageNumber={}, pageSize={})", 
            customerID, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByCustomerIDOrderByOrderDateDesc(customerID, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }

    @Override
    public Page<OrderDetails> getOrdersByEmployeeID(String employeeID, Integer pageNumber, Integer pageSize)
            throws JsonProcessingException, NotFoundException {
        log.info("getOrdersByEmployeeID(employeeID={}, pageNumber={}, pageSize={})", 
            employeeID, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByEmployeeIDOrderByOrderDateDesc(employeeID, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }

    @Override
    public Page<OrderDetails> getOrders(OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException, NotFoundException {
        log.info("getOrders(status={}, pageNumber={}, pageSize={})", status, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByStatusOrderByOrderDateDesc(status, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }

    @Override
    public OrderWithProducts getOrderByEmployeeID(String employeeID, String orderID, OrderStatus orderStatus)
            throws JsonProcessingException, NotFoundException {
        log.info("getOrderByEmployeeID(employeeID={}, orderID={}, orderStatus={})", employeeID, orderID, orderStatus);
        Order order = orderRepository.findByOrderIDAndEmployeeIDAndStatus(orderID, employeeID, orderStatus)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " of employee "+ employeeID + " does not found"));
        return convertOrderService.convertOrderWithProducts(order);
    }


    private Long countOrderByStatus(OrderStatus status) {
        Long totalOrders = orderRepository.countOrderByStatus(status)
        .orElseThrow(()-> new RuntimeException("Error occured while counting orders"));
        return totalOrders;
    }

    @Override
    public OrderSummary getOrderSummary() {
        OrderSummary summary = new OrderSummary();
        // total pending orders
        Long totalOfPendingOrders = this.countOrderByStatus(OrderStatus.Pending);
        summary.setTotalPendingOrders(totalOfPendingOrders);
        return summary;
    }
}
