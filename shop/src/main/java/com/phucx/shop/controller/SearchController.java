package com.phucx.shop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.phucx.shop.constant.ShopConstant;
import com.phucx.shop.service.jsonFilter.JsonFilterService;
import com.phucx.shop.service.products.ProductsService;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("search")
public class SearchController {
    @Autowired
    private ProductsService productsService;
    @Autowired
    private JsonFilterService jsonFilterService;

    @GetMapping("products")
    public ResponseEntity<MappingJacksonValue> searchProductsByName(
        @RequestParam(name = "l") String letters
    ) {
        if(letters.length()>2){
            var productsPageable = productsService.searchProductByName(
                letters, 0, ShopConstant.PAGESIZE);
            SimpleFilterProvider filterProvider = new SimpleFilterProvider().setFailOnUnknownId(true);
    
            var data = jsonFilterService.filterOutAllExcept(ShopConstant.PRODUCTSFILTER, 
                Set.of("productID", "productName"), 
                productsPageable.getContent(), filterProvider);
            return ResponseEntity.ok().body(data);
        }
        return ResponseEntity.badRequest().body(null);
    }    


}
