package com.phucx.account.service.image;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.phucx.account.model.Customer;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.CustomerDetail;

public interface CustomerImageService {
    
    public byte[] getCustomerImage(String imageName) throws IOException;
    public String uploadCustomerImage(MultipartFile file) throws IOException;
    public String getCurrentUrl(String requestUri, Integer serverPort);
    public String getMimeType(String imageName) throws IOException;

    // set customer image
    public Customer setCustomerImage(Customer customer);
    public List<Customer> setCustomerImage(List<Customer> customers);
    public CustomerDetail setCustomerDetailImage(CustomerDetail customer);
    public List<CustomerDetail> setCustomerDetailImage(List<CustomerDetail> customers);
    public CustomerAccount setCustomerAccountImage(CustomerAccount customer);
    public List<CustomerAccount> setCustomerAccountImage(List<CustomerAccount> customers);
}
