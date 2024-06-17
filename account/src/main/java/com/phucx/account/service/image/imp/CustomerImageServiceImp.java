package com.phucx.account.service.image.imp;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.account.config.FileProperties;
import com.phucx.account.model.Customer;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.service.image.CustomerImageService;
import com.phucx.account.service.image.ImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerImageServiceImp implements CustomerImageService{
    @Value("${spring.application.name}")
    private String serverName;
    @Autowired
    private ImageService imageService;
    @Autowired
    private FileProperties fileProperties;

    private final String imageUri = "/image/customer";

    @Override
    public Customer setCustomerImage(Customer customer) {
        // filtering customer 
        if(!(customer.getPicture()!=null && customer.getPicture().length()>0)) return customer;
        // customer has image
        String imageUrl = "/" + serverName + imageUri;
        customer.setPicture(imageUrl + "/" + customer.getPicture());
        return customer;
    }

    @Override
    public List<Customer> setCustomerImage(List<Customer> customers) {
        customers.stream().forEach(customer ->{
            if(customer.getPicture()!=null && customer.getPicture().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                customer.setPicture(uri + "/" + customer.getPicture());
            }
        });
        return customers;
    }

    @Override
    public CustomerDetail setCustomerDetailImage(CustomerDetail customer) {
        // filtering customer 
        if(!(customer.getPicture()!=null && customer.getPicture().length()>0)) return customer;
        // customer has image
        String imageUrl = "/" + serverName + imageUri;
        customer.setPicture(imageUrl + "/" + customer.getPicture());
        return customer;
    }

    @Override
    public List<CustomerDetail> setCustomerDetailImage(List<CustomerDetail> customers) {
        customers.stream().forEach(customer ->{
            if(customer.getPicture()!=null && customer.getPicture().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                customer.setPicture(uri + "/" + customer.getPicture());
            }
        });
        return customers;
    }

    @Override
    public CustomerAccount setCustomerAccountImage(CustomerAccount customer) {
        // filtering customer 
        if(!(customer.getPicture()!=null && customer.getPicture().length()>0)) return customer;
        // customer has image
        String imageUrl = "/" + serverName + imageUri;
        customer.setPicture(imageUrl + "/" + customer.getPicture());
        return customer;
    }

    @Override
    public List<CustomerAccount> setCustomerAccountImage(List<CustomerAccount> customers) {
        customers.stream().forEach(customer ->{
            if(customer.getPicture()!=null && customer.getPicture().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                customer.setPicture(uri + "/" + customer.getPicture());
            }
        });
        return customers;
    }

    @Override
    public byte[] getCustomerImage(String imageName) throws IOException {
        log.info("getCustomerImage({})", imageName);
        return this.imageService.getImage(imageName, fileProperties.getCustomerImageLocation());
    }

    @Override
    public String uploadCustomerImage(MultipartFile file) throws IOException {
        log.info("uploadCustomerImage({})", file);
        return this.imageService.uploadImage(file, fileProperties.getCustomerImageLocation());
    }

    @Override
    public String getCurrentUrl(String requestUri, Integer serverPort) {
        log.info("getCurrentUrl(requestUri={}, serverPort={})", requestUri, serverPort);
        return this.imageService.getCurrentUrl(requestUri, serverPort, "/" + serverName + imageUri);
    }

    @Override
    public String getMimeType(String imageName) throws IOException {
        log.info("getMimeType({})", imageName);
        return this.imageService.getMimeType(imageName, fileProperties.getCustomerImageLocation());
    }
    
}
