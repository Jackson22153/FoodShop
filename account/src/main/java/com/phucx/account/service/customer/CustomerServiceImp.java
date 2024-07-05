package com.phucx.account.service.customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.phucx.account.constant.WebConstant;
import com.phucx.account.exception.CustomerNotFoundException;
import com.phucx.account.exception.InvalidUserException;
import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.UserProfile;
import com.phucx.account.repository.CustomerDetailRepository;
import com.phucx.account.service.image.CustomerImageService;
import com.phucx.account.service.image.ImageService;
import com.phucx.account.service.user.UserProfileService;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImp implements CustomerService {
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private CustomerDetailRepository customerDetailRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CustomerImageService customerImageService;

	@Override
	public CustomerDetail updateCustomerInfo(CustomerDetail customer) throws CustomerNotFoundException {
        log.info("updateCustomerInfo({})", customer.toString());
        CustomerDetail fetchedCustomer = customerDetailRepository.findById(customer.getCustomerID())
            .orElseThrow(()->new CustomerNotFoundException("Customer " + customer.getCustomerID() + " does not found"));
        String picture = this.imageService.getImageName(customer.getPicture());
        Boolean result = customerDetailRepository.updateCustomerInfo(
            fetchedCustomer.getCustomerID(), customer.getContactName(),
            customer.getFirstName(), customer.getLastName(), customer.getAddress(), 
            customer.getCity(), customer.getPhone(), picture);
        if(!result) throw new RuntimeException("Error when update information of customer " + customer.getCustomerID());
        
        customer.setPicture(picture);
        customerImageService.setCustomerDetailImage(customer);
        return customer;
	}
	@Override
	public CustomerDetail getCustomerDetail(String userID) throws UserNotFoundException, InvalidUserException {
        UserProfile user = userProfileService.getUserProfile(userID);
        Optional<CustomerDetail> customerDetailOp = customerDetailRepository.findByUserID(user.getUserID());
        if(customerDetailOp.isPresent()){
            CustomerDetail customer = customerDetailOp.get();
            customerImageService.setCustomerDetailImage(customer);
            return customer;
        }else{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication==null){
                throw new RuntimeException("User is not autheticated");
            }
            Jwt jwt = (Jwt) authentication.getPrincipal();
            // extract user's information
            String firstname = jwt.getClaimAsString(WebConstant.FIRST_NAME);
            String lastname = jwt.getClaimAsString(WebConstant.LAST_NAME);
            String contactname = jwt.getClaimAsString(WebConstant.NAME);
            String username = jwt.getClaimAsString(WebConstant.PREFERRED_USERNAME);
            String email = jwt.getClaimAsString(WebConstant.EMAIL);
            // create a new customer
            Boolean result = this.addNewCustomer(
                new CustomerDetail(userID, username, email, firstname, lastname, contactname));
            if(!result) throw new RuntimeException("Customer with userID " + userID + " can not be created");
            // fetch new customer
            CustomerDetail customer = this.getCustomerByUserID(userID);
            customerImageService.setCustomerDetailImage(customer);
            return customer;
        }
    }
    
    private Boolean addNewCustomer(CustomerDetail customer) throws InvalidUserException, UserNotFoundException{
        log.info("addNewCustomer({})", customer);
        if(customer.getUsername()==null) throw new InvalidUserException("Missing username");
        if(customer.getEmail()==null) throw new InvalidUserException("Missing email");
        if(customer.getContactName()==null) throw new InvalidUserException("Missing contact name");
        if(customer.getFirstName()==null) throw new InvalidUserException("Missing first name");
        if(customer.getLastName()==null) throw new InvalidUserException("Missing last name");
        if(customer.getUserID()==null) throw new InvalidUserException("Missing userId");

        // check customer
        Optional<CustomerDetail> fetchedCustomer = customerDetailRepository.findByUserID(customer.getUserID());
        if(fetchedCustomer.isPresent()) 
            throw new EntityExistsException("Customer with userId " + customer.getUserID() + " is existed");
        // add new customer 
        String profileID = UUID.randomUUID().toString();
        String customerID = UUID.randomUUID().toString();

        return customerDetailRepository.addNewCustomer(
            profileID, customer.getUserID(), customer.getUsername(), 
            customer.getEmail(), customerID, customer.getFirstName(), 
            customer.getLastName(), customer.getContactName());
    }
	
	@Override
	public CustomerDetail getCustomerByID(String customerID) throws CustomerNotFoundException {
        log.info("getCustomerByID(customerID={})", customerID);
		CustomerDetail customer = customerDetailRepository.findById(customerID)
            .orElseThrow(()-> new CustomerNotFoundException("Customer " + customerID + " does not found"));
        customerImageService.setCustomerDetailImage(customer);
        return customer;
	}
    
    @Override
    public Page<CustomerDetail> searchCustomersByCustomerID(String customerID, int pageNumber, int pageSize) {
        String searchParam = "%" + customerID +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerDetail> customers = customerDetailRepository.findByCustomerIDLike(searchParam, page);
        customerImageService.setCustomerDetailImage(customers.getContent());
        return customers;
    }
    @Override
    public Page<CustomerDetail> searchCustomersByContactName(String contactName, int pageNumber, int pageSize) {
        String searchParam = "%" + contactName +"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CustomerDetail> customers = customerDetailRepository.findByContactNameLike(searchParam, page);
        customerImageService.setCustomerDetailImage(customers.getContent());
        return customers;
    }

    @Override
    public CustomerDetail getCustomerByUserID(String userID) throws CustomerNotFoundException {
        log.info("getCustomerByUserID(userID={})", userID);
        CustomerDetail customer = customerDetailRepository.findByUserID(userID)
            .orElseThrow(()-> new CustomerNotFoundException("Customer with userID " + userID + " does not found"));
        customerImageService.setCustomerDetailImage(customer);
        return customer;
    }

    @Override
    public List<CustomerDetail> getCustomersByIDs(List<String> customerIDs) {
        log.info("getCustomersByIDs(customerIDs={})", customerIDs);
        List<CustomerDetail> customers = customerDetailRepository.findAllById(customerIDs);
        customerImageService.setCustomerDetailImage(customers);
        return customers;
    }
    @Override
    public Page<CustomerDetail> getCustomers(Integer pageNumber, Integer pageSize) {
        log.info("getCustomers(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CustomerDetail> customers = customerDetailRepository.findAll(pageable);
        customerImageService.setCustomerDetailImage(customers.getContent());
        return customers;
    }
    @Override
    public Page<CustomerDetail> searchCustomersByUsername(String username, int pageNumber, int pageSize) {
        log.info("searchCustomersByUsername(username={}, pageNumber={}, pageSize={})", username, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CustomerDetail> customers = customerDetailRepository.findByUsernameLike(username, pageable);
        customerImageService.setCustomerDetailImage(customers.getContent());
        return customers;
    }
    @Override
    public Page<CustomerDetail> searchCustomersByEmail(String email, int pageNumber, int pageSize) {
        log.info("searchCustomersByEmail(email={}, pageNumber={}, pageSize={})", email, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CustomerDetail> customers = customerDetailRepository.findByEmailLike(email, pageable);
        customerImageService.setCustomerDetailImage(customers.getContent());
        return customers;
    }
}
