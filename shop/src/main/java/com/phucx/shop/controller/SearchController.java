package com.phucx.shop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.shop.config.WebConfig;
import com.phucx.shop.model.CurrentProduct;
import com.phucx.shop.service.product.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("search")
public class SearchController {
    @Autowired
    private ProductService productService;

    @GetMapping("products")
    public ResponseEntity<Page<CurrentProduct>> searchProductsByName(
        @RequestParam(name = "l") String letters,
        @RequestParam(name = "page", required = false) Integer pageNumber
    ) {
        if(letters.length()>2){
            pageNumber = pageNumber!=null?pageNumber:0;
            var productsPageable = productService.searchCurrentProducts(letters, pageNumber, WebConfig.PAGE_SIZE);

            return ResponseEntity.ok().body(productsPageable);
        }
        return ResponseEntity.badRequest().build();
    }    

    @GetMapping("recommended/{categoryName}")
    public ResponseEntity<Page<CurrentProduct>> getRecommendedProductByCategory(
        @PathVariable(name = "categoryName") String categoryName,
        @RequestParam(name = "productID") Integer productID,
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<CurrentProduct> products = productService.getRecommendedProductsByCategory(
            productID, categoryName, pageNumber, WebConfig.RECOMMENDED_PAGE_SIZE);

        return ResponseEntity.ok().body(products);
    }

}
