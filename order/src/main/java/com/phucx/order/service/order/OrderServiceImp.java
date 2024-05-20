package com.phucx.order.service.order;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.phucx.order.compositeKey.OrderDetailsKey;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.constant.WebConstant;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.exception.InvalidOrderException;
import com.phucx.order.model.Customer;
import com.phucx.order.model.Discount;
import com.phucx.order.model.Employee;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderDetailsDTO;
import com.phucx.order.model.OrderDetailsDiscounts;
import com.phucx.order.model.OrderDetailsExtendedStatus;
import com.phucx.order.model.OrderItem;
import com.phucx.order.model.OrderItemDiscount;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Order;
import com.phucx.order.model.ProductDTO;
import com.phucx.order.model.Product;
import com.phucx.order.model.Shipper;
import com.phucx.order.repository.CustomerRepository;
import com.phucx.order.repository.EmployeeRepository;
import com.phucx.order.repository.OrderDetailsDiscountsRepository;
import com.phucx.order.repository.OrderDetailsExtendedStatusRepository;
import com.phucx.order.repository.OrderDetailsRepository;
import com.phucx.order.repository.OrderRepository;
import com.phucx.order.repository.ShipperRepository;
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
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private OrderDetailsDiscountsRepository orderDetailsDiscountsRepository;
    @Autowired
    private OrderDetailsExtendedStatusRepository orderDetailsExtendedStatusRepository;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ShipperRepository shipperRepository;
    @Autowired
    private BigDecimalService bigDecimalService;


    @Override
    public boolean updateOrderStatus(Integer orderID, OrderStatus status) {
        Order order = orderRepository.findById(orderID)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " does not found"));
        Integer check = orderRepository.updateOrderStatus(order.getOrderID(), status); 
        if(check>0) return true;
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
        log.info("saveOrder({})", order);
        // fetch order's information
        Customer customer = customerRepository.findById(order.getCustomerID())
            .orElseThrow(()-> new NotFoundException("Customer "+order.getCustomerID()+" does not found"));
        Employee employee = null;
        if(order.getEmployeeID()!=null){
            employee = employeeRepository.findById(order.getEmployeeID())
                .orElseThrow(()-> new NotFoundException("Employee "+order.getEmployeeID()+" does not found"));
        }
        Shipper shipper = shipperRepository.findById(order.getShipVia())
            .orElseThrow(()-> new NotFoundException("Shipper "+order.getShipVia()+" does not found"));
        // save order
        Order newOrder = new Order(customer.getCustomerID(), employee.getEmployeeID(), order.getOrderDate(), order.getRequiredDate(), 
            order.getShippedDate(), shipper.getShipperID(), bigDecimalService.formatter(order.getFreight()), 
            order.getShipName(), order.getShipAddress(),  order.getShipCity(), order.getPhone(), 
            OrderStatus.Pending);
        Order checkOrder = orderRepository.saveAndFlush(newOrder);
        if(checkOrder==null) throw new RuntimeException("Error while saving order");
        return checkOrder;
    }
    // save orderDetails
    private OrderDetails saveOrderDetails(OrderItem orderItem, Order order) 
        throws RuntimeException, NotFoundException, SQLException{
        log.info("saveOrderDetails OrderItem:{}", orderItem.toString());
        Product product = productService.getProduct(orderItem.getProductID());
        OrderDetailsKey key = new OrderDetailsKey(product, order);
        OrderDetails orderDetail = new OrderDetails(
            key, bigDecimalService.formatter(product.getUnitPrice()), 
            orderItem.getQuantity());
        OrderDetails newOrderDetail = orderDetailsRepository.saveAndFlush(orderDetail);
        if(newOrderDetail==null) throw new RuntimeException("Error while saving orderdetail for order: "+ order.getOrderID());
        return newOrderDetail;
    }
    // save orderDiscount
    private void saveOrderDetailsDiscounts(OrderItemDiscount orderItemDiscount, OrderDetails orderDetail)
        throws InvalidDiscountException, SQLException
    {
        log.info("saveOrderDetailsDiscounts: orderItemDiscount:{}, orderDetail:{}", 
            orderItemDiscount.toString(), orderDetail.toString());
        Discount discount = discountService.getDiscount(orderItemDiscount.getDiscountID());
        OrderDetailsKey orderDetailsID = orderDetail.getKey();

        // set applied date for discount
        if(orderItemDiscount.getDiscountID()!=null && orderItemDiscount.getAppliedDate()==null){
            orderItemDiscount.setAppliedDate(LocalDateTime.now());
        }

        log.info("InsertOrderDetailDiscount: {discountID={}, orderID={}, productID={}}", 
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
        log.info("validateAndProcessOrder({})", order.toString());
        if(order!=null && order.getProducts().size()>0){
            List<OrderItem> products = order.getProducts();
            // Collections.sort(order.getProducts(), Comparator.comparingInt(OrderItem::getProductID));
            List<Integer> productIds = products.stream().map(OrderItem::getProductID).collect(Collectors.toList());
            List<Product> fetchedProducts = productService.getProducts(productIds);
            // List<Product> fetchedProducts = productRepository.findAllByIdAscending(productIds);
            // validate and update product inStock with order product quantity
            for(OrderItem product : products){
                Product fetchedProduct = findProduct(fetchedProducts, product.getProductID())
                    .orElseThrow(()-> new NotFoundException("Product "+product.getProductID()+" does not found"));
                // Product product = fetchedProducts.get(i);
                // validate discount of product
                Boolean isValidDiscount = discountService.validateDiscountsOfProduct(product);
                if(!isValidDiscount){
                    throw new InvalidDiscountException("Discounts of Product " + product.getProductID() + " is invalid");
                }
                // validate product's stock
                int orderQuantity = product.getQuantity();
                int inStocks = fetchedProduct.getUnitsInStock();
                if(orderQuantity>inStocks){
                    throw new RuntimeException("Product "+product.getProductName()+
                        " does not have enough stocks in inventory");
                }
                // update product
                Boolean status = productService.updateProductInStocks(product.getProductID(), inStocks-orderQuantity);
                if(!status) throw new RuntimeException("Can not update product in stocks");
            }
            return true;
        }
        return false;
    }
    @Override
    public boolean updateOrderEmployee(Integer orderID, String employeeID) {
        // orderRepository.findById(11071);
        Integer check = orderRepository.updateOrderEmployeeID(orderID, employeeID);
        if(check>0) return true;
        return false;
    }
  
    @Override
    public boolean isPendingOrder(Integer orderID) throws InvalidOrderException {
        if(orderID==null) throw new InvalidOrderException("OrderID is null");
        return orderRepository.findById(orderID)
        .map(order -> order.getStatus().equals(OrderStatus.Pending))
        .orElse(false);
    }

    @Override
    public boolean validateOrder(OrderWithProducts order) throws InvalidDiscountException {
        log.info("validateOrder({})", order);
        try {
            // get customer
            Optional<Customer> customer = customerRepository.findById(order.getCustomerID());
            if(customer.isEmpty())
                throw new NotFoundException("Customer "+order.getCustomerID()+" does not found");
            
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
                log.info("Discount: {}", product.getDiscounts());
                if(product.getDiscounts().size()>0){
                    // validate number of discounts that can apply to 1 product
                    Integer numberOfDiscounts = product.getDiscounts().size();
                    if(numberOfDiscounts>WebConstant.MAX_APPLIED_DISCOUNT_PRODUCT){
                        throw new RuntimeException("Exceed the number of discount codes");
                    }
                    boolean isValidDiscount = discountService.validateDiscountsOfProduct(product);
                    if(!isValidDiscount) throw new InvalidDiscountException("Invalid discount");
                }
            }
            return true;
        } catch (InvalidDiscountException e) {
            log.error("Error occurs: " + e.getMessage());
            return false;
        }
        
    }

    private Optional<Product> findProduct(List<Product> products, Integer productID){
        return products.stream().filter(p -> p.getProductID().equals(productID)).findFirst();
    }

    @Override
    public OrderWithProducts getPendingOrderDetail(int orderID) throws InvalidOrderException{
        log.info("getPendingOrderDetail(orderID={})", orderID);
        Order order = orderRepository.findByStatusAndOrderID(OrderStatus.Pending, orderID)
            .orElseThrow(()-> new InvalidOrderException("Order " + orderID + " is invalid"));
        OrderWithProducts convertedOrder = this.getOrderDetail(order);
        return convertedOrder;
    }

    // convert order to orderWithProducts
    private OrderWithProducts getOrderDetail(Order order){
        // fetch infomation
        Customer customer = customerRepository.findById(order.getCustomerID())
            .orElseThrow(()-> new NotFoundException("Customer "+order.getCustomerID()+" does not found"));
        Employee employee = employeeRepository.findById(order.getEmployeeID())
            .orElseThrow(()-> new NotFoundException("Employee "+order.getEmployeeID()+" does not found"));
        Shipper shipper = shipperRepository.findById(order.getShipVia())
            .orElseThrow(()-> new NotFoundException("Shipper "+order.getShipVia()+" does not found"));
        // create and order
        OrderWithProducts orderWithProducts = new OrderWithProducts(
            order.getOrderID(), customer.getCustomerID(), customer.getContactName(), 
            employee!=null?employee.getEmployeeID():null, employee!=null?
            employee.getFirstName() + " " + employee.getLastName():null, order.getOrderDate(), 
            order.getRequiredDate(), order.getShippedDate(), shipper.getShipperID(), 
            shipper.getCompanyName(), shipper.getPhone(), order.getFreight(), 
            order.getShipName(), order.getShipAddress(), order.getShipCity(), order.getPhone(), 
            order.getStatus());

        List<OrderDetails> orderDetails = orderDetailsRepository.findByOrderID(order.getOrderID());
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
    // convert orders
    // private List<OrderDetailsDTO> convertOrders(List<OrderDetailsExtendedStatus> orders){
    //     List<OrderDetailsDTO> orderDTOs = new ArrayList<>();
    //     for(OrderDetailsExtendedStatus order: orders){
    //         // add new OrderDetailsDTO to result list when it meets a new orderID
    //         if(orderDTOs.size()==0 || !orderDTOs.get(orderDTOs.size()-1).getOrderID().equals(order.getOrderID())){
    //             OrderDetailsDTO orderDTO = new OrderDetailsDTO();
    //             orderDTO.setOrderID(order.getOrderID());
    //             orderDTO.setStatus(order.getStatus());
    //             // set customer
    //             orderDTO.setCustomerID(order.getCustomerID());
    //             orderDTO.setContactName(order.getContactName());
    //             orderDTO.setPicture(order.getCustomerPicture());
    //             // add order to a list of order
    //             orderDTOs.add(orderDTO);
    //         }
    //         // add new product to the newest OrderDetails
    //         ProductDTO productDTO = new ProductDTO(order.getProductID(), order.getProductName(), 
    //             order.getUnitPrice(), order.getQuantity(), order.getDiscount(), order.getExtendedPrice(),
    //             order.getPicture());
    //         orderDTOs.get(orderDTOs.size()-1).getProducts().add(productDTO);
    //         orderDTOs.get(orderDTOs.size()-1).setTotalPrice(
    //             orderDTOs.get(orderDTOs.size()-1).getTotalPrice().add(order.getExtendedPrice()));
            
    //     }

    //     return orderDTOs;
    // }

    @Override
    public OrderDetailsDTO getOrder(Integer orderID, OrderStatus status) {
        log.info("getOrder(orderID={}, status={})", orderID, status.name());
        List<OrderDetailsExtendedStatus> orderWithProducts = orderDetailsExtendedStatusRepository
            .findByOrderIDAndStatus(orderID, status);
        if(orderWithProducts==null || orderWithProducts.size()==0) 
            throw new NotFoundException("Order "+ orderID +" does not found");
        return this.convertOrderDetail(orderWithProducts);
    }

    @Override
    public OrderDetailsDTO getOrder(Integer orderID) {
        log.info("getOrder(orderID={})", orderID);
        List<OrderDetailsExtendedStatus> orderWithProducts = orderDetailsExtendedStatusRepository
            .findByOrderID(orderID);
        if(orderWithProducts==null || orderWithProducts.size()==0) 
            throw new NotFoundException("Order "+ orderID +" does not found");
        return convertOrderDetail(orderWithProducts);
    }
    // convert from one specific orderID to an orderDetailsDTO
    private OrderDetailsDTO convertOrderDetail(List<OrderDetailsExtendedStatus> orderProducts){
        // get the first element inside orderproducts
        OrderDetailsExtendedStatus firstElement = orderProducts.get(0);
        OrderDetailsDTO order = new OrderDetailsDTO();
        order.setOrderID(firstElement.getOrderID());
        order.setStatus(firstElement.getStatus());
        // set employee of order
        order.setEmployeeID(firstElement.getEmployeeID());
        // set customer of order
        order.setCustomerID(firstElement.getCustomerID());
        order.setContactName(firstElement.getContactName());
        order.setPicture(firstElement.getCustomerPicture());
        for(OrderDetailsExtendedStatus orderWithProduct: orderProducts){
            // add new product to the newest OrderDetails
            ProductDTO productDTO = new ProductDTO(
                orderWithProduct.getProductID(), orderWithProduct.getProductName(), 
                orderWithProduct.getUnitPrice(), orderWithProduct.getQuantity(), 
                orderWithProduct.getDiscount(), orderWithProduct.getExtendedPrice(),
                orderWithProduct.getPicture());
            // add products to order
            order.getProducts().add(productDTO);
            // set order totalPrice
            order.setTotalPrice(order.getTotalPrice().add(orderWithProduct.getExtendedPrice()));
        }
        return order;
    }
    
}
