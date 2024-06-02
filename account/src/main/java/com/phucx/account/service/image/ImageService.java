package com.phucx.account.service.image;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.phucx.account.model.Customer;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.CustomerDetail;
import com.phucx.account.model.Employee;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.EmployeeDetail;


public interface ImageService {
    public byte[] getImage(String imagename) throws IOException;
    public String uploadImage(MultipartFile file) throws IOException;
    public String getCurrentUrl(String requestUri, Integer serverPort);
    public String getMimeType(String imagename) throws IOException;
    public String getImageName(String url);
    // set employee image
    public Employee setEmployeeImage(Employee employee);
    public List<Employee> setEmployeeImage(List<Employee> employees);
    public EmployeeDetail setEmployeeDetailImage(EmployeeDetail employee);
    public List<EmployeeDetail> setEmployeeDetailImage(List<EmployeeDetail> employees);
    public EmployeeAccount setEmployeeAccountImage(EmployeeAccount employee);
    public List<EmployeeAccount> setEmployeeAccountImage(List<EmployeeAccount> employees);
    // set customer image
    public Customer setCustomerImage(Customer customer);
    public List<Customer> setCustomerImage(List<Customer> customers);
    public CustomerDetail setCustomerDetailImage(CustomerDetail customer);
    public List<CustomerDetail> setCustomerDetailImage(List<CustomerDetail> customers);
    public CustomerAccount setCustomerAccountImage(CustomerAccount customer);
    public List<CustomerAccount> setCustomerAccountImage(List<CustomerAccount> customers);
}
