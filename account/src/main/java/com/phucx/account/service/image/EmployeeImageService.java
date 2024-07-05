package com.phucx.account.service.image;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.phucx.account.model.EmployeeDetail;

public interface EmployeeImageService {
    public byte[] getEmployeeImage(String imageName) throws IOException;
    public String uploadEmployeeImage(MultipartFile file) throws IOException;
    public String getCurrentUrl(String requestUri, Integer serverPort);
    public String getMimeType(String imageName) throws IOException;

    // set employee image
    public EmployeeDetail setEmployeeDetailImage(EmployeeDetail employee);
    public List<EmployeeDetail> setEmployeeDetailImage(List<EmployeeDetail> employees);
}
