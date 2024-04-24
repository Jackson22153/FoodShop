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
import com.phucx.account.model.Customers;
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
import com.phucx.account.model.Order;
import com.phucx.account.model.ProductDTO;
import com.phucx.account.model.ProductWithBriefDiscount;
import com.phucx.account.model.Products;
import com.phucx.account.model.Shipper;
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
    public Order saveFullOrder(OrderWithProducts order) 
        throws NotFoundException, SQLException, RuntimeException, InvalidDiscountException{

        Order newOrder = this.saveOrder(order);
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
    private Order saveOrder(OrderWithProducts order) throws SQLException, RuntimeException, NotFoundException{
        logger.info("saveOrder({})", order);
        // fetch order's information
        Customers customer = customersRepository.findById(order.getCustomerID())
            .orElseThrow(()-> new NotFoundException("Customer " + order.getCustomerID()+" does not found"));
        Employees employee = null;
        if(order.getEmployeeID()!=null){
            employee = employeesRepository.findById(order.getEmployeeID()).orElse(null);
        }
        Shipper shipper = shipperRepository.findById(order.getShipVia())
            .orElseThrow(()-> new NotFoundException("Shipper " + order.getShipVia()+ " does not found"));
        // save order
        Order newOrder = new Order(customer, employee, order.getOrderDate(), order.getRequiredDate(), 
            order.getShippedDate(), shipper, order.getFreight(), order.getShipName(), order.getShipAddress(), 
            order.getShipCity(), order.getPhone(), OrderStatus.Pending);
    
        logger.info("customer: {}", customer);
        logger.info("Shipper: {}", shipper);
        logger.info("employee: {}", employee);

        Order checkOrder = ordersRepository.saveAndFlush(newOrder);
        if(checkOrder==null) throw new RuntimeException("Error while saving order");
        return checkOrder;
    }
    // save orderDetails
    private OrderDetails saveOrderDetails(OrderItem orderItem, Order order) 
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

        if(!result){
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
    public Page<OrderDetailsDTO> getEmployeeOrders(Integer pageNumber, Integer pageSize, String employeeID, OrderStatus orderStatus) {
        // logger.info("getEmployeeOrders(pageNumber={}, pageSize={})", pageNumber, pageSize);
        // Pageable pageable = PageRequest.of(pageNumber, pageSize);
        // // convert order and orderDetails data from database into OrderwithProduct as viewe data
        // Page<Orders> ordersPageable = ordersRepository.findByStatusAndEmployeeID(status, employeeID, pageable);
        // logger.info("order: {}", ordersPageable.toString());
        // List<Orders> orders =  ordersPageable.getContent();

        // List<OrderWithProducts> result = orders.stream().map(order ->{
        //     logger.info("orderID: {}", order.getOrderID());
        //     return this.getOrderDetail(order);
        // }).collect(Collectors.toList());

        // Page<OrderWithProducts> resultPageable = new PageImpl<>(result, pageable, result.size());
        // return resultPageable;

        logger.info("getEmployeeOrders(pageNumber={}, pageSize={}, employeeID={}, orderStatus={})", 
            pageNumber, pageSize, employeeID, orderStatus);
        // get orders 
        Pageable page = PageRequest.of(pageNumber, pageSize);   
        Page<OrderDetailsExtendedStatus> orders = orderDetailsExtendedStatusRepository
            .findAllByEmployeeIDAndStatusOrderByDesc(employeeID, orderStatus, page);

        // convert OrderDetailsExtendedStatus to OrderDetailsDTO
        List<OrderDetailsDTO> orderDTOs = convertOrders(orders.getContent());

        Page<OrderDetailsDTO> result = new PageImpl<>(orderDTOs, orders.getPageable(), orders.getTotalElements());
        return result;
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
    public InvoiceDTO getCustomerInvoice(int orderID, String customerID) throws InvalidOrderException {
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
    private OrderWithProducts getOrderDetail(Order order){
        Customers customer = order.getCustomer();
        Shipper shipper = order.getShipVia();
        Employees employee = order.getEmployee();
        OrderWithProducts orderWithProducts = new OrderWithProducts(
            order.getOrderID(), customer.getCustomerID(), customer.getContactName(), 
            employee!=null?employee.getEmployeeID():null, employee!=null?
            employee.getFirstName() + " " + employee.getLastName():null, order.getOrderDate(), 
            order.getRequiredDate(), order.getShippedDate(), shipper.getShipperID(), 
            shipper.getCompanyName(), shipper.getPhone(), order.getFreight(), 
            order.getShipName(), order.getShipAddress(), order.getShipCity(), order.getPhone(), 
            order.getStatus());

        List<OrderDetails> orderDetails = orderDetailsRepository.findByOrderID(order.getOrderID());
        // calculate totalprice of an order
        Double totalPrice = Double.valueOf(0);
        for(OrderDetails orderDetail: orderDetails){
            // create a product for order
            OrderItem orderItem = new OrderItem(
                orderDetail.getKey().getProduct().getProductID(),
                orderDetail.getKey().getProduct().getProductName(),
                orderDetail.getQuantity(), 
                orderDetail.getKey().getProduct().getPicture(), 
                orderDetail.getUnitPrice());
            Integer totalDiscount = Integer.valueOf(0);
            // fetch product'discounts
            Integer orderID = orderDetail.getKey().getOrder().getOrderID();
            Integer productID = orderDetail.getKey().getProduct().getProductID();
            List<OrderDetailsDiscounts> orderDetailsDiscounts = orderDetailsDiscountsRepository
                .findByOrderDetail(orderID, productID);
            // add discount
            List<OrderItemDiscount> discounts = new ArrayList<>();
            for(OrderDetailsDiscounts orderDetailsDiscount: orderDetailsDiscounts){
                OrderItemDiscount discount = new OrderItemDiscount(
                    orderDetailsDiscount.getId().getDiscount().getDiscountID(), 
                    orderDetailsDiscount.getAppliedDate(), 
                    orderDetailsDiscount.getDiscountPercent(), 
                    orderDetailsDiscount.getId().getDiscount().getDiscountType().getDiscountType());
                // add total discounts
                totalDiscount +=orderDetailsDiscount.getDiscountPercent();
                discounts.add(discount);
            }
  
            // add discounts and price of each product
            orderItem.setDiscounts(discounts);
            Double extendedPrice = (Double.valueOf(orderDetail.getQuantity()) * orderDetail.getUnitPrice())*(1- Double.valueOf(totalDiscount) /100) ;
            totalPrice+=extendedPrice;
            // logger.info("totalPrice {}", totalPrice);
            orderItem.setExtendedPrice(extendedPrice);
            // add product to order
            orderWithProducts.getProducts().add(orderItem);
        }
        orderWithProducts.setTotalPrice(totalPrice);
        logger.info("OrderWithProducts: {}", orderWithProducts.toString());
        return orderWithProducts;
    }

    @Override
    public Page<OrderDetailsDTO> getCustomerOrders(Integer pageNumber, Integer pageSize, String customerID, OrderStatus orderStatus) {
        logger.info("getCustomerOrders(pageNumber={}, pageSize={}, customerID={}, orderStatus={})", 
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
    public Page<OrderDetailsDTO> getCustomerOrders(Integer pageNumber, Integer pageSize, String customerID) {
        logger.info("getCustomerOrders(pageNumber={}, pageSize={}, customerID={})", pageNumber, pageSize, customerID);
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

    @Override
    public Page<OrderDetailsDTO> getPendingOrders(int pageNumber, int pageSize) {
        logger.info("getPendingOrders(pageNumber={}, pageSize={})", pageNumber, pageSize);
        // get orders 
        Pageable page = PageRequest.of(pageNumber, pageSize);   
        Page<OrderDetailsExtendedStatus> orders = orderDetailsExtendedStatusRepository
            .findAllByStatusOrderByDesc(OrderStatus.Pending, page);

        // convert OrderDetailsExtendedStatus to OrderDetailsDTO
        List<OrderDetailsDTO> orderDTOs = convertOrders(orders.getContent());

        Page<OrderDetailsDTO> result = new PageImpl<>(orderDTOs, orders.getPageable(), orders.getTotalElements());
        return result;
    }

    @Override
    public OrderWithProducts getEmployeeOrderDetail(int orderID, String employeeID) throws InvalidOrderException {
        logger.info("getEmployeeOrderDetail(orderID={}, EmployeeID={})", orderID, employeeID);
        Order order = ordersRepository.findByEmployeeIDAndOrderID(employeeID, orderID)
            .orElseThrow(()-> new InvalidOrderException("Order " + orderID + " is invalid"));
        logger.info("order: {}", order);
        OrderWithProducts convertedOrder = this.getOrderDetail(order);
        return convertedOrder;
    }

    @Override
    public OrderWithProducts getPendingOrderDetail(int orderID) throws InvalidOrderException{
        logger.info("getPendingOrderDetail(orderID={})", orderID);
        Order order = ordersRepository.findByStatusAndOrderID(OrderStatus.Pending, orderID)
            .orElseThrow(()-> new InvalidOrderException("Order " + orderID + " is invalid"));
        OrderWithProducts convertedOrder = this.getOrderDetail(order);
        return convertedOrder;
    }

    @Override
    public Page<OrderDetailsDTO> getEmployeeOrders(Integer pageNumber, Integer pageSize, String employeeID) {
        logger.info("getEmployeeOrders(pageNumber={}, pageSize={}, customerID={})", pageNumber, pageSize, employeeID);
        // get orders 
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailsExtendedStatus> orders = orderDetailsExtendedStatusRepository
            .findAllByEmployeeIDOrderByDesc(employeeID, page);
        // convert orders
        List<OrderDetailsDTO> orderDTOs = this.convertOrders(orders.getContent());  
        return new PageImpl<>(orderDTOs, orders.getPageable(), orders.getTotalElements());
    }
    
}
