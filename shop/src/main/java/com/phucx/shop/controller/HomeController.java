package com.phucx.shop.controller;

import org.springframework.web.bind.annotation.RestController;
import com.phucx.shop.config.WebConfig;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.Category;
import com.phucx.shop.model.CurrentProduct;
import com.phucx.shop.model.ProductDetail;
import com.phucx.shop.service.category.CategoryService;
import com.phucx.shop.service.product.ProductService;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "home", produces = MediaType.APPLICATION_JSON_VALUE)
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    // Category
    @Operation(summary = "Get all categories", tags = {"tutorials", "get", "public"})
    @GetMapping("categories")
    public ResponseEntity<Page<Category>> getCategories(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<Category> data = categoryService
            .getCategories(pageNumber, WebConfig.PAGE_SIZE);

        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Get category by name", tags = {"tutorials", "get", "public"})
    @GetMapping("categories/name/{categoryName}")
    public ResponseEntity<Category> getCategory(
        @PathVariable(name = "categoryName") String categoryName
    ) throws NotFoundException{
        Category data = categoryService.getCategory(categoryName);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Get category by id", tags = {"tutorials", "get", "public"})
    @GetMapping("categories/id/{categoryID}")
    public ResponseEntity<Category> getCategoryByID(
        @PathVariable(name = "categoryID") Integer categoryID
    ) throws NotFoundException{
        Category data = categoryService.getCategory(categoryID);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Get products by category", tags = {"tutorials", "get", "public"})
    @GetMapping("categories/{categoryName}/products")
    public ResponseEntity<Page<CurrentProduct>> getProductsByCategoryName(
        @PathVariable(name = "categoryName") String categoryName,
        @RequestParam(name = "page", required=false) Integer pageNumber
    ) throws NotFoundException{
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<CurrentProduct> products = productService.getCurrentProductsByCategoryName(
            categoryName, pageNumber, WebConfig.PAGE_SIZE);
        return ResponseEntity.ok().body(products);
    }


    // products
    @Operation(summary = "Get products", tags = {"tutorials", "get", "public"})
    @GetMapping("products")
    public ResponseEntity<Page<CurrentProduct>> getProducts(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        var productsPageable =productService.getCurrentProduct(pageNumber, WebConfig.PAGE_SIZE);
        return ResponseEntity.ok().body(productsPageable);
    }
    
    @Operation(summary = "Get product by id", tags = {"tutorials", "get", "public"})
    @GetMapping("products/id/{productID}")
    public ResponseEntity<ProductDetail> getProductByID(@PathVariable(name = "productID") Integer productID) throws NotFoundException{
        ProductDetail productDetails = productService.getProductDetail(productID);
        return ResponseEntity.ok().body(productDetails);
    }

    @Operation(summary = "Get recommended products", tags = {"tutorials", "get", "public"})
    @GetMapping("/products/recommended")
    public ResponseEntity<List<CurrentProduct>> getRecommendedProducts(){
        List<CurrentProduct> products = productService
            .getRecommendedProducts(0, WebConfig.RECOMMENDED_PRODUCT_PAGE_SIZE);
            
        return ResponseEntity.ok().body(products);
    }
}
