package com.phucx.account.service.customers;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.account.constant.OrderStatus;
import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.exception.InvalidOrderException;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.Customers;
import com.phucx.account.model.InvoiceDTO;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderItem;
import com.phucx.account.model.OrderItemDiscount;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.Order;
import com.phucx.account.repository.CustomerAccountRepository;
import com.phucx.account.repository.CustomerDetailRepository;
import com.phucx.account.repository.CustomersRepository;
import com.phucx.account.service.github.GithubService;
import com.phucx.account.service.order.OrderService;

import jakarta.ws.rs.NotFoundException;


@Service
public class CustomersServiceImp implements CustomersService {
    private Logger logger = LoggerFactory.getLogger(CustomersServiceImp.class);
    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private GithubService githubService;
    @Autowired
    private CustomerAccountRepository customerAccountRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerDetailRepository customerDetailRepository;

	@Override
	public boolean updateCustomerInfo(CustomerDetail customer) {
        // return false;
        logger.info("updateCustomerInfo({})", customer.toString());
        try {
            Customers fetchedCustomer = customersRepository.findById(customer.getCustomerID())
                .orElseThrow(()->new NotFoundException("Customer " + customer.getCustomerID() + " does not found"));
            // Boolean result = false;
            Boolean result = customerDetailRepository.updateCustomerInfo(fetchedCustomer.getCustomerID(), customer.getEmail(),
                customer.getContactName(), customer.getAddress(), customer.getCity(), customer.getPhone(),
                customer.getPicture());
                logger.info("Result: {}", result);
            return result;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return false;
        }


        // try {
        //     var fetchedCustomerOp = customersRepository
        //         .findById(customer.getCustomerID());  
        //     if(fetchedCustomerOp.isPresent()){
        //         String picture = customer.getPicture();
        //         Customers fetchedCustomer = fetchedCustomerOp.get();
        //         if(picture!=null){
        //             if(fetchedCustomer.getPicture()==null){
        //                 picture = githubService.uploadImage(picture);
        //             }else{
        //                 int comparedPicture =fetchedCustomer.getPicture()
        //                     .compareToIgnoreCase(picture);
        //                 if(comparedPicture!=0){
        //                     picture = githubService.uploadImage(picture);
        //                 }else if(comparedPicture==0){
        //                     picture = fetchedCustomer.getPicture();
        //                 }
        //             }
        //         }
        //         Integer check = customersRepository.updateCustomer(customer.getCompanyName(), 
        //             customer.getContactName(), customer.getContactTitle(), 
        //             customer.getAddress(), customer.getCity(), customer.getRegion(), 
        //             customer.getPostalCode(), customer.getCountry(), customer.getPhone(), 
        //             customer.getFax(), picture, customer.getCustomerID());

        //         if (check>0) {
        //             return true;
        //         }
        //     }

        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // return false;
	}
	@Override
	public CustomerDetail getCustomerDetail(String username) {
        CustomerAccount customerAcc = customerAccountRepository.findByUsername(username);
        if(customerAcc!=null){
            String customerID = customerAcc.getCustomerID();
            CustomerDetail customer = customerDetailRepository.findById(customerID)
                .orElseThrow(()-> new NotFoundException("CustomerID: " + customerID + " does not found"));
            return customer;
        }else{
            String customerID = UUID.randomUUID().toString();
            customerAccountRepository.createCustomerInfo(customerID, username, username);
            CustomerDetail customer = customerDetailRepository.findById(customerID)
                .orElseThrow(()-> new NotFoundException("CustomerID: " + customerID + " does not found"));
            return customer;
            // return this.getCustomerDetailByID(customerID);
        }
    }
    
    @Override
    public boolean createCustomer(Customers customer){
        try {
            var customerOP = customersRepository.findById(customer.getCustomerID());
            if(customerOP.isEmpty()){
                Customers c = customersRepository.save(customer);
                if(c!=null) return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
	
    @Override
	public Page<CustomerAccount> getAllCustomers(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> result = customerAccountRepository.findAll(page);
		return result;
	}
	@Override
	public Customers getCustomerDetailByID(String customerID) {
		var customerOp = customersRepository.findById(customerID);
        if(customerOp.isPresent()) return customerOp.get();
        return null;
	}
    @Override
    public OrderWithProducts placeOrder(OrderWithProducts order) 
        throws InvalidDiscountException, NotFoundException, RuntimeException, SQLException, InvalidOrderException 
    {
        if(order.getCustomerID()!=null){
            LocalDateTime currenDateTime = LocalDateTime.now();
            order.setOrderDate(currenDateTime);
            // set applieddate for discount;
            for (OrderItem product : order.getProducts()) {
                for(OrderItemDiscount discount : product.getDiscounts()){
                    discount.setAppliedDate(currenDateTime);
                }
            }


            // validate order
            boolean isValidOrder = orderService.validateOrder(order);
            if(!isValidOrder) throw new InvalidOrderException("Order is not valid");
            // save order
            Order pendingOrder = orderService.saveFullOrder(order);
            order.setOrderID(pendingOrder.getOrderID());
            return order;
        }
        throw new NotFoundException("Customer is not found");
    }
    
    @Override
    public Customers getCustomerByUsername(String username) {
        CustomerAccount customerAccount = customerAccountRepository.findByUsername(username);
        if(customerAccount!=null){
            String customerID = customerAccount.getCustomerID();
            Customers customer = customersRepository.findById(customerID)
                .orElseThrow(()-> new NotFoundException("Customer: " + customerID + " does not found"));
            return customer;
        }else throw new NotFoundException(username + "does not found");
    }
    @Override
    public Page<OrderDetailsDTO> getOrders(int pageNumber, int pageSize, String customerID, OrderStatus orderStatus) {
        Page<OrderDetailsDTO> orders = null;
        if(orderStatus.equals(OrderStatus.All)) {
            orders = orderService.getCustomerOrders(pageNumber, pageSize, customerID);
        }else {
            orders = orderService.getCustomerOrders(pageNumber, pageSize, customerID, orderStatus);
        }
        return orders;
    }
    @Override
    public InvoiceDTO getInvoice(int orderID, String customerID) throws InvalidOrderException {
        return orderService.getCustomerInvoice(orderID, customerID);
    }
    @Override
    public Page<CustomerAccount> searchCustomersByCustomerID(String customerID, int pageNumber, int pageSize) {
        String searchParam = "%" + customerID +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> customers = customerAccountRepository.findByCustomerIDLike(searchParam, page);
        return customers;
    }
    @Override
    public Page<CustomerAccount> searchCustomersByContactName(String contactName, int pageNumber, int pageSize) {
        String searchParam = "%" + contactName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> customers = customerAccountRepository.findByContactNameLike(searchParam, page);
        return customers;
    }
    @Override
    public Page<CustomerAccount> searchCustomersByUsername(String username, int pageNumber, int pageSize) {
        String searchParam = "%" + username +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> customers = customerAccountRepository.findByUsernameLike(searchParam, page);
        return customers;
    }
    @Override
    public Page<CustomerAccount> searchCustomersByEmail(String email, int pageNumber, int pageSize) {
        String searchParam = "%" + email +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> customers = customerAccountRepository.findByEmailLike(searchParam, page);
        return customers;
    }
}
