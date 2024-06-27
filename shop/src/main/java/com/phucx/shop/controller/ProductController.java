package com.phucx.shop.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.shop.exceptions.EntityExistsException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.ImageFormat;
import com.phucx.shop.model.ProductDetail;
import com.phucx.shop.model.ResponseFormat;
import com.phucx.shop.service.image.ProductImageService;
import com.phucx.shop.service.product.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;

    @PostMapping
    public ResponseEntity<ResponseFormat> updateProductDetail(
        @RequestBody ProductDetail productDetail
    ) throws NotFoundException{        
        boolean status = productService.updateProductDetail(productDetail);
        ResponseFormat data = new ResponseFormat(status);

        return ResponseEntity.ok().body(data);
    }
    @PutMapping
    public ResponseEntity<ResponseFormat> insertProductDetail(
        @RequestBody ProductDetail productDetail
    ) throws EntityExistsException{        
        boolean status = productService.insertProductDetail(productDetail);
        ResponseFormat data = new ResponseFormat(status);

        return ResponseEntity.ok().body(data);
    }
    @GetMapping("/{productID}")
    public ResponseEntity<ProductDetail> getProductDetail(
        @PathVariable Integer productID
    ) throws NotFoundException{        
        ProductDetail product = productService.getProductDetail(productID);
        return ResponseEntity.ok().body(product);
    } 

    // set image
    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
}
