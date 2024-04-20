package com.phucx.account.service.order;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.phucx.account.compositeKey.OrderDetailsKey;
import com.phucx.account.constant.OrderStatus;
import com.phucx.account.constant.WebConstant;
import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.Discount;
import com.phucx.account.model.DiscountBreifInfo;
import com.phucx.account.model.Employees;
import com.phucx.account.model.Invoice;
import com.phucx.account.model.InvoiceDTO;
import com.phucx.account.model.OrderDetails;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderDetailsDiscounts;
import com.phucx.account.model.OrderDetailsExtendedStatus;
import com.phucx.account.model.OrderItem;
import com.phucx.account.model.OrderItemDiscount;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.Orders;
import com.phucx.account.model.ProductDTO;
import com.phucx.account.model.ProductWithBriefDiscount;
import com.phucx.account.model.Products;
import com.phucx.account.repository.CustomersRepository;
import com.phucx.account.repository.EmployeesRepository;
import com.phucx.account.repository.InvoicesRepository;
import com.phucx.account.repository.OrderDetailsDiscountsRepository;
import com.phucx.account.repository.OrderDetailsExtendedStatusRepository;
import com.phucx.account.repository.OrderDetailsRepository;
import com.phucx.account.repository.OrdersRepository;
import com.phucx.account.repository.ProductsRepository;
import com.phucx.account.repository.ShipperRepository;
import com.phucx.account.service.discounts.DiscountService;

import jakarta.ws.rs.NotFoundException;


@Service
public class OrderServiceImp implements OrderService{
    private Logger logger = LoggerFactory.getLogger(OrderServiceImp.class);
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private OrderDetailsDiscountsRepository orderDetailsDiscountsRepository;
    @Autowired
    private OrderDetailsExtendedStatusRepository orderDetailsExtendedStatusRepository;
    // @Autowired
    // private DiscountRepository discountRepository;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private EmployeesRepository employeesRepository;
    @Autowired
    private ShipperRepository shipperRepository;
    @Autowired
    private InvoicesRepository invoicesRepository;

    @Override
    public boolean updateOrderStatus(Integer orderID, OrderStatus status) {
        var orderOp = ordersRepository.findById(orderID);
        if(orderOp.isPresent()){
            Integer check = ordersRepository.updateOrderStatus(orderID, status); 
            if(check>0) return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Orders saveFullOrder(OrderWithProducts order) 
        throws NotFoundException, SQLException, RuntimeException, InvalidDiscountException{

        Orders newOrder = this.saveOrder(order);
        // save orderdetails
        List<OrderItem> orderItems = order.getProducts();
        for(OrderItem orderItem : orderItems) {
            OrderDetails newOrderDetail = this.saveOrderDetails(orderItem, newOrder);
            for(OrderItemDiscount discount: orderItem.getDiscounts()){
                // discount.setAppliedDate(order.getOrderDate());
                this.saveOrderDetailsDiscounts(discount, newOrderDetail);
            }
        }
        return newOrder;
    }
    // save order
    private Orders saveOrder(OrderWithProducts order) throws SQLException, RuntimeException, NotFoundException{
        var customerOp = customersRepository.findById(order.getCustomerID());
        Employees employee = null;
        if(order.getEmployeeID()!=null){
            var employeeOp = employeesRepository.findById(order.getEmployeeID());
            if(employeeOp.isPresent()) employee = employeeOp.get();
        }
        var shipOp = shipperRepository.findById(order.getShipVia());
        if(customerOp.isPresent() && shipOp.isPresent()){
            // save order
            Orders newOrder = new Orders(customerOp.get(), employee, 
                order.getOrderDate(), order.getRequiredDate(), order.getShippedDate(), 
                order.getShipVia(), order.getFreight(), order.getShipName(), 
                order.getShipAddress(), order.getShipCity(), OrderStatus.Pending);

            Orders checkOrder = ordersRepository.saveAndFlush(newOrder);
            if(checkOrder==null) throw new RuntimeException("Error while saving order");
            return checkOrder;
        }
        else throw new NotFoundException("Order missing");
    }
    // save orderDetails
    private OrderDetails saveOrderDetails(OrderItem orderItem, Orders order) 
        throws RuntimeException, NotFoundException, SQLException{
        logger.info("saveOrderDetails OrderItem:{}", orderItem.toString());
        var productOp = productsRepository.findById(orderItem.getProductID());
        if(productOp.isPresent()){
            Products product = productOp.get();
            OrderDetailsKey key = new OrderDetailsKey(product, order);
            
            OrderDetails orderDetail = new OrderDetails(
                key, product.getUnitPrice(), 
                orderItem.getQuantity());
            OrderDetails newOrderDetail = orderDetailsRepository.saveAndFlush(orderDetail);
            if(newOrderDetail==null) throw new RuntimeException("Error while saving orderdetail for order: "+ order.getOrderID());
            return newOrderDetail;
        }else{
            throw new NotFoundException("Product not found");
        }
    }
    // save orderDiscount
    private void saveOrderDetailsDiscounts(OrderItemDiscount orderItemDiscount, OrderDetails orderDetail)
        throws InvalidDiscountException, SQLException
    {
        logger.info("saveOrderDetailsDiscounts: orderItemDiscount:{}, orderDetail:{}", 
            orderItemDiscount.toString(), orderDetail.toString());
        Discount discount = discountService.getDiscount(orderItemDiscount.getDiscountID());
        // Discount discount = discountRepository.findById(orderItemDiscount.getDiscountID())
        //     .orElseThrow(() -> new InvalidDiscountException("Discount "+orderItemDiscount.getDiscountID()+" does not found"));
        OrderDetailsKey orderDetailsID = orderDetail.getKey();

        // set applied date for discount
        if(orderItemDiscount.getAppliedDate()==null){
            orderItemDiscount.setAppliedDate(LocalDateTime.now());
        }

        logger.info("InsertOrderDetailDiscount: {discountID={}, orderID={}, productID={}}", 
            discount.getDiscountID(), 
            orderDetailsID.getOrder().getOrderID(), 
            orderDetailsID.getProduct().getProductID());
        // save orderdetails discount
        // Boolean result = false;
        Boolean result = orderDetailsDiscountsRepository.insertOrderDetailDiscount(
           orderDetailsID.getOrder().getOrderID(),
           orderDetailsID.getProduct().getProductID(), 
           discount.getDiscountID(), 
           orderItemDiscount.getAppliedDate());

        if(result){
            throw new RuntimeException("Can not apply discount to this product");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean validateAndProcessOrder(OrderWithProducts order) throws RuntimeException, InvalidDiscountException{
        logger.info("validateAndProcessOrder({})", order.toString());
        if(order!=null && order.getProducts().size()>0){
            List<OrderItem> products = order.getProducts();
            Collections.sort(order.getProducts(), Comparator.comparingInt(OrderItem::getProductID));
            List<Integer> productIds = products.stream()
                .map(item -> item.getProductID()).collect(Collectors.toList());
            List<Products> fetchedProducts = productsRepository.findAllByIdAscending(productIds);
            // validate and update product inStock with order product quantity
            for(int i = 0 ;i<products.size();i++){
                Products product = fetchedProducts.get(i);
                // validate discount of product
                Boolean isValidDiscount = discountService.validateDiscountsOfProduct(products.get(i));
                if(!isValidDiscount){
                    throw new InvalidDiscountException("Discounts of Product " + products.get(i).getProductID() + " is invalid");
                }
                // validate product's stock
                int orderQuantity = products.get(i).getQuantity();
                int inStocks = product.getUnitsInStock();
                if(orderQuantity>inStocks){
                    throw new RuntimeException("Product "+product.getProductName()+
                        " does not have enough stocks in inventory");
                }
                // update product instocks
                Integer check = productsRepository.updateProductInStocks(
                    product.getProductID(), 
                    inStocks-orderQuantity);
                if(check<=0) throw new RuntimeException("Can not update product in stocks");
            }
            return true;
        }
        return false;
    }
    @Override
    public boolean updateOrderEmployee(Integer orderID, String employeeID) {
        // ordersRepository.findById(11071);
        Integer check = ordersRepository.updateOrderEmployeeID(orderID, employeeID);
        if(check>0) return true;
        return false;
    }
    @Override
    public Page<OrderWithProducts> getPendingOrders(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        // convert order and orderDetails data from database into OrderwithProduct as viewe data
        Page<Orders> ordersPageable = ordersRepository.findByStatus(OrderStatus.Pending, pageable);
        logger.info("order: {}", ordersPageable.toString());
        List<Orders> orders =  ordersPageable.getContent();

        List<OrderWithProducts> result = orders.stream().map(order ->{
            logger.info("orderID: {}", order.getOrderID());
            return this.getOrderDetail(order);
        }).collect(Collectors.toList());

        Page<OrderWithProducts> resultPageable = new PageImpl<>(result, pageable, result.size());
        return resultPageable;
    }

    @Override
    public boolean isPendingOrder(Integer orderID) throws InvalidOrderException {
        if(orderID==null) throw new InvalidOrderException("OrderID is null");
        return ordersRepository.findById(orderID)
        .map(order -> order.getStatus().equals(OrderStatus.Pending))
        .orElse(false);
    }

    @Override
    public boolean validateOrder(OrderWithProducts order) throws InvalidDiscountException {
        try {
            String customerID = order.getCustomerID();
            customersRepository.findById(customerID)
                .orElseThrow(()-> new NotFoundException("Customer not found"));
            List<OrderItem> products = order.getProducts();

            
            for (OrderItem product : products) {
                productsRepository.findById(product.getProductID())
                    .orElseThrow(()-> new NotFoundException("Product "+ product.getProductID()+" does not found"));

                Integer numberOfDiscounts = product.getDiscounts().size();
                if(numberOfDiscounts>WebConstant.MAX_APPLIED_DISCOUNT_PRODUCT){
                    throw new RuntimeException("Exceed the number of discount codes");
                }
                boolean isValidDiscount = discountService.validateDiscountsOfProduct(product);
                if(!isValidDiscount) throw new InvalidDiscountException("Invalid discount");

            }
            return true;
        } catch (InvalidDiscountException e) {
            logger.error("Error occurs: " + e.getMessage());
            return false;
        }
        
    }

    @Override
    public InvoiceDTO getOrderDetail(int orderID, String customerID) throws InvalidOrderException {
        logger.info("getOrderDetail(orderID={}, customerID={})", orderID, customerID);
        // fetch invoice
        List<Invoice> fetchedInvoices = invoicesRepository.findByOrderIDAndCustomerIDOrderByProductIDAsc(orderID, customerID);
        if(fetchedInvoices==null || fetchedInvoices.isEmpty()) 
            throw new InvalidOrderException("Order " + orderID + " does not found");

        Invoice invoice = fetchedInvoices.get(0);
        
        InvoiceDTO invoiceDTO = new InvoiceDTO(
            invoice.getOrderID(), invoice.getCustomerID(), invoice.getSalesperson(), 
            invoice.getShipName(), invoice.getShipAddress(), invoice.getShipCity(), 
            invoice.getOrderDate(), invoice.getRequiredDate(), invoice.getShippedDate(), 
            invoice.getShipperName(), invoice.getFreight(), OrderStatus.fromString(invoice.getStatus()));

        // convert invoice of customer
        List<ProductWithBriefDiscount> products = invoiceDTO.getProducts();
        for(Invoice tempInvoice: fetchedInvoices){
            // add new product
            if(products.size()==0 || !products.get(products.size()-1).getProductID().equals(tempInvoice.getProductID())){
                ProductWithBriefDiscount productWithBriefDiscount = new ProductWithBriefDiscount(
                    tempInvoice.getProductID(), tempInvoice.getProductName(), tempInvoice.getUnitPrice(), 
                    tempInvoice.getQuantity(), tempInvoice.getPicture(), tempInvoice.getExtendedPrice());
                // set totalprice for invoice
                invoiceDTO.setTotalPrice(tempInvoice.getExtendedPrice()+invoiceDTO.getTotalPrice());
                products.add(productWithBriefDiscount);
            }
            // add new discount for product
            if(tempInvoice.getDiscountID()!=null){
                DiscountBreifInfo discountBreifInfo = new DiscountBreifInfo(
                    tempInvoice.getDiscountID(), 
                    tempInvoice.getDiscountPercent(), 
                    tempInvoice.getDiscountType());
                products.get(products.size()-1).getDiscounts().add(discountBreifInfo);
                products.get(products.size()-1).setTotalDiscount(products.get(products.size()-1).getTotalDiscount() + tempInvoice.getDiscountPercent());
            }
        }
        logger.info("getTotalPrice({})", invoiceDTO.getTotalPrice());
        return invoiceDTO;
    }

    // convert orderDetail
    private OrderWithProducts getOrderDetail(Orders order){
        OrderWithProducts orderWithProducts = new OrderWithProducts(order);
        List<OrderItem> orderItems = orderDetailsRepository.findByOrderID(order.getOrderID())
            .stream().map(orderDetail ->{
                Integer orderID = orderDetail.getKey().getOrder().getOrderID();
                Integer productID = orderDetail.getKey().getProduct().getProductID();
                logger.info("orderID: {}, productID: {}", orderID, productID);
                List<OrderDetailsDiscounts> orderDetailsDiscounts = orderDetailsDiscountsRepository
                    .findByOrderDetail(orderID, productID);

                logger.info("orderDetailsDiscounts: {}", orderDetailsDiscounts.toString());
                List<OrderItemDiscount> discounts = orderDetailsDiscounts.stream()
                    .map(orderDetailsDiscount ->{
                        OrderItemDiscount discount = new OrderItemDiscount(
                            orderDetailsDiscount.getId().getDiscount().getDiscountID(), 
                            orderDetailsDiscount.getAppliedDate());
                        return discount;
                    }).collect(Collectors.toList());
                OrderItem orderItem = new OrderItem(
                    orderDetail.getKey().getProduct().getProductID(), 
                    orderDetail.getQuantity(), 
                    discounts);
                return orderItem;
            }).collect(Collectors.toList());
        orderWithProducts.setProducts(orderItems);
        return orderWithProducts;
    }

    @Override
    public Page<OrderDetailsDTO> getOrders(Integer pageNumber, Integer pageSize, String customerID, OrderStatus orderStatus) {
        logger.info("getOrders(pageNumber={}, pageSize={}, customerID={}, orderStatus={})", 
            pageNumber, pageSize, customerID, orderStatus);
        // get orders 
        Pageable page = PageRequest.of(pageNumber, pageSize);   
        Page<OrderDetailsExtendedStatus> orders = orderDetailsExtendedStatusRepository
            .findAllByCustomerIDAndStatusOrderByDesc(customerID, orderStatus, page);

        // convert OrderDetailsExtendedStatus to OrderDetailsDTO
        List<OrderDetailsDTO> orderDTOs = convertOrders(orders.getContent());

        Page<OrderDetailsDTO> result = new PageImpl<>(orderDTOs, orders.getPageable(), orders.getTotalElements());
        return result;
    }

    @Override
    public Page<OrderDetailsDTO> getOrders(Integer pageNumber, Integer pageSize, String customerID) {
        // get orders 
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailsExtendedStatus> orders = orderDetailsExtendedStatusRepository
            .findAllByCustomerIDOrderByDesc(customerID, page);
        // convert orders
        List<OrderDetailsDTO> orderDTOs = this.convertOrders(orders.getContent());  
        return new PageImpl<>(orderDTOs, orders.getPageable(), orders.getTotalElements());
    }
    // convert orders
    private List<OrderDetailsDTO> convertOrders(List<OrderDetailsExtendedStatus> orders){
        List<OrderDetailsDTO> orderDTOs = new ArrayList<>();
        for(OrderDetailsExtendedStatus order: orders){
            // add new OrderDetailsDTO to result list when it meets a new orderID
            if(orderDTOs.size()==0 || !orderDTOs.get(orderDTOs.size()-1).getOrderID().equals(order.getOrderID())){
                OrderDetailsDTO orderDTO = new OrderDetailsDTO();
                orderDTO.setOrderID(order.getOrderID());
                orderDTO.setStatus(order.getStatus());
                orderDTOs.add(orderDTO);
            }
            // add new product to the newest OrderDetails
            ProductDTO productDTO = new ProductDTO(order.getProductID(), order.getProductName(), 
                order.getUnitPrice(), order.getQuantity(), order.getDiscount(), order.getExtendedPrice(),
                order.getPicture());
            orderDTOs.get(orderDTOs.size()-1).getProducts().add(productDTO);
            orderDTOs.get(orderDTOs.size()-1).setTotalPrice(orderDTOs.get(orderDTOs.size()-1).getTotalPrice()+order.getExtendedPrice());
        }

        return orderDTOs;
    }
    
}
