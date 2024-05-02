package com.phucx.account.service.customer;

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
import com.phucx.account.model.CustomerDetailDTO;
import com.phucx.account.model.Customer;
import com.phucx.account.model.InvoiceDTO;
import com.phucx.account.model.OrderDetailsDTO;
import com.phucx.account.model.OrderItem;
import com.phucx.account.model.OrderItemDiscount;
import com.phucx.account.model.OrderWithProducts;
import com.phucx.account.model.UserInfo;
import com.phucx.account.model.Order;
import com.phucx.account.repository.CustomerAccountRepository;
import com.phucx.account.repository.CustomerDetailRepository;
import com.phucx.account.repository.CustomerRepository;
import com.phucx.account.service.github.GithubService;
import com.phucx.account.service.order.OrderService;
import com.phucx.account.service.user.UserService;

import jakarta.ws.rs.NotFoundException;


@Service
public class CustomerServiceImp implements CustomerService {
    private Logger logger = LoggerFactory.getLogger(CustomerServiceImp.class);
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserService userService;
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
            Customer fetchedCustomer = customerRepository.findById(customer.getCustomerID())
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
    public boolean createCustomer(Customer customer){
        try {
            var customerOP = customerRepository.findById(customer.getCustomerID());
            if(customerOP.isEmpty()){
                Customer c = customerRepository.save(customer);
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
	public Customer getCustomerByID(String customerID) {
		Customer customer = customerRepository.findById(customerID)
            .orElseThrow(()-> new NotFoundException("Customer " + customerID + " does not found"));
        return customer;
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
    public Customer getCustomerByUsername(String username) {
        CustomerAccount customerAccount = customerAccountRepository.findByUsername(username);
        if(customerAccount!=null){
            String customerID = customerAccount.getCustomerID();
            Customer customer = customerRepository.findById(customerID)
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
    @Override
    public CustomerDetailDTO getCustomerDetailByCustomerID(String customerID) {
        Customer customer = customerRepository.findById(customerID)
            .orElseThrow(()-> new NotFoundException("Customer " + customerID + " does not found"));
        UserInfo user = userService.getUserInfo(customer.getUser().getUserID());
        
        CustomerDetailDTO customerDetail = new CustomerDetailDTO(
            customer.getCustomerID(), customer.getContactName(), customer.getPicture(), user);
        return customerDetail;
    }
}
