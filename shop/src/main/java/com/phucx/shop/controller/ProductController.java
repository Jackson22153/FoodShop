package com.phucx.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.shop.model.ProductDetail;
import com.phucx.shop.model.ResponseFormat;
import com.phucx.shop.service.product.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<ResponseFormat> updateProductDetail(
        @RequestBody ProductDetail productDetail
    ){        
        boolean status = productService.updateProductDetail(productDetail);
        ResponseFormat data = new ResponseFormat(status);

        return ResponseEntity.ok().body(data);
    }
    @PutMapping("/product")
    public ResponseEntity<ResponseFormat> insertProductDetail(
        @RequestBody ProductDetail productDetail
    ){        
        boolean status = productService.insertProductDetail(productDetail);
        ResponseFormat data = new ResponseFormat(status);

        return ResponseEntity.ok().body(data);
    }
    @GetMapping("/product/{productID}")
    public ResponseEntity<ProductDetail> getProductDetail(
        @PathVariable Integer productID
    ){        
        ProductDetail product = productService.getProductDetail(productID);
        return ResponseEntity.ok().body(product);
    }
}
