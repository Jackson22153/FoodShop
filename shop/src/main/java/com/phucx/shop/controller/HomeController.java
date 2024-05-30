package com.phucx.shop.controller;

import org.springframework.web.bind.annotation.RestController;

import com.phucx.shop.config.WebConfig;
import com.phucx.shop.model.Category;
import com.phucx.shop.model.CurrentProduct;
import com.phucx.shop.model.ProductDetail;
import com.phucx.shop.service.category.CategoryService;
import com.phucx.shop.service.product.ProductService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("home")
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    // Category
    @GetMapping("categories")
    public ResponseEntity<Page<Category>> getCategories(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<Category> data = categoryService
            .getCategories(pageNumber, WebConfig.PAGE_SIZE);

        return ResponseEntity.ok().body(data);
    }

    @GetMapping("categories/name/{categoryName}")
    public ResponseEntity<Category> getCategory(
        @PathVariable(name = "categoryName") String categoryName
    ){
        Category data = categoryService.getCategory(categoryName);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("categories/id/{categoryID}")
    public ResponseEntity<Category> getCategoryByID(
        @PathVariable(name = "categoryID") Integer categoryID
    ){
        Category data = categoryService.getCategory(categoryID);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("categories/{categoryName}/products")
    public ResponseEntity<Page<CurrentProduct>> getProductsByCategoryName(
        @PathVariable(name = "categoryName") String categoryName,
        @RequestParam(name = "page", required=false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<CurrentProduct> products = productService
            .getCurrentProductsByCategoryName(categoryName, pageNumber, WebConfig.PAGE_SIZE);

        return ResponseEntity.ok().body(products);
    }


    // products
    @GetMapping("products")
    public ResponseEntity<Page<CurrentProduct>> getProducts(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        var productsPageable =productService.getCurrentProduct(pageNumber, WebConfig.PAGE_SIZE);
        return ResponseEntity.ok().body(productsPageable);
    }
    
    @GetMapping("products/id/{productID}")
    public ResponseEntity<ProductDetail> getProductByID(@PathVariable(name = "productID") Integer productID){
        ProductDetail productDetails = productService.getProductDetail(productID);
        return ResponseEntity.ok().body(productDetails);
    }

    @GetMapping("/products/recommended")
    public ResponseEntity<List<CurrentProduct>> getRecommendedProducts(){
        List<CurrentProduct> products = productService
            .getRecommendedProducts(0, WebConfig.RECOMMENDED_PRODUCT_PAGE_SIZE);
            
        return ResponseEntity.ok().body(products);
    }
}
