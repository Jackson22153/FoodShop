package com.phucx.account.service.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.phucx.account.config.FileProperties;
import com.phucx.account.model.Customer;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.Employee;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.EmployeeDetail;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageServiceImp implements ImageService{
    @Autowired
    private FileProperties fileProperties;

    @Value("${spring.application.name}")
    private String serverName;

    private final String imagePath = "/image";
    private final String imageUri = "/user" + imagePath;

    @Override
    public byte[] getImage(String imagename) throws IOException {
        log.info("getImage({})", imagename);
        String filePath = fileProperties.getImageStoredLocation();
        if(filePath.charAt(filePath.length()-1)!='/') filePath+='/';
        Path path = Paths.get(filePath+imagename);
        return Files.readAllBytes(path);
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        log.info("uploadImage({})", file.getOriginalFilename());
        if(file.isEmpty()) throw new NotFoundException("Image does not found");
        // set filename
        String filename = file.getOriginalFilename();
        int dotIndex = filename.lastIndexOf(".");
        String extension = filename.substring(dotIndex+1);
        String randomName = UUID.randomUUID().toString();
        String newFile = randomName + "." + extension;
        // set stored location
        Path targetPath = Path.of(fileProperties.getImageStoredLocation(), newFile);
        // copy image to stored location
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return newFile;
    }

    @Override
    public String getCurrentUrl(String requestUri, Integer serverPort) {
        String host = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        String imageUri = this.getUri(requestUri);
        String url = host + ":" + serverPort + imageUri;
        return url;
    }

    private String getUri(String requestUri){
        int slashIndex = requestUri.lastIndexOf("/");
        String imageUri = requestUri.substring(0, slashIndex) + this.imagePath;
        return imageUri;
    }

    @Override
    public String getMimeType(String imagename) throws IOException {
        log.info("getMimeType({})", imagename);
        String storedLocation = fileProperties.getImageStoredLocation();
        if(storedLocation.charAt(storedLocation.length()-1)!='/') storedLocation+='/';
        Path path = Paths.get(storedLocation + imagename);
        String mimeType = Files.probeContentType(path);
        return mimeType;
    }


    @Override
    public String getImageName(String url) {
        if(url==null) return null;
        int indexOfSlash = url.lastIndexOf("/");
        String filename = url.substring(indexOfSlash + 1);
        log.info("filename: {}", filename);
        return filename;
    }

    @Override
    public Employee setEmployeeImage(Employee employee) {
        // filtering employee 
        if(!(employee.getPhoto()!=null && employee.getPhoto().length()>0)) return employee;
        // employee has image
        String imageUrl = "/" + serverName + this.getUri(imageUri);
        employee.setPhoto(imageUrl + "/" + employee.getPhoto());
        return employee;
    }

    @Override
    public List<Employee> setEmployeeImage(List<Employee> employees) {
        employees.stream().forEach(employee ->{
            if(employee.getPhoto()!=null && employee.getPhoto().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + this.getUri(imageUri);
                employee.setPhoto(uri + "/" + employee.getPhoto());
            }
        });
        return employees;
    }

    @Override
    public EmployeeDetail setEmployeeDetailImage(EmployeeDetail employee) {
        // filtering employee 
        if(!(employee.getPhoto()!=null && employee.getPhoto().length()>0)) return employee;
        // employee has image
        String imageUrl = "/" + serverName + this.getUri(imageUri);
        employee.setPhoto(imageUrl + "/" + employee.getPhoto());
        return employee;
    }

    @Override
    public List<EmployeeDetail> setEmployeeDetailImage(List<EmployeeDetail> employees) {
        employees.stream().forEach(employee ->{
            if(employee.getPhoto()!=null && employee.getPhoto().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + this.getUri(imageUri);
                employee.setPhoto(uri + "/" + employee.getPhoto());
            }
        });
        return employees;
    }

    @Override
    public EmployeeAccount setEmployeeAccountImage(EmployeeAccount employee) {
        // filtering employee 
        if(!(employee.getPhoto()!=null && employee.getPhoto().length()>0)) return employee;
        // employee has image
        String imageUrl = "/" + serverName + this.getUri(imageUri);
        employee.setPhoto(imageUrl + "/" + employee.getPhoto());
        return employee;
    }

    @Override
    public List<EmployeeAccount> setEmployeeAccountImage(List<EmployeeAccount> employees) {
        employees.stream().forEach(employee ->{
            if(employee.getPhoto()!=null && employee.getPhoto().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + this.getUri(imageUri);
                employee.setPhoto(uri + "/" + employee.getPhoto());
            }
        });
        return employees;
    }

    @Override
    public Customer setCustomerImage(Customer customer) {
        // filtering customer 
        if(!(customer.getPicture()!=null && customer.getPicture().length()>0)) return customer;
        // customer has image
        String imageUrl = "/" + serverName + this.getUri(imageUri);
        customer.setPicture(imageUrl + "/" + customer.getPicture());
        return customer;
    }

    @Override
    public List<Customer> setCustomerImage(List<Customer> customers) {
        customers.stream().forEach(customer ->{
            if(customer.getPicture()!=null && customer.getPicture().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + this.getUri(imageUri);
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
        String imageUrl = "/" + serverName + this.getUri(imageUri);
        customer.setPicture(imageUrl + "/" + customer.getPicture());
        return customer;
    }

    @Override
    public List<CustomerDetail> setCustomerDetailImage(List<CustomerDetail> customers) {
        customers.stream().forEach(customer ->{
            if(customer.getPicture()!=null && customer.getPicture().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + this.getUri(imageUri);
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
        String imageUrl = "/" + serverName + this.getUri(imageUri);
        customer.setPicture(imageUrl + "/" + customer.getPicture());
        return customer;
    }

    @Override
    public List<CustomerAccount> setCustomerAccountImage(List<CustomerAccount> customers) {
        customers.stream().forEach(customer ->{
            if(customer.getPicture()!=null && customer.getPicture().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + this.getUri(imageUri);
                customer.setPicture(uri + "/" + customer.getPicture());
            }
        });
        return customers;
    }

}
