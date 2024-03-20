package com.phucx.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.model.ProductDetails;
import com.phucx.account.service.products.ProductService;


@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private ProductService productService;
    

    @PostMapping("/updateProduct")
    public ResponseEntity<Boolean> updateProductDetails(@RequestBody ProductDetails productDetails){
        var result = productService.updateProductDetails(productDetails);
        return ResponseEntity.ok().body(result);
    }
}
