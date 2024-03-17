package com.phucx.shop.controller;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.phucx.shop.constant.ShopConstant;
import com.phucx.shop.model.CurrentProductList;
import com.phucx.shop.model.ProductDetails;
import com.phucx.shop.model.Products;
import com.phucx.shop.model.Shippers;
import com.phucx.shop.service.categories.CategoriesService;
import com.phucx.shop.service.jsonFilter.JsonFilterService;
import com.phucx.shop.service.products.ProductsService;
import com.phucx.shop.service.shippers.ShippersService;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
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
    @Autowired
    private ShippersService shippersService;
    @Autowired
    private JsonFilterService jsonFilterService;

    // categories
    @GetMapping("categories")
    public ResponseEntity<MappingJacksonValue> getCategories(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        var categoryPageable =categoriesService.getCategories(pageNumber, ShopConstant.PAGESIZE);

        SimpleFilterProvider filterProvider = new SimpleFilterProvider().setFailOnUnknownId(true);
        var data = jsonFilterService.serializeAllExcept(ShopConstant.CATEGORIESFILTER, 
            Set.of("picture"),categoryPageable,filterProvider);
        // var data = jsonFilterService.filterOutAllExcept(ClientConstant.PRODUCTSFILTER, 
        //     Set.of("productID", "productName", "unitPrice", "unitsInStock", "categoryID"), 
        //     categoryPageable, filterProvider);

        // data = jsonFilterService.filterOutAllExcept(ClientConstant.CATEGORIESFILTER, 
        //     Set.of("categoryName"), data.getValue(), filterProvider);

        return ResponseEntity.ok().body(data);
    }

    @GetMapping("categories/{categoryName}")
    public ResponseEntity<MappingJacksonValue> getCategory(
        @PathVariable(name = "categoryName") String categoryName
    ){
        var data = categoriesService.getCategory(categoryName);
        SimpleFilterProvider filterProvider = new SimpleFilterProvider().setFailOnUnknownId(true);
        var result = jsonFilterService.serializeAll(ShopConstant.CATEGORIESFILTER, data, filterProvider);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("categories/{categoryName}/products")
    public ResponseEntity<MappingJacksonValue> getProductsByCategoryName(
        @PathVariable(name = "categoryName") String categoryName,
        @RequestParam(name = "page", required=false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;

        categoryName = categoryName.replaceAll("-", "_");

        var productsPageable = productsService.getProductsByCategoryName(
            pageNumber, ShopConstant.PAGESIZE, categoryName);

        SimpleFilterProvider filterProvider = new SimpleFilterProvider().setFailOnUnknownId(true);
        var data = jsonFilterService.filterOutAllExcept(ShopConstant.PRODUCTSFILTER, 
            Set.of("productID", "productName", "unitPrice", "unitsInStock", "categoryID"), 
            productsPageable, filterProvider);

        data = jsonFilterService.filterOutAllExcept(ShopConstant.CATEGORIESFILTER, 
            Set.of("categoryName"), data.getValue(), filterProvider);

        return ResponseEntity.ok().body(data);
    }

    // shippers
    @GetMapping("shippers")
    public ResponseEntity<MappingJacksonValue> getShippers(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<Shippers> shippers = shippersService.getShippers(pageNumber, ShopConstant.PAGESIZE);
        SimpleFilterProvider filterProvider = new SimpleFilterProvider().setFailOnUnknownId(true);
        var data = jsonFilterService.serializeAll(ShopConstant.SHIPPERSFILTER, shippers, filterProvider);
        
        return ResponseEntity.ok().body(data);
    }
    

    // products
    @GetMapping("products")
    public ResponseEntity<Page<CurrentProductList>> getProducts(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        var productsPageable =productsService.getCurrentProductList(pageNumber, ShopConstant.PAGESIZE);
        return ResponseEntity.ok().body(productsPageable);
    }
    
    @GetMapping("products/id/{productID}")
    public ResponseEntity<ProductDetails> getProductByID(
        @PathVariable(name = "productID") Integer productID
    ){
        ProductDetails productDetails = productsService.getProductDetailsByID(productID);
        return ResponseEntity.ok().body(productDetails);
    }

    @GetMapping("/products/recommended")
    public ResponseEntity<MappingJacksonValue> getRecommendedProducts(){
        List<Products> products = productsService.getRecommendedProducts(0, 3);
        SimpleFilterProvider filterProvider = new SimpleFilterProvider().setFailOnUnknownId(true);
        var data = jsonFilterService.filterOutAllExcept(ShopConstant.PRODUCTSFILTER, 
            Set.of("productID", "productName", "unitPrice", "unitsInStock", "categoryID"), 
            products, filterProvider);
        data = jsonFilterService.filterOutAllExcept(ShopConstant.CATEGORIESFILTER, 
            Set.of("categoryName"), data.getValue(), filterProvider);
            
        return ResponseEntity.ok().body(data);
    }
}
