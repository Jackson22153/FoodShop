package com.phucx.account.service.order;

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
import com.phucx.account.model.Employees;
import com.phucx.account.model.OrderDetails;
import com.phucx.account.model.OrderItem;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.Orders;
import com.phucx.account.model.Products;
import com.phucx.account.repository.CustomersRepository;
import com.phucx.account.repository.EmployeesRepository;
import com.phucx.account.repository.OrderDetailsRepository;
import com.phucx.account.repository.OrdersRepository;
import com.phucx.account.repository.ProductsRepository;
import com.phucx.account.repository.ShipperRepository;

import jakarta.ws.rs.NotFoundException;


@Service
public class OrderServiceImp implements OrderService{
    private Logger logger = LoggerFactory.getLogger(OrderServiceImp.class);
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private EmployeesRepository employeesRepository;
    @Autowired
    private ShipperRepository shipperRepository;

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
    public Orders saveOrder(OrderWithProducts order) {
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
                order.getOrderDate(), order.getRequiredDate(), order.getShippedDate(), order.getShipVia(), 
                order.getFreight(), order.getShipName(), order.getShipAddress(), order.getShipCity(), order.getShipRegion(), 
                order.getShipPostalCode(), order.getShipCountry(), OrderStatus.Pending);

            Orders checkOrder = ordersRepository.save(newOrder);
            if(checkOrder!=null && checkOrder.getOrderID()>0){
                // save orderdetails
                List<OrderItem> orderItems = order.getProducts();
                for(OrderItem orderItem : orderItems) {
                    var productOp = productsRepository.findById(orderItem.getProductID());
                    if(productOp.isPresent()){
                        Products product = productOp.get();
                        OrderDetailsKey key = new OrderDetailsKey(product, checkOrder);
                        
                        OrderDetails orderDetail = new OrderDetails(key, product.getUnitPrice(), 
                            orderItem.getQuantity());
                        OrderDetails newOrderDetail = orderDetailsRepository.save(orderDetail);
                        if(newOrderDetail==null) throw new RuntimeException("Error while saving orderdetail");
                    }else{
                        throw new NotFoundException("Product not found");
                    }
                }
                return checkOrder;
            }
            else throw new RuntimeException("Error while saving order");
        }
        else throw new NotFoundException("Order missing");
    }
    
    
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean validateOrder(OrderWithProducts order) throws RuntimeException{
        if(order!=null && order.getProducts().size()>0){
            List<OrderItem> products = order.getProducts();
            Collections.sort(order.getProducts(), Comparator.comparingInt(OrderItem::getProductID));
            List<Integer> productIds = products.stream()
                .map(item -> item.getProductID()).collect(Collectors.toList());
            List<Products> fetchedProducts = productsRepository.findAllByIdAscending(productIds);
            // validate and update product inStock with order product quantity
            for(int i = 0 ;i<products.size();i++){
                Products product = fetchedProducts.get(i);
                int orderQuantity = products.get(i).getQuantity();
                int inStocks = product.getUnitsInStock();
                if(orderQuantity>inStocks){
                    throw new RuntimeException("Product "+product.getProductName()+
                        " does not have enough stocks in inventory");
                }
                Integer check = productsRepository.updateProductInStocks(product.getProductID(), inStocks-orderQuantity);
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
            OrderWithProducts orderWithProducts = new OrderWithProducts(order);
            List<OrderItem> orderItems = orderDetailsRepository.findByOrderID(order.getOrderID())
                .stream().map(orderDetail ->{
                    OrderItem orderItem = new OrderItem(orderDetail.getKey().getProductID().getProductID(), 
                        orderDetail.getQuantity(), null);
                    return orderItem;
                }).collect(Collectors.toList());
            orderWithProducts.setProducts(orderItems);
            return orderWithProducts;
        }).collect(Collectors.toList());

        Page<OrderWithProducts> resultPageable = new PageImpl<>(result, pageable, result.size());
        return resultPageable;
    }

    @Override
    public boolean isPendingOrder(Integer orderID) {
        if(orderID==null) throw new RuntimeException("OrderID is null");
        return ordersRepository.findById(orderID)
        .map(order -> order.getStatus().equals(OrderStatus.Pending))
        .orElse(false);
    }
    
}
