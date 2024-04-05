package com.phucx.account.service.customers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.account.config.WebConfig;
import com.phucx.account.model.CustomerAccounts;
import com.phucx.account.model.Customers;
import com.phucx.account.model.UserOrderProducts;
import com.phucx.account.repository.CustomerAccountsRepository;
import com.phucx.account.repository.CustomersRepository;
import com.phucx.account.service.github.GithubService;
import com.phucx.account.service.messageQueue.sender.MessageSender;

@Service
public class CustomersServiceImp implements CustomersService {
    // private Logger logger = LoggerFactory.getLogger(CustomersServiceImp.class);
    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private GithubService githubService;
    @Autowired
    private CustomerAccountsRepository customerAccountsRepository;
    @Autowired
    private MessageSender messageSender;

	@Override
	public boolean updateCustomerInfo(Customers customer) {
        try {
            var fetchedCustomerOp = customersRepository
                .findById(customer.getCustomerID());  
            if(fetchedCustomerOp.isPresent()){
                String picture = customer.getPicture();
                Customers fetchedCustomer = fetchedCustomerOp.get();
                if(picture!=null){
                    if(fetchedCustomer.getPicture()==null){
                        picture = githubService.uploadImage(picture);
                    }else{
                        int comparedPicture =fetchedCustomer.getPicture()
                            .compareToIgnoreCase(picture);
                        if(comparedPicture!=0){
                            picture = githubService.uploadImage(picture);
                        }else if(comparedPicture==0){
                            picture = fetchedCustomer.getPicture();
                        }
                    }
                }
                Integer check = customersRepository.updateCustomer(customer.getCompanyName(), 
                    customer.getContactName(), customer.getContactTitle(), 
                    customer.getAddress(), customer.getCity(), customer.getRegion(), 
                    customer.getPostalCode(), customer.getCountry(), customer.getPhone(), 
                    customer.getFax(), picture, customer.getCustomerID());

                if (check>0) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
	}
	@Override
	public Customers getCustomerDetail(String username) {
        CustomerAccounts customerAcc = customerAccountsRepository.findByUsername(username);
        if(customerAcc!=null){
            String customerID = customerAcc.getCustomerID();
            var customerOp = customersRepository.findById(customerID);
            if(customerOp.isPresent()) return customerOp.get();
        }else{
            String customerID = UUID.randomUUID().toString();
            customerAccountsRepository.createCustomerInfo(customerID, username, username);
            return this.getCustomerDetailByID(customerID);
        }
        return null;
    }
    
    @Override
    public boolean createCustomer(Customers customer){
        try {
            var customerOP = customersRepository.findById(customer.getCustomerID());
            // System.out.println(customerOP.get());
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
	public Page<Customers> findAllCustomers(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Customers> result = customersRepository.findAll(page);
		return result;
	}
	@Override
	public Customers getCustomerDetailByID(String customerID) {
		var customerOp = customersRepository.findById(customerID);
        if(customerOp.isPresent()) return customerOp.get();
        return null;
	}
    @Override
    public boolean placeOrder(UserOrderProducts userOrderProducts) {
        try {
            messageSender.send(WebConfig.ORDER_QUEUE, WebConfig.ORDER_ROUTING_KEY, userOrderProducts);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
