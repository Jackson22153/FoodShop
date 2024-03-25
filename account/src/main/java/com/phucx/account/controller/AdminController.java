package com.phucx.account.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.model.Discounts;
import com.phucx.account.model.ProductDetails;
import com.phucx.account.service.discounts.DiscountsService;
import com.phucx.account.service.products.ProductService;


@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private DiscountsService discountsService;

    @PostMapping("/product")
    public ResponseEntity<Map<String, Boolean>> updateProductDetails(
        @RequestBody ProductDetails productDetails
    ){        
        Map<String, Boolean> result = new HashMap<>();
        boolean status = productService.updateProductDetails(productDetails);
        result.put("status", status);
        return ResponseEntity.ok().body(result);
    }
    @PutMapping("/product")
    public ResponseEntity<Map<String, Boolean>> insertProductDetails(
        @RequestBody ProductDetails productDetails
    ){        
        Map<String, Boolean> result = new HashMap<>();
        boolean status = productService.insertProductDetails(productDetails);
        result.put("status", status);
        return ResponseEntity.ok().body(result);
    }
    // discount
    @PutMapping("/discount")
    public ResponseEntity<Map<String, Boolean>> insertDiscount(
        @RequestBody Discounts discount
    ){
        Boolean status = discountsService.insertDiscount(discount);
        Map<String, Boolean> result = new HashMap<>();
        result.put("status", status);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/discount")
    public ResponseEntity<Map<String, Boolean>> updateDiscount(
        @RequestBody Discounts discount
    ){
        Boolean status = discountsService.updateDiscount(discount);
        Map<String, Boolean> result = new HashMap<>();
        result.put("status", status);
        return ResponseEntity.ok().body(result);
    }
}
