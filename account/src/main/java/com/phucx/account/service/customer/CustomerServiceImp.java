package com.phucx.account.service.customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.account.constant.OrderStatus;
import com.phucx.account.constant.WebConstant;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.CustomerDetails;
import com.phucx.account.model.InvoiceDetails;
import com.phucx.account.model.Customer;
import com.phucx.account.model.OrderDetails;
import com.phucx.account.model.User;
import com.phucx.account.model.UserInfo;
import com.phucx.account.repository.CustomerAccountRepository;
import com.phucx.account.repository.CustomerDetailRepository;
import com.phucx.account.repository.CustomerRepository;
import com.phucx.account.service.image.ImageService;
import com.phucx.account.service.order.OrderService;
import com.phucx.account.service.user.UserService;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImp implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerAccountRepository customerAccountRepository;
    @Autowired
    private CustomerDetailRepository customerDetailRepository;
    @Autowired
    private ImageService imageService;

	@Override
	public boolean updateCustomerInfo(CustomerDetail customer) {
        log.info("updateCustomerInfo({})", customer.toString());
        Customer fetchedCustomer = customerRepository.findById(customer.getCustomerID())
            .orElseThrow(()->new NotFoundException("Customer " + customer.getCustomerID() + " does not found"));
        String picture = this.imageService.getImageName(customer.getPicture());
        Boolean result = customerDetailRepository.updateCustomerInfo(fetchedCustomer.getCustomerID(), customer.getEmail(),
            customer.getContactName(), customer.getAddress(), customer.getCity(), customer.getPhone(), picture);
        return result;
	}
	@Override
	public CustomerDetail getCustomerDetail(String username) {
        User user = userService.getUser(username);
        Optional<CustomerAccount> customerAccOp = customerAccountRepository.findByUserID(user.getUserID());
        if(customerAccOp.isPresent()){
            CustomerAccount customerAcc = customerAccOp.get();
            String customerID = customerAcc.getCustomerID();
            CustomerDetail customer = customerDetailRepository.findById(customerID)
                .orElseThrow(()-> new NotFoundException("CustomerID: " + customerID + " does not found"));
            imageService.setCustomerDetailImage(customer);
            return customer;
        }else{
            String customerID = UUID.randomUUID().toString();
            customerAccountRepository.createCustomerInfo(customerID, username, username);
            CustomerDetail customer = customerDetailRepository.findById(customerID)
                .orElseThrow(()-> new NotFoundException("CustomerID: " + customerID + " does not found"));
            imageService.setCustomerDetailImage(customer);
            return customer;
        }
    }
    
    @Override
    public boolean addNewCustomer(CustomerAccount customer){
        log.info("addNewCustomer({})", customer);
        if(customer.getUsername() == null || customer.getEmail()==null || customer.getContactName()==null)
            throw new RuntimeException("Missing some customer's information");
        CustomerAccount fetchedCustomer= customerAccountRepository.findByUsername(customer.getUsername());
        if(fetchedCustomer!=null) throw new RuntimeException("Customer " + customer.getUsername() + " already exists");
        var fetchedCustomerByEmail= customerAccountRepository.findByEmail(customer.getEmail());
        if(fetchedCustomerByEmail.isPresent()) throw new RuntimeException("Customer " + customer.getEmail() + " already exists");
        // add new customer
        String userID = UUID.randomUUID().toString();
        String customerID = UUID.randomUUID().toString();
        
        return customerAccountRepository.addNewCustomer(
            userID, customer.getUsername(), 
            passwordEncoder.encode(WebConstant.DEFUALT_PASSWORD), 
            customer.getEmail(), customerID, 
            customer.getContactName());
    }
	
    @Override
	public Page<CustomerAccount> getAllCustomers(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> result = customerAccountRepository.findAll(page);
        imageService.setCustomerAccountImage(result.getContent());
		return result;
	}
	@Override
	public Customer getCustomerByID(String customerID) {
		Customer customer = customerRepository.findById(customerID)
            .orElseThrow(()-> new NotFoundException("Customer " + customerID + " does not found"));
        imageService.setCustomerImage(customer);
        return customer;
	}
    
    
    @Override
    public Customer getCustomerByUsername(String username) {
        CustomerAccount customerAccount = customerAccountRepository.findByUsername(username);
        if(customerAccount!=null){
            String customerID = customerAccount.getCustomerID();
            Customer customer = customerRepository.findById(customerID)
                .orElseThrow(()-> new NotFoundException("Customer: " + customerID + " does not found"));
            imageService.setCustomerImage(customer);
            return customer;
        }else throw new NotFoundException(username + "does not found");
    }
    @Override
    public Page<CustomerAccount> searchCustomersByCustomerID(String customerID, int pageNumber, int pageSize) {
        String searchParam = "%" + customerID +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> customers = customerAccountRepository.findByCustomerIDLike(searchParam, page);
        imageService.setCustomerAccountImage(customers.getContent());
        return customers;
    }
    @Override
    public Page<CustomerAccount> searchCustomersByContactName(String contactName, int pageNumber, int pageSize) {
        String searchParam = "%" + contactName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> customers = customerAccountRepository.findByContactNameLike(searchParam, page);
        imageService.setCustomerAccountImage(customers.getContent());
        return customers;
    }
    @Override
    public Page<CustomerAccount> searchCustomersByUsername(String username, int pageNumber, int pageSize) {
        String searchParam = "%" + username +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> customers = customerAccountRepository.findByUsernameLike(searchParam, page);
        imageService.setCustomerAccountImage(customers.getContent());
        return customers;
    }
    @Override
    public Page<CustomerAccount> searchCustomersByEmail(String email, int pageNumber, int pageSize) {
        String searchParam = "%" + email +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerAccount> customers = customerAccountRepository.findByEmailLike(searchParam, page);
        imageService.setCustomerAccountImage(customers.getContent());
        return customers;
    }
    @Override
    public CustomerDetails getCustomerDetailByCustomerID(String customerID) {
        Customer customer = customerRepository.findById(customerID)
            .orElseThrow(()-> new NotFoundException("Customer " + customerID + " does not found"));
        UserInfo user = userService.getUserInfo(customer.getUserID());
        
        imageService.setCustomerImage(customer);

        CustomerDetails customerDetail = new CustomerDetails(
            customer.getCustomerID(), customer.getContactName(), customer.getPicture(), user);
        return customerDetail;
    }

    @Override
    public Customer getCustomerByUserID(String userID) {
        log.info("getCustomerByUserID(userID={})", userID);
        Customer customer = customerRepository.findByUserID(userID).orElseThrow(
            ()-> new NotFoundException("Customer with userID " + userID + " does not found"));
        imageService.setCustomerImage(customer);
        return customer;
    }
    @Override
    public Page<OrderDetails> getOrders(int pageNumber, int pageSize, String customerID, OrderStatus orderStatus) throws JsonProcessingException {
        log.info("getOrders(pageNumber={}, pageSize={}, customerID={}, orderStatus={})", 
            pageNumber, pageSize, customerID, orderStatus);
        return orderService.getCustomerOrders(customerID, orderStatus, pageNumber, pageSize);
    }
    @Override
    public InvoiceDetails getInvoice(String orderID, String customerID) throws JsonProcessingException {
        log.info("getInvoice(orderID={}, customerID={})", orderID, customerID);
        return orderService.getCustomerInvoice(orderID, customerID);
    }
    @Override
    public List<Customer> getCustomersByIDs(List<String> customerIDs) {
        log.info("getCustomersByIDs(customerIDs={})", customerIDs);
        List<Customer> customers = customerRepository.findAllById(customerIDs);
        imageService.setCustomerImage(customers);
        return customers;
    }
}
