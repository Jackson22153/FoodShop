package com.phucx.shop.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.shop.model.ImageFormat;
import com.phucx.shop.service.image.CategoryImageService;
import com.phucx.shop.service.image.ProductImageService;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private CategoryImageService categoryImageService;


    // set image
    @PostMapping(value = "/product/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageFormat> uploadProductImage(
        @RequestBody MultipartFile file,
        @RequestHeader(name = "X-Forwarded-Uri", required = false) String requestUri,
        @RequestHeader(name = "X-Server-Port", required = false) Integer serverPort
    ) throws IOException {

        String filename = productImageService.uploadProductImage(file);
        String imageUrl = productImageService.getCurrentUrl(requestUri, serverPort) + "/" + filename;
        ImageFormat imageFormat = new ImageFormat(imageUrl);
        return ResponseEntity.ok().body(imageFormat);
    }

    @GetMapping("/product/{imageName}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable String imageName) throws IOException {
        byte[] image = productImageService.getProductImage(imageName);
        String mimeType = productImageService.getProductMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }

    // set image
    @PostMapping(value = "/category/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageFormat> uploadCategoryImage(
        @RequestBody MultipartFile file,
        @RequestHeader(name = "X-Forwarded-Uri", required = false) String requestUri,
        @RequestHeader(name = "X-Server-Port", required = false) Integer serverPort
    ) throws IOException {

        String filename = categoryImageService.uploadCategoryImage(file);
        String imageUrl = categoryImageService.getCurrentUrl(requestUri, serverPort) + "/" + filename;
        ImageFormat imageFormat = new ImageFormat(imageUrl);
        return ResponseEntity.ok().body(imageFormat);
    }

    @GetMapping("/category/{imageName}")
    public ResponseEntity<byte[]> getCategoryImage(@PathVariable String imageName) throws IOException {
        byte[] image = categoryImageService.getCategoryImage(imageName);
        String mimeType = categoryImageService.getCategoryMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }
    
}
