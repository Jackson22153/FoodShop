package com.phucx.account.service.image.imp;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.account.config.FileProperties;
import com.phucx.account.model.Employee;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.EmployeeDetail;
import com.phucx.account.service.image.EmployeeImageService;
import com.phucx.account.service.image.ImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeImageServiceImp implements EmployeeImageService{

    @Value("${spring.application.name}")
    private String serverName;
    @Autowired
    private FileProperties fileProperties;
    @Autowired
    private ImageService imageService;

    private final String imageUri = "/image/employee";

    @Override
    public Employee setEmployeeImage(Employee employee) {
        // filtering employee 
        if(!(employee.getPhoto()!=null && employee.getPhoto().length()>0)) return employee;
        // employee has image
        String imageUrl = "/" + serverName + imageUri;
        employee.setPhoto(imageUrl + "/" + employee.getPhoto());
        return employee;
    }

    @Override
    public List<Employee> setEmployeeImage(List<Employee> employees) {
        employees.stream().forEach(employee ->{
            if(employee.getPhoto()!=null && employee.getPhoto().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
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
        String imageUrl = "/" + serverName + imageUri;
        employee.setPhoto(imageUrl + "/" + employee.getPhoto());
        return employee;
    }

    @Override
    public List<EmployeeDetail> setEmployeeDetailImage(List<EmployeeDetail> employees) {
        employees.stream().forEach(employee ->{
            if(employee.getPhoto()!=null && employee.getPhoto().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
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
        String imageUrl = "/" + serverName + imageUri;
        employee.setPhoto(imageUrl + "/" + employee.getPhoto());
        return employee;
    }

    @Override
    public List<EmployeeAccount> setEmployeeAccountImage(List<EmployeeAccount> employees) {
        employees.stream().forEach(employee ->{
            if(employee.getPhoto()!=null && employee.getPhoto().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                employee.setPhoto(uri + "/" + employee.getPhoto());
            }
        });
        return employees;
    }

    @Override
    public byte[] getEmployeeImage(String imageName) throws IOException {
        log.info("getEmployeeImage({})", imageName);
        return imageService.getImage(imageName, fileProperties.getEmployeeImageLocation());
    }

    @Override
    public String uploadEmployeeImage(MultipartFile file) throws IOException {
        log.info("uploadEmployeeImage({})", file);
        return imageService.uploadImage(file, fileProperties.getEmployeeImageLocation());
    }

    @Override
    public String getCurrentUrl(String requestUri, Integer serverPort) {
        log.info("getCurrentUrl(requestUri={}, serverPort={})", requestUri, serverPort);
        return imageService.getCurrentUrl(requestUri, serverPort, "/" + serverName + imageUri);
    }

    @Override
    public String getMimeType(String imageName) throws IOException {
        log.info("getMimeType({})", imageName);
        return imageService.getMimeType(imageName, fileProperties.getEmployeeImageLocation());
    }
}
