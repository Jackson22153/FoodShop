package com.phucx.shop.service.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.phucx.shop.model.CurrentProductList;
import com.phucx.shop.model.CurrentSalesProduct;
import com.phucx.shop.model.ProductDetails;
import com.phucx.shop.model.Product;
import com.phucx.shop.repository.CurrentProductListRepository;
import com.phucx.shop.repository.ProductDetailsRepository;
import com.phucx.shop.repository.ProductRepository;
import com.phucx.shop.repository.SalesByCategoryRepository;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SalesByCategoryRepository salesByCategoryRepository;
    @Autowired
    private CurrentProductListRepository currentProductListRepository;
    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getProducts(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        
        var productsPageable = productRepository.findAll(page);
        return productsPageable;
    }

    @Override
    public Product getProduct(int productID) {
        var product = productRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        return product;
    }

    @Override
    public List<Product> getProducts(String productName) {
        var products = productRepository.findByProductName(productName);
        return products;
    }

    @Override
    public Page<Product> getProductsByName(int pageNumber, int pageSize, String productName) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        var productsPageable = productRepository.findByProductName(productName, page);

        return productsPageable;
    }

    @Override
    public Page<Product> getProductsByCategoryName(int pageNumber, int pageSize, String categoryName) {
        Pageable page = PageRequest.of(pageNumber, pageSize);

        // System.out.println(categoryName);
        var data = productRepository.findByCategoryNameLike(categoryName, page);
        return data;
    }

    @Override
    public List<CurrentProductList> getRecommendedProducts(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        var salesByCategory = salesByCategoryRepository
            .findAllByOrderByProductSalesDesc(page);

        List<Integer> productIDs = salesByCategory.stream()
            .map(product -> product.getProductID())
            .collect(Collectors.toList());
        List<CurrentProductList> products = currentProductListRepository.findAllById(productIDs);
        return products;
    }

    @Override
    public CurrentProductList getCurrentProduct(int productID) {
        CurrentProductList product = currentProductListRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        return product;
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

    @Override
    public Page<CurrentProductList> searchCurrentProducts(String productName, int pageNumber, int pageSize) {
        String searchValue = "%"+productName+"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProductList> products = currentProductListRepository
            .findByProductNameLike(searchValue, page);
        return products;
    }

    @Override
    public Page<CurrentProductList> getCurrentProductsByCategoryName(
        String categoryName, int pageNumber, int pageSize) {
        // replace '-' with "_" for like syntax in sql server
        categoryName = categoryName.replaceAll("-", "_");
        log.info("getCurrentProductsByCategoryName(categoryName={}, pageNumber={}, pageSize={})", categoryName, pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProductList> products = currentProductListRepository
            .findByCategoryNameLike(categoryName, page);
        return products;
    }

    @Override
    public CurrentSalesProduct getCurrentSalesProductsByID(int productID) {
        ProductDetails product = productDetailsRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        CurrentSalesProduct currentSalesProduct = new CurrentSalesProduct(
            product.getProductID(), product.getProductName(), product.getSupplierID(), 
            product.getCategoryID(), product.getQuantityPerUnit(), product.getUnitPrice(), 
            product.getUnitsInStock(), product.getDiscountID(), product.getDiscountPercent(), 
            product.getPicture(), product.getCategoryName(), product.getCompanyName(), product.getDescription());
        
        return currentSalesProduct;
    }

    @Override
    public Page<CurrentProductList> getRecommendedProductsByCategory(
        int productID, String categoryName, int pageNumber, int pageSize) {

        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProductList> products = currentProductListRepository.findRandomByCategoryName(productID, categoryName, page);
        
        return products;
    }

}
