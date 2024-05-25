package com.phucx.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.shop.model.Category;
import com.phucx.shop.model.ResponseFormat;
import com.phucx.shop.service.category.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    // category
    @PostMapping
    public ResponseEntity<ResponseFormat> updateCategory(
        @RequestBody Category category
    ){
        boolean check = categoryService.updateCategory(category);
        ResponseFormat data = new ResponseFormat(check);
        return ResponseEntity.ok().body(data);
    }
    @PutMapping
    public ResponseEntity<ResponseFormat> createCategory(
        @RequestBody Category category
    ){
        boolean check = categoryService.createCategory(category);
        ResponseFormat data = new ResponseFormat(check);
        return ResponseEntity.ok().body(data);
    }
}
