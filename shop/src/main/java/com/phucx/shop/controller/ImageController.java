package com.phucx.shop.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.phucx.shop.service.image.CategoryImageService;
import com.phucx.shop.service.image.ProductImageService;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private CategoryImageService categoryImageService;


    @GetMapping("/product/{imageName}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable String imageName) throws IOException {
        byte[] image = productImageService.getProductImage(imageName);
        String mimeType = productImageService.getProductMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }


    @GetMapping("/category/{imageName}")
    public ResponseEntity<byte[]> getCategoryImage(@PathVariable String imageName) throws IOException {
        byte[] image = categoryImageService.getCategoryImage(imageName);
        String mimeType = categoryImageService.getCategoryMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }
    
}
