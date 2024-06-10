package com.phucx.order.service.order.imp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.phucx.order.constant.WebConstant;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.model.Customer;
import com.phucx.order.model.DiscountDetail;
import com.phucx.order.model.Invoice;
import com.phucx.order.model.InvoiceDetails;
import com.phucx.order.model.OrderItem;
import com.phucx.order.model.OrderItemDiscount;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Order;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderDetailExtended;
import com.phucx.order.model.ProductDiscountsDTO;
import com.phucx.order.model.Product;
import com.phucx.order.repository.InvoiceRepository;
import com.phucx.order.repository.OrderDetailDiscountRepository;
import com.phucx.order.repository.OrderDetailExtendedRepository;
import com.phucx.order.repository.OrderDetailRepository;
import com.phucx.order.repository.OrderRepository;
import com.phucx.order.service.bigdecimal.BigDecimalService;
import com.phucx.order.service.customer.CustomerService;
import com.phucx.order.service.discount.DiscountService;
import com.phucx.order.service.order.ConvertOrderService;
import com.phucx.order.service.order.OrderService;
import com.phucx.order.service.product.ProductService;
import jakarta.ws.rs.NotFoundException;
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
    private DiscountService discountService;
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
    public Boolean updateOrderStatus(String orderID, OrderStatus status) {
        log.info("updateOrderStatus(orderID={}, status={})", orderID, status);
        Order order = orderRepository.findById(orderID)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " does not found"));
        Integer check = orderRepository.updateOrderStatus(order.getOrderID(), status); 
        if(check>0) return true;
        return false;
    }

    @Override
    @Transactional
    public String saveFullOrder(OrderWithProducts order) throws JsonProcessingException {
            log.info("saveFullOrder({})", order);
        String orderID = this.saveOrder(order);
        // // save OrderDetail
        List<OrderItem> orderItems = order.getProducts();
        for(OrderItem orderItem : orderItems) {
            OrderDetailKey orderDetailID = this.saveOrderDetail(orderID, orderItem);
            for(OrderItemDiscount discount: orderItem.getDiscounts()){
                this.saveOrderDetailDiscounts(orderDetailID, discount);
            }
        }
        return orderID;
    }
    // save order
    private String saveOrder(OrderWithProducts order) throws JsonProcessingException{
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
    @Transactional
    public Boolean validateAndProcessOrder(OrderWithProducts order) throws InvalidDiscountException, JsonProcessingException{
        log.info("validateAndProcessOrder({})", order.toString());
        if(order!=null && order.getProducts().size()>0){
            // store product's new instock
            List<Product> updateProductsInStock = new ArrayList<>();
            // get product in order
            List<OrderItem> products = order.getProducts();
            List<Integer> productIds = products.stream().map(OrderItem::getProductID).collect(Collectors.toList());
            List<Product> fetchedProducts = productService.getProducts(productIds);
            // validate discounts of products
            // get products and products's discount
            List<ProductDiscountsDTO> productDiscountsDTOs = order.getProducts().stream().map((product) -> {
                // get discountIDs
                List<String> discountIDs = product.getDiscounts().stream()
                    .map(OrderItemDiscount::getDiscountID).collect(Collectors.toList());
                // get applieddate
                LocalDateTime currenTime = null;
                if(!discountIDs.isEmpty()){
                    currenTime = product.getDiscounts().get(0).getAppliedDate();
                }
                return new ProductDiscountsDTO(product.getProductID(), discountIDs, currenTime);
            }).collect(Collectors.toList());
            // send and receive request 
            Boolean isValidDiscount = discountService.validateDiscount(productDiscountsDTOs);
            if(!isValidDiscount){
                throw new InvalidDiscountException("Discounts of Product is invalid");
            }
            // validate and update product inStock with order product quantity
            for(OrderItem product : products){
                Product fetchedProduct = findProduct(fetchedProducts, product.getProductID())
                    .orElseThrow(()-> new NotFoundException("Product "+product.getProductID()+" does not found"));
                // validate product's stock
                int orderQuantity = product.getQuantity();
                int inStocks = fetchedProduct.getUnitsInStock();
                if(orderQuantity>inStocks){
                    throw new RuntimeException("Product "+product.getProductName()+" does not have enough stocks in inventory");
                }
                // add product new in stock
                Product newProduct = new Product();
                newProduct.setProductID(product.getProductID());
                newProduct.setUnitsInStock(inStocks-orderQuantity);
                updateProductsInStock.add(newProduct);
            }
            // update product's instocks
            Boolean status = productService.updateProductInStocks(updateProductsInStock);
            if(!status) throw new RuntimeException("Can not update product in stocks");
            return true;
        }
        return false;
    }
    @Override
    public Boolean updateOrderEmployee(String orderID, String employeeID) {
        // orderRepository.findById(11071);
        Integer check = orderRepository.updateOrderEmployeeID(orderID, employeeID);
        if(check>0) return true;
        return false;
    }

    @Override
    public Boolean validateOrder(OrderWithProducts order) throws InvalidDiscountException, JsonProcessingException {
        log.info("validateOrder({})", order);
        try {
            // get discounts
            List<String> discountIDs =order.getProducts().stream()
                .flatMap(product -> product.getDiscounts().stream())
                .map(OrderItemDiscount::getDiscountID)
                .collect(Collectors.toList());
            List<DiscountDetail> fetchedDiscounts = discountService.getDiscounts(discountIDs);
            // get products
            List<OrderItem> products = order.getProducts();
            List<Integer> productIDs = products.stream().map(OrderItem::getProductID).collect(Collectors.toList());
            List<Product> fetchedProducts = productService.getProducts(productIDs);
            // product discount DTOs
            List<ProductDiscountsDTO> productDiscountsDTOs = new ArrayList<>();
            // validate products
            for (OrderItem product : products) {
                // check if the product exists or not
                Product fetchedProduct = findProduct(fetchedProducts, product.getProductID())
                    .orElseThrow(()-> new NotFoundException("Product "+ product.getProductID()+" does not found"));
                // set default value for product
                product.setUnitPrice(fetchedProduct.getUnitPrice());
                product.setPicture(fetchedProduct.getPicture());
                // check if product has any discount applied or not
                List<OrderItemDiscount> productDiscounts = product.getDiscounts().stream()
                    .filter(discount -> discount.getDiscountID()!=null)
                    .collect(Collectors.toList());
                product.setDiscounts(productDiscounts);
                // set and get discounts 
                if(product.getDiscounts().size()>0){
                    // validate number of discounts that can apply to 1 product
                    Integer numberOfDiscounts = product.getDiscounts().size();
                    if(numberOfDiscounts>WebConstant.MAX_APPLIED_DISCOUNT_PRODUCT){
                        throw new RuntimeException("Exceed the number of discount codes");
                    }
                    List<String> productDiscountIDs = new ArrayList<>();
                    for (OrderItemDiscount orderItemDiscount : product.getDiscounts()) {
                        DiscountDetail fetchedDiscount = this.findDiscount(fetchedDiscounts, orderItemDiscount.getDiscountID())
                            .orElseThrow(()-> new NotFoundException("Discount "+ orderItemDiscount.getDiscountID()+" does not found"));
                        orderItemDiscount.setDiscountPercent(fetchedDiscount.getDiscountPercent());
                        orderItemDiscount.setDiscountType(fetchedDiscount.getDiscountType());
            
                        productDiscountIDs.add(orderItemDiscount.getDiscountID());
                    }
                    LocalDateTime currentTime = product.getDiscounts().get(0).getAppliedDate();
                    ProductDiscountsDTO productDiscountsDTO = new ProductDiscountsDTO(product.getProductID(), productDiscountIDs, currentTime);
                    productDiscountsDTOs.add(productDiscountsDTO);
                }
            }
            // validate discount status
            Boolean isValidDiscount = discountService.validateDiscount(productDiscountsDTOs);
            if(!isValidDiscount) throw new InvalidDiscountException("Invalid discount");

            return true;
        } catch (InvalidDiscountException e) {
            log.error("Error: " + e.getMessage());
            return false;
        }
        
    }

    @Override
    public OrderWithProducts getPendingOrderDetail(String orderID) throws JsonProcessingException{
        log.info("getPendingOrderDetail(orderID={})", orderID);
        Order order = orderRepository.findByOrderIDAndStatus( orderID, OrderStatus.Pending)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " with pending status does not found"));
        OrderWithProducts convertedOrder = convertOrderService.convertOrderWithProducts(order);
        return convertedOrder;
    }

    @Override
    public OrderDetails getOrder(String orderID, OrderStatus status) throws JsonProcessingException {
        log.info("getOrder(orderID={}, status={})", orderID, status.name());
        List<OrderDetailExtended> orderDetailExtendeds = orderDetailExtendedRepository
            .findByOrderIDAndStatus(orderID, status);
        if(orderDetailExtendeds==null || orderDetailExtendeds.size()==0) 
            throw new NotFoundException("Order "+ orderID +" does not found");
        return convertOrderService.convertOrderDetail(orderDetailExtendeds);
    }

    @Override
    public OrderDetails getOrder(String orderID) throws JsonProcessingException {
        log.info("getOrder(orderID={})", orderID);
        List<OrderDetailExtended> orderDetailExtended = orderDetailExtendedRepository
            .findByOrderID(orderID);
        if(orderDetailExtended==null || orderDetailExtended.size()==0) 
            throw new NotFoundException("Order "+ orderID +" does not found");
        return convertOrderService.convertOrderDetail(orderDetailExtended);
    }

    @Override
    public Boolean isPendingOrder(String orderID) {
        log.info("isPendingOrder(OrderID={})", orderID);
        Order fetchedOrder = orderRepository.findById(orderID)
            .orElseThrow(()-> new NotFoundException("Order ID " + orderID + " does not found"));
        if(fetchedOrder.getStatus().equals(OrderStatus.Pending)) return true;
        return false;
    }

    @Override
    public Page<OrderDetails> getOrdersByCustomerID(String customerID, OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException {
        log.info("getOrdersByCustomerID(customerID={}, orderStatus={}, pageNumber={}, pageSize={})", 
            customerID, status, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByCustomerIDAndStatusOrderByDesc(customerID, status, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }

    @Override
    public InvoiceDetails getInvoiceByCustomerID(String customerID, String orderID) throws JsonProcessingException {
        log.info("getInvoiceByCustomerID(customerID={}, orderID={})", customerID, orderID);
        List<Invoice> invoices = invoiceRepository.findByOrderIDAndCustomerID(orderID, customerID);
        if(invoices==null || invoices.isEmpty()) 
            throw new NotFoundException("Invoice " + orderID + " of customer "+ customerID + " does not found");
        InvoiceDetails invoice = convertOrderService.convertInvoiceDetails(invoices);
        return invoice;
    }

    @Override
    public Page<OrderDetails> getOrdersByEmployeeID(String employeeID, OrderStatus status, Integer pageNumber,
            Integer pageSize) throws JsonProcessingException {
        log.info("getOrdersByEmployeeID(employeeID={}, status={}, pageNumber={}, pageSize={})", 
            employeeID, status, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByEmployeeIDAndStatusOrderByDesc(employeeID, status, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }

    @Override
    public OrderWithProducts getOrderByEmployeeID(String employeeID, String orderID) throws JsonProcessingException {
        log.info("getOrderByEmployeeID(employeeID={}, orderID={})", employeeID, orderID);
        Order order = orderRepository.findByEmployeeIDAndOrderID(employeeID, orderID)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " of employee "+ employeeID + " does not found"));
        return convertOrderService.convertOrderWithProducts(order);
    }

    // find product
    private Optional<Product> findProduct(List<Product> products, Integer productID){
        return products.stream().filter(p -> p.getProductID().equals(productID)).findFirst();
    }
    // find discount
    private Optional<DiscountDetail> findDiscount(List<DiscountDetail> discounts, String discountID){
        return discounts.stream().filter(d -> d.getDiscountID().equalsIgnoreCase(discountID)).findFirst();
    }

    @Override
    public Page<OrderDetails> getOrdersByCustomerID(String customerID, Integer pageNumber, Integer pageSize)
            throws JsonProcessingException {
        log.info("getOrdersByCustomerID(customerID={}, pageNumber={}, pageSize={})", 
            customerID, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByCustomerIDOrderByOrderIDDesc(customerID, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }

    @Override
    public Page<OrderDetails> getOrdersByEmployeeID(String employeeID, Integer pageNumber, Integer pageSize)
            throws JsonProcessingException {
        log.info("getOrdersByEmployeeID(employeeID={}, pageNumber={}, pageSize={})", 
            employeeID, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByEmployeeIDOrderByOrderIDDesc(employeeID, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }

    @Override
    public Page<OrderDetails> getOrders(OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException {
        log.info("getOrders(status={}, pageNumber={}, pageSize={})", status, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByStatusOrderByDesc(status, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }
}
