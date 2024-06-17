package com.phucx.account.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.phucx.account.service.image.CustomerImageService;
import com.phucx.account.service.image.EmployeeImageService;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private EmployeeImageService employeeImageService; 
    @Autowired
    private CustomerImageService customerImageService; 

    @GetMapping("/employee/{imageName}")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable String imageName) throws IOException {
        byte[] image = employeeImageService.getEmployeeImage(imageName);
        String mimeType = employeeImageService.getMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }

    @GetMapping("/customer/{imageName}")
    public ResponseEntity<byte[]> getCustomerImage(@PathVariable String imageName) throws IOException {
        byte[] image = customerImageService.getCustomerImage(imageName);
        String mimeType = customerImageService.getMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }
}
