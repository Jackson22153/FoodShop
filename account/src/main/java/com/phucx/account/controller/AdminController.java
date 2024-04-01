package com.phucx.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.model.Categories;
import com.phucx.account.model.Discounts;
import com.phucx.account.model.ProductDetails;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.service.categories.CategoriesService;
import com.phucx.account.service.discounts.DiscountsService;
import com.phucx.account.service.products.ProductService;


@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private DiscountsService discountsService;
    @Autowired
    private CategoriesService categoriesService;

    @PostMapping("/product")
    public ResponseEntity<ResponseFormat> updateProductDetails(
        @RequestBody ProductDetails productDetails
    ){        
        boolean status = productService.updateProductDetails(productDetails);
        ResponseFormat data = new ResponseFormat(status);

        return ResponseEntity.ok().body(data);
    }
    @PutMapping("/product")
    public ResponseEntity<ResponseFormat> insertProductDetails(
        @RequestBody ProductDetails productDetails
    ){        
        boolean status = productService.insertProductDetails(productDetails);
        ResponseFormat data = new ResponseFormat(status);

        return ResponseEntity.ok().body(data);
    }
    // discount
    @PutMapping("/discount")
    public ResponseEntity<ResponseFormat> insertDiscount(
        @RequestBody Discounts discount
    ){
        Boolean status = discountsService.insertDiscount(discount);
        ResponseFormat data = new ResponseFormat(status);

        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/discount")
    public ResponseEntity<ResponseFormat> updateDiscount(
        @RequestBody Discounts discount
    ){
        Boolean status = discountsService.updateDiscount(discount);
        ResponseFormat data = new ResponseFormat(status);
        return ResponseEntity.ok().body(data);
    }

// categories
    @PostMapping("/category")
    public ResponseEntity<ResponseFormat> updateCategory(
        @RequestBody Categories category
    ){
        boolean check = categoriesService.updateCategory(category);
        ResponseFormat data = new ResponseFormat(check);
        return ResponseEntity.ok().body(data);
    }
    @PutMapping("/category")
    public ResponseEntity<ResponseFormat> createCategory(
        @RequestBody Categories category
    ){
        boolean check = categoriesService.createCategory(category);
        ResponseFormat data = new ResponseFormat(check);
        return ResponseEntity.ok().body(data);
    }
}