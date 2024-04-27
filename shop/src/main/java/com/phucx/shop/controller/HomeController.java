package com.phucx.shop.controller;

import org.springframework.web.bind.annotation.RestController;

import com.phucx.shop.config.WebConfig;
import com.phucx.shop.model.Categories;
import com.phucx.shop.model.CurrentProductList;
import com.phucx.shop.model.CurrentSalesProduct;
import com.phucx.shop.service.categories.CategoriesService;
import com.phucx.shop.service.products.ProductsService;
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
    private CategoriesService categoriesService;
    @Autowired
    private ProductsService productsService;

    // categories
    @GetMapping("categories")
    public ResponseEntity<Page<Categories>> getCategories(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<Categories> data = categoriesService
            .getCategories(pageNumber, WebConfig.PAGE_SIZE);

        return ResponseEntity.ok().body(data);
    }

    @GetMapping("categories/{categoryName}")
    public ResponseEntity<Categories> getCategory(
        @PathVariable(name = "categoryName") String categoryName
    ){
        Categories data = categoriesService.getCategory(categoryName);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("categories/{categoryName}/products")
    public ResponseEntity<Page<CurrentProductList>> getProductsByCategoryName(
        @PathVariable(name = "categoryName") String categoryName,
        @RequestParam(name = "page", required=false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<CurrentProductList> products = productsService
            .getCurrentProductsByCategoryName(categoryName, pageNumber, WebConfig.PAGE_SIZE);

        return ResponseEntity.ok().body(products);
    }


    // products
    @GetMapping("products")
    public ResponseEntity<Page<CurrentProductList>> getProducts(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        var productsPageable =productsService.getCurrentProductList(pageNumber, WebConfig.PAGE_SIZE);
        return ResponseEntity.ok().body(productsPageable);
    }
    
    @GetMapping("products/id/{productID}")
    public ResponseEntity<CurrentSalesProduct> getProductByID(
        @PathVariable(name = "productID") Integer productID
    ){
        CurrentSalesProduct productDetails = productsService.getCurrentSalesProductsByID(productID);
        return ResponseEntity.ok().body(productDetails);
    }

    @GetMapping("/products/recommended")
    public ResponseEntity<List<CurrentProductList>> getRecommendedProducts(){
        List<CurrentProductList> products = productsService
            .getRecommendedProducts(0, WebConfig.PAGE_SIZE);
            
        return ResponseEntity.ok().body(products);
    }
}
