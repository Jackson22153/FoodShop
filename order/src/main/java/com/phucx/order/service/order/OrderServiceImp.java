package com.phucx.order.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.order.compositeKey.OrderDetailKey;
import com.phucx.order.constant.OrderStatus;
import com.phucx.order.constant.WebConstant;
import com.phucx.order.exception.InvalidDiscountException;
import com.phucx.order.model.Customer;
import com.phucx.order.model.DiscountDetail;
import com.phucx.order.model.Employee;
import com.phucx.order.model.OrderItem;
import com.phucx.order.model.OrderItemDiscount;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Order;
import com.phucx.order.model.OrderDetail;
import com.phucx.order.model.OrderDetailDTO;
import com.phucx.order.model.OrderDetailDiscount;
import com.phucx.order.model.OrderDetailExtended;
import com.phucx.order.model.ProductDTO;
import com.phucx.order.model.Shipper;
import com.phucx.order.model.Product;
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

    @Override
    public boolean updateOrderStatus(String orderID, OrderStatus status) {
        log.info("updateOrderStatus(orderID={}, status={})", orderID, status);
        Order order = orderRepository.findById(orderID)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " does not found"));
        Integer check = orderRepository.updateOrderStatus(order.getOrderID(), status); 
        if(check>0) return true;
        return false;
    }

    @Override
    @Transactional
    public String saveFullOrder(OrderWithProducts order) {
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
    private String saveOrder(OrderWithProducts order){
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
    public boolean validateAndProcessOrder(OrderWithProducts order) throws InvalidDiscountException{
        log.info("validateAndProcessOrder({})", order.toString());
        if(order!=null && order.getProducts().size()>0){
            // store product's new instock
            List<Product> updateProductsInStock = new ArrayList<>();
            // get product in order
            List<OrderItem> products = order.getProducts();
            List<Integer> productIds = products.stream().map(OrderItem::getProductID).collect(Collectors.toList());
            List<Product> fetchedProducts = productService.getProducts(productIds);
            // validate discounts of products
            Boolean isValidDiscount = discountService.validateDiscount(order);
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
    public boolean updateOrderEmployee(String orderID, String employeeID) {
        // orderRepository.findById(11071);
        Integer check = orderRepository.updateOrderEmployeeID(orderID, employeeID);
        if(check>0) return true;
        return false;
    }
  
    // @Override
    // public boolean isPendingOrder(String orderID) throws InvalidOrderException {
    //     if(orderID==null) throw new InvalidOrderException("OrderID is null");
    //     return orderRepository.findById(orderID)
    //     .map(order -> order.getStatus().equals(OrderStatus.Pending))
    //     .orElse(false);
    // }

    @Override
    public boolean validateOrder(OrderWithProducts order) throws InvalidDiscountException {
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
                    // boolean isValidDiscount = discountService.validateDiscountsOfProduct(product);
                    // if(!isValidDiscount) throw new InvalidDiscountException("Invalid discount");
                }
            }
            Boolean isValidDiscount = discountService.validateDiscount(order);
            if(!isValidDiscount) throw new InvalidDiscountException("Invalid discount");

            return true;
        } catch (InvalidDiscountException e) {
            log.error("Error occurs: " + e.getMessage());
            return false;
        }
        
    }



    @Override
    public OrderWithProducts getPendingOrderDetail(String orderID){
        log.info("getPendingOrderDetail(orderID={})", orderID);
        Order order = orderRepository.findByOrderIDAndStatus( orderID, OrderStatus.Pending)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " with pending status does not found"));
        OrderWithProducts convertedOrder = this.getOrderWithProducts(order);
        return convertedOrder;
    }

    // convert orders
    // private List<OrderDetailDTO> convertOrders(List<OrderDetailExtended> orders){
    //     List<OrderDetailDTO> orderDTOs = new ArrayList<>();
    //     for(OrderDetailExtended order: orders){
    //         // add new OrderDetailDTO to result list when it meets a new orderID
    //         if(orderDTOs.size()==0 || !orderDTOs.get(orderDTOs.size()-1).getOrderID().equals(order.getOrderID())){
    //             OrderDetailDTO orderDTO = new OrderDetailDTO();
    //             orderDTO.setOrderID(order.getOrderID());
    //             orderDTO.setStatus(order.getStatus());
    //             // set customer
    //             orderDTO.setCustomerID(order.getCustomerID());
    //             orderDTO.setContactName(order.getContactName());
    //             orderDTO.setPicture(order.getCustomerPicture());
    //             // add order to a list of order
    //             orderDTOs.add(orderDTO);
    //         }
    //         // add new product to the newest OrderDetail
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
    public OrderDetailDTO getOrder(String orderID, OrderStatus status) {
        log.info("getOrder(orderID={}, status={})", orderID, status.name());
        List<OrderDetailExtended> orderDetailExtendeds = orderDetailExtendedRepository
            .findByOrderIDAndStatus(orderID, status);
        if(orderDetailExtendeds==null || orderDetailExtendeds.size()==0) 
            throw new NotFoundException("Order "+ orderID +" does not found");
        return this.convertOrderDetail(orderDetailExtendeds);
    }

    @Override
    public OrderDetailDTO getOrder(String orderID) {
        log.info("getOrder(orderID={})", orderID);
        List<OrderDetailExtended> orderDetailExtended = orderDetailExtendedRepository
            .findByOrderID(orderID);
        if(orderDetailExtended==null || orderDetailExtended.size()==0) 
            throw new NotFoundException("Order "+ orderID +" does not found");
        return this.convertOrderDetail(orderDetailExtended);
    }
    // convert from one specific orderID to an OrderDetailDTO
    private OrderDetailDTO convertOrderDetail(List<OrderDetailExtended> orderProducts){
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
    private OrderWithProducts getOrderWithProducts(Order order){
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

    // find product
    private Optional<Product> findProduct(List<Product> products, Integer productID){
        return products.stream().filter(p -> p.getProductID().equals(productID)).findFirst();
    }
    // find discount
    private Optional<DiscountDetail> findDiscount(List<DiscountDetail> discounts, String discountID){
        return discounts.stream().filter(p -> p.getDiscountID().equalsIgnoreCase(discountID)).findFirst();
    }

    @Override
    public Boolean isPendingOrder(String orderID) {
        log.info("isPendingOrder(OrderID={})", orderID);
        Order fetchedOrder = orderRepository.findById(orderID)
            .orElseThrow(()-> new NotFoundException("Order ID " + orderID + " does not found"));
        if(fetchedOrder.getStatus().equals(OrderStatus.Pending)) return true;
        return false;
    }
}
