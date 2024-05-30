package com.phucx.order.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import com.phucx.order.model.DiscountBreifInfo;
import com.phucx.order.model.DiscountDetail;
import com.phucx.order.model.Employee;
import com.phucx.order.model.Invoice;
import com.phucx.order.model.InvoiceDTO;
import com.phucx.order.model.OrderItem;
import com.phucx.order.model.OrderItemDiscount;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Order;
import com.phucx.order.model.OrderDetail;
import com.phucx.order.model.OrderDetailDTO;
import com.phucx.order.model.OrderDetailDiscount;
import com.phucx.order.model.OrderDetailExtended;
import com.phucx.order.model.ProductDTO;
import com.phucx.order.model.ProductDiscountsDTO;
import com.phucx.order.model.ProductWithBriefDiscount;
import com.phucx.order.model.Shipper;
import com.phucx.order.model.Product;
import com.phucx.order.repository.InvoiceRepository;
import com.phucx.order.repository.OrderDetailDiscountRepository;
import com.phucx.order.repository.OrderDetailExtendedRepository;
import com.phucx.order.repository.OrderDetailRepository;
import com.phucx.order.repository.OrderRepository;
import com.phucx.order.service.bigdecimal.BigDecimalService;
import com.phucx.order.service.customer.CustomerService;
import com.phucx.order.service.discount.DiscountService;
import com.phucx.order.service.employee.EmployeeService;
import com.phucx.order.service.product.ProductService;
import com.phucx.order.service.shipper.ShipperService;

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
    private EmployeeService employeeService;
    @Autowired
    private ShipperService shipperService;
    @Autowired
    private InvoiceRepository invoiceRepository;

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
            orderDetailID.getOrderID(), orderDetailID.getProductID(), orderItemDiscount.getDiscountID(), currentTime);
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
                List<String> discountIDs = product.getDiscounts().stream()
                    .map(OrderItemDiscount::getDiscountID).collect(Collectors.toList());
                return new ProductDiscountsDTO(product.getProductID(), discountIDs);
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
            // get products
            List<OrderItem> products = order.getProducts();
            List<Integer> productIDs = products.stream().map(OrderItem::getProductID).collect(Collectors.toList());
            List<Product> fetchedProducts = productService.getProducts(productIDs);
            for (OrderItem product : products) {
                // check if the product exists or not
                findProduct(fetchedProducts, product.getProductID())
                    .orElseThrow(()-> new NotFoundException("Product "+ product.getProductID()+" does not found"));
                // check if product has any discount applied or not
                List<OrderItemDiscount> productDiscounts = product.getDiscounts().stream()
                    .filter(discount -> discount.getDiscountID()!=null)
                    .collect(Collectors.toList());
                product.setDiscounts(productDiscounts);
                if(product.getDiscounts().size()>0){
                    // validate number of discounts that can apply to 1 product
                    Integer numberOfDiscounts = product.getDiscounts().size();
                    if(numberOfDiscounts>WebConstant.MAX_APPLIED_DISCOUNT_PRODUCT){
                        throw new RuntimeException("Exceed the number of discount codes");
                    }
                }
            }
            // get products and products's discount
            List<ProductDiscountsDTO> productDiscountsDTOs = order.getProducts().stream().map((product) -> {
                List<String> discountIDs = product.getDiscounts().stream()
                    .map(OrderItemDiscount::getDiscountID).collect(Collectors.toList());
                return new ProductDiscountsDTO(product.getProductID(), discountIDs);
            }).collect(Collectors.toList());
            Boolean isValidDiscount = discountService.validateDiscount(productDiscountsDTOs);
            // check discount status
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
        OrderWithProducts convertedOrder = this.getOrderWithProducts(order);
        return convertedOrder;
    }

    // convert orders to list orderDetailDTO
    private List<OrderDetailDTO> convertOrders(List<OrderDetailExtended> orders) throws JsonProcessingException{
        // fetch products
        List<Integer> productIds = orders.stream().map(OrderDetailExtended::getProductID).collect(Collectors.toList());
        List<Product> fetchedProducts = this.productService.getProducts(productIds);
        // fetch customer
        Set<String> setCustomerId = orders.stream().map(OrderDetailExtended::getCustomerID).collect(Collectors.toSet());
        List<String> listCustomerId = new ArrayList<>(setCustomerId);
        List<Customer> fetchedCustomers = this.customerService.getCustomersByIDs(listCustomerId);
        // create order
        List<OrderDetailDTO> orderDTOs = new ArrayList<>();
        for(OrderDetailExtended order: orders){
            // add new OrderDetailDTO to result list when it meets a new orderID
            if(orderDTOs.size()==0 || !orderDTOs.get(orderDTOs.size()-1).getOrderID().equals(order.getOrderID())){
                // find customer
                Customer customer = this.findCustomer(fetchedCustomers, order.getCustomerID())
                    .orElseThrow(()-> new NotFoundException("Customer " + order.getCustomerID() + " does not found"));
                // create an order
                OrderDetailDTO orderDTO = new OrderDetailDTO();
                orderDTO.setOrderID(order.getOrderID());
                orderDTO.setStatus(order.getStatus());
                // set customer
                orderDTO.setCustomerID(customer.getCustomerID());
                orderDTO.setContactName(customer.getContactName());
                orderDTO.setPicture(customer.getPicture());
                // add order to a list of order
                orderDTOs.add(orderDTO);
            }
            Product product = findProduct(fetchedProducts, order.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + order.getProductID() + " does not found"));
            // add new product to the newest OrderDetail
            ProductDTO productDTO = new ProductDTO(order.getProductID(), product.getProductName(), 
                order.getUnitPrice(), order.getQuantity(), order.getDiscount(), order.getExtendedPrice(),
                product.getPicture());
            orderDTOs.get(orderDTOs.size()-1).getProducts().add(productDTO);
            orderDTOs.get(orderDTOs.size()-1).setTotalPrice(
                orderDTOs.get(orderDTOs.size()-1).getTotalPrice().add(order.getExtendedPrice()));
            
        }

        return orderDTOs;
    }

    @Override
    public OrderDetailDTO getOrder(String orderID, OrderStatus status) throws JsonProcessingException {
        log.info("getOrder(orderID={}, status={})", orderID, status.name());
        List<OrderDetailExtended> orderDetailExtendeds = orderDetailExtendedRepository
            .findByOrderIDAndStatus(orderID, status);
        if(orderDetailExtendeds==null || orderDetailExtendeds.size()==0) 
            throw new NotFoundException("Order "+ orderID +" does not found");
        return this.convertOrderDetail(orderDetailExtendeds);
    }

    @Override
    public OrderDetailDTO getOrder(String orderID) throws JsonProcessingException {
        log.info("getOrder(orderID={})", orderID);
        List<OrderDetailExtended> orderDetailExtended = orderDetailExtendedRepository
            .findByOrderID(orderID);
        if(orderDetailExtended==null || orderDetailExtended.size()==0) 
            throw new NotFoundException("Order "+ orderID +" does not found");
        return this.convertOrderDetail(orderDetailExtended);
    }
    // convert from one specific orderID to an OrderDetailDTO
    private OrderDetailDTO convertOrderDetail(List<OrderDetailExtended> orderProducts) throws JsonProcessingException{
        // get the first element inside orderproducts
        OrderDetailExtended firstElement = orderProducts.get(0);

        // get customer
        String customerID = firstElement.getCustomerID();
        Customer fetchedCustomer = customerService.getCustomerByID(customerID);
        if(fetchedCustomer==null) throw new NotFoundException("Customer " + customerID + " does not found");
        // get products
        List<Integer> productIds = orderProducts.stream().map(OrderDetailExtended::getProductID).collect(Collectors.toList());
        List<Product> fetchedProducts = productService.getProducts(productIds);
        
        // create an orderdetailDTO instance
        OrderDetailDTO order = new OrderDetailDTO();
        order.setOrderID(firstElement.getOrderID());
        order.setStatus(firstElement.getStatus());
        // set employee of order
        order.setEmployeeID(firstElement.getEmployeeID());
        // set customer of order
        order.setCustomerID(fetchedCustomer.getCustomerID());
        order.setContactName(fetchedCustomer.getContactName());
        order.setPicture(fetchedCustomer.getPicture());
        // set product for order
        for(OrderDetailExtended orderDetailExtended: orderProducts){
            Product product = this.findProduct(fetchedProducts, orderDetailExtended.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + orderDetailExtended.getProductID() + " does not found"));
            // add new product to the newest OrderDetail
            ProductDTO productDTO = new ProductDTO(
                orderDetailExtended.getProductID(), product.getProductName(), 
                orderDetailExtended.getUnitPrice(), orderDetailExtended.getQuantity(), 
                orderDetailExtended.getDiscount(), orderDetailExtended.getExtendedPrice(),
                product.getPicture());
            // add products to order
            order.getProducts().add(productDTO);
            // set order totalPrice
            order.setTotalPrice(order.getTotalPrice().add(orderDetailExtended.getExtendedPrice()));
        }
        return order;
    }
    
    // convert order to orderWithProducts
    private OrderWithProducts getOrderWithProducts(Order order) throws JsonProcessingException{
        // fetch infomation
        // get customer
        String customerID = order.getCustomerID();
        Customer fetchedCustomer = customerService.getCustomerByID(customerID);
        if(fetchedCustomer==null) throw new NotFoundException("Customer " + customerID + " does not found");
        // get employee
        Employee fetchedEmployee = null;
        if(order.getEmployeeID()!=null){
            fetchedEmployee = employeeService.getEmployeeByID(order.getEmployeeID());
            if(fetchedEmployee==null) throw new NotFoundException("Employee " + order.getEmployeeID() + " does not found");
        }
        // get shipper
        Integer shipperID = order.getShipVia();
        Shipper fetchedShipper = shipperService.getShipper(shipperID);
        if(fetchedShipper==null) throw new NotFoundException("Shipper " + shipperID + " does not found");
        // get products
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderID(order.getOrderID());
        List<Integer> productIds = orderDetails.stream().map(OrderDetail::getProductID).collect(Collectors.toList());
        List<Product> products = productService.getProducts(productIds);

        // create and order
        OrderWithProducts orderWithProducts = new OrderWithProducts(
            order.getOrderID(), customerID, fetchedCustomer.getContactName(), 
            fetchedEmployee!=null?fetchedEmployee.getEmployeeID():null, fetchedEmployee!=null?
            fetchedEmployee.getFirstName() + " " + fetchedEmployee.getLastName():null, order.getOrderDate(), 
            order.getRequiredDate(), order.getShippedDate(), shipperID, 
            fetchedShipper.getCompanyName(), fetchedShipper.getPhone(), order.getFreight(), 
            order.getShipName(), order.getShipAddress(), order.getShipCity(), order.getPhone(), 
            order.getStatus());

        
        for(OrderDetail orderDetail: orderDetails){
            // find product
            Product product = this.findProduct(products, orderDetail.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + orderDetail.getProductID() + " does not found"));
            // create a product for order
            OrderItem orderItem = new OrderItem(
                orderDetail.getProductID(),
                product.getProductName(),
                orderDetail.getQuantity(), 
                product.getPicture(), 
                orderDetail.getUnitPrice());
            Integer totalDiscount = Integer.valueOf(0);
            // fetch product'discounts
            String orderID = orderDetail.getOrderID();
            Integer productID = orderDetail.getProductID();
            List<OrderDetailDiscount> orderDetailDiscounts = orderDetailDiscountRepository
                .findByOrderDetail(orderID, productID);
            // get discounts of product
            List<String> discountIds = orderDetailDiscounts.stream().map(OrderDetailDiscount::getDiscountID).collect(Collectors.toList());
            List<DiscountDetail> fetchedDiscounts = discountService.getDiscounts(discountIds);
            // add discount
            List<OrderItemDiscount> discounts = new ArrayList<>();
            for(OrderDetailDiscount orderDetailDiscount: orderDetailDiscounts){
                // find discount
                DiscountDetail fetchedDiscount = this.findDiscount(fetchedDiscounts, orderDetailDiscount.getDiscountID())
                    .orElseThrow(()-> new NotFoundException("Discount "+orderDetailDiscount.getDiscountID() + " does not found"));
                OrderItemDiscount discount = new OrderItemDiscount(
                    orderDetailDiscount.getDiscountID(), 
                    orderDetailDiscount.getAppliedDate(), 
                    orderDetailDiscount.getDiscountPercent(), 
                    fetchedDiscount.getDiscountType());
                // add total discounts
                totalDiscount +=orderDetailDiscount.getDiscountPercent();
                discounts.add(discount);
            }
  
            // add discounts and price of each product
            orderItem.setDiscounts(discounts);
            BigDecimal productDiscount = BigDecimal.valueOf(1- Double.valueOf(totalDiscount) /100);
            
            BigDecimal extendedPrice = BigDecimal.valueOf(orderDetail.getQuantity())
                .multiply(orderDetail.getUnitPrice())
                .multiply(productDiscount);

            orderWithProducts.setTotalPrice(orderWithProducts.getTotalPrice().add(extendedPrice));
            // log.info("totalPrice {}", totalPrice);
            orderItem.setExtendedPrice(extendedPrice);
            // add product to order
            orderWithProducts.getProducts().add(orderItem);
        }
        log.info("OrderWithProducts: {}", orderWithProducts.toString());
        return orderWithProducts;
    }

    // convert order to invoiceDTO
    private InvoiceDTO getInvoiceDTO(List<Invoice> invoices) throws JsonProcessingException{
        // fetch invoice
        Invoice invoice = invoices.get(0);
        // fetch employee
        Employee fetchedEmployee = this.employeeService.getEmployeeByID(invoice.getEmployeeID());
        if(fetchedEmployee==null) throw new NotFoundException("Employee " + invoice.getEmployeeID() + " does not found");
        String salesperson = fetchedEmployee.getLastName() + " " + fetchedEmployee.getFirstName();
        // fetch shipper
        Shipper fetchedShipper = this.shipperService.getShipper(invoice.getShipperID());
        if(fetchedShipper==null) throw new NotFoundException("Shipper " + invoice.getShipperID() + " does not found");
        // fetch products
        List<Integer> productIds = invoices.stream().map(Invoice::getProductID).collect(Collectors.toList());
        List<Product> fetchedProducts = this.productService.getProducts(productIds);
        // fetch discounts
        List<String> discountIds = invoices.stream().map(Invoice::getDiscountID).collect(Collectors.toList());
        List<DiscountDetail> fetchedDiscounts = this.discountService.getDiscounts(discountIds);
        // create an invoice
        InvoiceDTO invoiceDTO = new InvoiceDTO(
            invoice.getOrderID(), invoice.getCustomerID(), invoice.getEmployeeID(), 
            salesperson, invoice.getShipName(), invoice.getShipAddress(), 
            invoice.getShipCity(), invoice.getPhone(), invoice.getOrderDate(), 
            invoice.getRequiredDate(), invoice.getShippedDate(), fetchedShipper.getCompanyName(), 
            invoice.getFreight(), invoice.getStatus()); 
        // convert invoice of customer
        List<ProductWithBriefDiscount> products = invoiceDTO.getProducts();
        for(Invoice tempInvoice: invoices){
            // find product
            Product product = findProduct(fetchedProducts, tempInvoice.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + tempInvoice.getProductID() + " does not found"));
            // add new product
            if(products.size()==0 || !products.get(products.size()-1).getProductID().equals(tempInvoice.getProductID())){
                ProductWithBriefDiscount productWithBriefDiscount = new ProductWithBriefDiscount(
                    tempInvoice.getProductID(), product.getProductName(), tempInvoice.getUnitPrice(), 
                    tempInvoice.getQuantity(), product.getPicture(), tempInvoice.getExtendedPrice());
                // set totalprice for invoice
                invoiceDTO.setTotalPrice(tempInvoice.getExtendedPrice().add(invoiceDTO.getTotalPrice()));
                products.add(productWithBriefDiscount);
            }
            // add new discount for product
            if(tempInvoice.getDiscountID()!=null){
                // find discount
                DiscountDetail discount = this.findDiscount(fetchedDiscounts, tempInvoice.getDiscountID())
                    .orElseThrow(()-> new NotFoundException("Discount " + tempInvoice.getDiscountID() + " does not found"));
                DiscountBreifInfo discountBreifInfo = new DiscountBreifInfo(
                    tempInvoice.getDiscountID(), 
                    tempInvoice.getDiscountPercent(), 
                    discount.getDiscountType());
                products.get(products.size()-1).getDiscounts().add(discountBreifInfo);
                products.get(products.size()-1).setTotalDiscount(products.get(products.size()-1).getTotalDiscount() + tempInvoice.getDiscountPercent());
            }
        }
        return invoiceDTO;
    }


    // find product
    private Optional<Product> findProduct(List<Product> products, Integer productID){
        return products.stream().filter(p -> p.getProductID().equals(productID)).findFirst();
    }
    // find discount
    private Optional<DiscountDetail> findDiscount(List<DiscountDetail> discounts, String discountID){
        return discounts.stream().filter(p -> p.getDiscountID().equalsIgnoreCase(discountID)).findFirst();
    }
    // find customer
    private Optional<Customer> findCustomer(List<Customer> customers, String customerID){
        return customers.stream().filter(p -> p.getCustomerID().equalsIgnoreCase(customerID)).findFirst();
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
    public Page<OrderDetailDTO> getOrdersByCustomerID(String customerID, Integer pageNumber, Integer pageSize) throws JsonProcessingException {
        log.info("getOrdersByCustomerID(customerID={}, pageNumber={}, pageSize={})", 
            customerID, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByCustomerIDOrderByOrderIDDesc(customerID, pageable);
        List<OrderDetailDTO> orderDetailDTOs = this.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailDTOs, pageable, orders.getTotalElements());
    }

    @Override
    public InvoiceDTO getInvoiceByCustomerID(String customerID, String orderID) throws JsonProcessingException {
        log.info("getInvoiceByCustomerID(customerID={}, orderID={})", customerID, orderID);
        List<Invoice> invoices = invoiceRepository.findByOrderIDAndCustomerID(orderID, customerID);
        InvoiceDTO invoice = this.getInvoiceDTO(invoices);
        return invoice;
    }

    @Override
    public Page<OrderDetailDTO> getOrdersByEmployeeID(String employeeID, OrderStatus status, Integer pageNumber,
            Integer pageSize) throws JsonProcessingException {
        log.info("getOrdersByEmployeeID(employeeID={}, status={}, pageNumber={}, pageSize={})", 
            employeeID, status, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByEmployeeIDAndStatusOrderByDesc(employeeID, status, pageable);
        List<OrderDetailDTO> orderDetailDTOs = this.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailDTOs, pageable, orders.getTotalElements());
    }

    @Override
    public OrderWithProducts getOrderByEmployeeID(String employeeID, String orderID) throws JsonProcessingException {
        log.info("getOrderByEmployeeID(employeeID={}, orderID={})", employeeID, orderID);
        Order order = orderRepository.findByEmployeeIDAndOrderID(employeeID, orderID)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " of employee "+ employeeID + " does not found"));
        return this.getOrderWithProducts(order);
    }
}
