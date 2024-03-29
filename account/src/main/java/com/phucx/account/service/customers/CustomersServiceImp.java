package com.phucx.account.service.customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.account.model.Customers;
import com.phucx.account.repository.CustomersRepository;
import com.phucx.account.service.github.GithubService;

@Service
public class CustomersServiceImp implements CustomersService {
    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private GithubService githubService;

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
	public Customers getCustomerDetail(String customerID) {
		var customerOp = customersRepository.findById(customerID);
        if(customerOp.isPresent()) return customerOp.get();
        return null;
    }
    
    @Override
    public boolean createCustomer(String customerID){
        try {
            var customerOP = customersRepository.findById(customerID);
            if(customerOP.isEmpty()){
                Customers newCustomer = new Customers();
                newCustomer.setCustomerID(customerID);
                Customers c = customersRepository.save(newCustomer);
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
}
