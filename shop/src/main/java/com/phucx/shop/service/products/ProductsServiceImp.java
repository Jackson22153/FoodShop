package com.phucx.shop.service.products;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.CurrentProductList;
import com.phucx.shop.model.Products;
import com.phucx.shop.model.SalesByCategory;
import com.phucx.shop.repository.CurrentProductListRepository;
import com.phucx.shop.repository.ProductsRepository;
import com.phucx.shop.service.SalesByCategory.SalesByCategoryService;

@Service
public class ProductsServiceImp implements ProductsService{
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private SalesByCategoryService salesByCategoryService;
    @Autowired
    private CurrentProductListRepository currentProductListRepository;

    @Override
    public List<Products> getProducts() {
        return productsRepository.findAll();
    }

    @Override
    public Page<Products> getProducts(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        
        var productsPageable = productsRepository.findAll(page);
        return productsPageable;
    }

    @Override
    public Products getProduct(int productID) {
        var product = productsRepository.findById(productID);
        if(product.isPresent()) return product.get();
        return new Products();
    }

    @Override
    public List<Products> getProducts(String productName) {
        var products = productsRepository.findByProductName(productName);
        return products;
    }

    @Override
    public Page<Products> getProductsByName(int pageNumber, int pageSize, String productName) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        var productsPageable = productsRepository.findByProductName(productName, page);

        return productsPageable;
    }

    @Override
    public Page<Products> getProductsByCategoryName(int pageNumber, int pageSize, String categoryName) {
        Pageable page = PageRequest.of(pageNumber, pageSize);

        // System.out.println(categoryName);
        var data = productsRepository.findByCategoryNameLike(categoryName, page);
        return data;
    }

    @Override
    public List<Products> getRecommendedProducts() {
        List<Products> products = new ArrayList<>();
        
        var salesByCategory = salesByCategoryService
            .findSalesOfProductsPerCategory(0, 3);

        List<SalesByCategory> salesByCategoryData = salesByCategory.getContent();
        for(SalesByCategory i: salesByCategoryData){
            @SuppressWarnings("null")
            var product = productsRepository.findById(i.getProductID());
            if(product.isPresent()) products.add(product.get());
        }
        return products;
    }

    @Override
    public Page<Products> searchProductByName(String productName, int pageNumber, int pageSize) {
        String productPattern = "%" + productName + "%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Products> products = productsRepository.findByProductNameLike(productPattern, page);

        return products;
    }

    @Override
    public CurrentProductList getCurrentProduct(int productID) {
        var option = currentProductListRepository.findById(productID);
        if(option.isPresent())
            return option.get();
        return null;
    }

    @Override
    public List<CurrentProductList> getCurrentProductList() {
        var products = currentProductListRepository.findAll();
        return products;
    }

    @Override
    public Page<CurrentProductList> getCurrentProductList(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return currentProductListRepository.findAll(pageable);
    }

}
