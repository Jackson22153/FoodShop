package com.phucx.order.service.order.imp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.model.Customer;
import com.phucx.order.model.DiscountBreifInfo;
import com.phucx.order.model.DiscountDetail;
import com.phucx.order.model.Employee;
import com.phucx.order.model.Invoice;
import com.phucx.order.model.InvoiceDetails;
import com.phucx.order.model.Order;
import com.phucx.order.model.OrderDetail;
import com.phucx.order.model.OrderDetailDiscount;
import com.phucx.order.model.OrderDetailExtended;
import com.phucx.order.model.OrderDetails;
import com.phucx.order.model.OrderItem;
import com.phucx.order.model.OrderItemDiscount;
import com.phucx.order.model.OrderProduct;
import com.phucx.order.model.OrderWithProducts;
import com.phucx.order.model.Product;
import com.phucx.order.model.ProductWithBriefDiscount;
import com.phucx.order.model.Shipper;
import com.phucx.order.repository.OrderDetailDiscountRepository;
import com.phucx.order.repository.OrderDetailRepository;
import com.phucx.order.service.customer.CustomerService;
import com.phucx.order.service.discount.DiscountService;
import com.phucx.order.service.employee.EmployeeService;
import com.phucx.order.service.order.ConvertOrderService;
import com.phucx.order.service.product.ProductService;
import com.phucx.order.service.shipper.ShipperService;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConvertOrderServiceImp implements ConvertOrderService{
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ShipperService shipperService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderDetailDiscountRepository orderDetailDiscountRepository;

    @Override
    public InvoiceDetails convertInvoiceDetails(List<Invoice> invoices) throws JsonProcessingException {
        log.info("convertInvoiceDetails({})", invoices);
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
        InvoiceDetails invoiceDetails = new InvoiceDetails(
            invoice.getOrderID(), invoice.getCustomerID(), invoice.getEmployeeID(), 
            salesperson, invoice.getShipName(), invoice.getShipAddress(), 
            invoice.getShipCity(), invoice.getPhone(), invoice.getOrderDate(), 
            invoice.getRequiredDate(), invoice.getShippedDate(), fetchedShipper.getCompanyName(), 
            invoice.getFreight(), invoice.getStatus()); 
        // convert invoice of customer
        List<ProductWithBriefDiscount> products = invoiceDetails.getProducts();
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
                invoiceDetails.setTotalPrice(tempInvoice.getExtendedPrice().add(invoiceDetails.getTotalPrice()));
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
        return invoiceDetails;
    }

    @Override
    public List<OrderDetails> convertOrders(List<OrderDetailExtended> orders) throws JsonProcessingException {
        log.info("convertOrders({})", orders);
        // fetch products
        List<Integer> productIds = orders.stream().map(OrderDetailExtended::getProductID).collect(Collectors.toList());
        List<Product> fetchedProducts = this.productService.getProducts(productIds);
        // fetch customer
        Set<String> setCustomerId = orders.stream().map(OrderDetailExtended::getCustomerID).collect(Collectors.toSet());
        List<String> listCustomerId = new ArrayList<>(setCustomerId);
        List<Customer> fetchedCustomers = this.customerService.getCustomersByIDs(listCustomerId);
        // create order
        List<OrderDetails> orderDetails = new ArrayList<>();
        for(OrderDetailExtended order: orders){
            // add new OrderDetails to result list when it meets a new orderID
            if(orderDetails.size()==0 || !orderDetails.get(orderDetails.size()-1).getOrderID().equals(order.getOrderID())){
                // find customer
                Customer customer = this.findCustomer(fetchedCustomers, order.getCustomerID())
                    .orElseThrow(()-> new NotFoundException("Customer " + order.getCustomerID() + " does not found"));
                // create an order
                OrderDetails orderDTO = new OrderDetails();
                orderDTO.setOrderID(order.getOrderID());
                orderDTO.setStatus(order.getStatus());
                // set customer
                orderDTO.setCustomerID(customer.getCustomerID());
                orderDTO.setContactName(customer.getContactName());
                orderDTO.setPicture(customer.getPicture());
                // add order to a list of order
                orderDetails.add(orderDTO);
            }
            Product product = findProduct(fetchedProducts, order.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + order.getProductID() + " does not found"));
            // add new product to the newest OrderDetail
            OrderProduct OrderProduct = new OrderProduct(order.getProductID(), product.getProductName(), 
                order.getUnitPrice(), order.getQuantity(), order.getDiscount(), order.getExtendedPrice(),
                product.getPicture());
            orderDetails.get(orderDetails.size()-1).getProducts().add(OrderProduct);
            orderDetails.get(orderDetails.size()-1).setTotalPrice(
                orderDetails.get(orderDetails.size()-1).getTotalPrice().add(order.getExtendedPrice()));
            
        }
        return orderDetails;
    }

    @Override
    public OrderDetails convertOrderDetail(List<OrderDetailExtended> orderDetailExtendeds)
            throws JsonProcessingException {
        log.info("convertOrderDetail({})", orderDetailExtendeds);
        // get the first element inside orderproducts
        OrderDetailExtended firstElement = orderDetailExtendeds.get(0);

        // get customer
        String customerID = firstElement.getCustomerID();
        Customer fetchedCustomer = customerService.getCustomerByID(customerID);
        if(fetchedCustomer==null) throw new NotFoundException("Customer " + customerID + " does not found");
        // get products
        List<Integer> productIds = orderDetailExtendeds.stream().map(OrderDetailExtended::getProductID).collect(Collectors.toList());
        List<Product> fetchedProducts = productService.getProducts(productIds);
        
        // create an orderdetails instance
        OrderDetails order = new OrderDetails();
        order.setOrderID(firstElement.getOrderID());
        order.setStatus(firstElement.getStatus());
        // set employee of order
        order.setEmployeeID(firstElement.getEmployeeID());
        // set customer of order
        order.setCustomerID(fetchedCustomer.getCustomerID());
        order.setContactName(fetchedCustomer.getContactName());
        order.setPicture(fetchedCustomer.getPicture());
        // set product for order
        for(OrderDetailExtended orderDetailExtended: orderDetailExtendeds){
            Product product = this.findProduct(fetchedProducts, orderDetailExtended.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + orderDetailExtended.getProductID() + " does not found"));
            // add new product to the newest OrderDetail
            OrderProduct orderProduct = new OrderProduct(
                orderDetailExtended.getProductID(), product.getProductName(), 
                orderDetailExtended.getUnitPrice(), orderDetailExtended.getQuantity(), 
                orderDetailExtended.getDiscount(), orderDetailExtended.getExtendedPrice(),
                product.getPicture());
            // add products to order
            order.getProducts().add(orderProduct);
            // set order totalPrice
            order.setTotalPrice(order.getTotalPrice().add(orderDetailExtended.getExtendedPrice()));
        }
        return order;
    }

    @Override
    public OrderWithProducts convertOrderWithProducts(Order order) throws JsonProcessingException {
        log.info("convertOrderWithProducts({})", order);
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
            OrderItem orderItem = new OrderItem(orderDetail.getProductID(), product.getProductName(),
                orderDetail.getQuantity(), product.getPicture(), orderDetail.getUnitPrice());
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
    // find customer
    private Optional<Customer> findCustomer(List<Customer> customers, String customerID){
        return customers.stream().filter(p -> p.getCustomerID().equalsIgnoreCase(customerID)).findFirst();
    }    
}
