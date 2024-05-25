package com.phucx.shop.service.product;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.phucx.shop.model.CurrentProductList;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ProductDetail;
import com.phucx.shop.repository.CurrentProductListRepository;
import com.phucx.shop.repository.ProductDetailRepository;
import com.phucx.shop.repository.ProductRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CurrentProductListRepository currentProductListRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;

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
        log.info("getRecommendedProducts(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProductList> products = currentProductListRepository.findProductsRandom(pageable);
        return products.getContent();
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

    // search product by name like
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
    public ProductDetail getProductDetail(int productID) {
        ProductDetail product = productDetailRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        return product;
    }

    @Override
    public Page<CurrentProductList> getRecommendedProductsByCategory(
        int productID, String categoryName, int pageNumber, int pageSize) {

        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProductList> products = currentProductListRepository.findRandomByCategoryName(productID, categoryName, page);
        
        return products;
    }

    @Override
    public boolean updateProductDetail(ProductDetail productDetail) {  
        log.info("updateProductDetail()", productDetail.toString());
        if(productDetail.getProductID()==null) throw new NotFoundException("Product Id is null");
        ProductDetail fetchedProduct = productDetailRepository.findById(productDetail.getProductID())
            .orElseThrow(()-> new NotFoundException("Product " + productDetail.getProductID() + " does not found"));
        
        Boolean result = productDetailRepository.updateProduct(fetchedProduct.getProductID(), productDetail.getProductName(), 
            productDetail.getQuantityPerUnit(), productDetail.getUnitPrice(), productDetail.getUnitsInStock(),  
            productDetail.getDiscontinued(), productDetail.getPicture(), productDetail.getDescription(), 
            productDetail.getCategoryID());
        return result;
    }
    @Override
    public boolean insertProductDetail(ProductDetail productDetail) {
        log.info("insertProductDetail({})", productDetail);
        List<Product> products = productRepository.findByProductName(productDetail.getProductName());
        if(!products.isEmpty()){
            throw new EntityExistsException("Product " + productDetail.getProductName() + " already exists");
        }
        Boolean result = productDetailRepository.insertProduct(
            productDetail.getProductName(), productDetail.getQuantityPerUnit(), 
            productDetail.getUnitPrice(), productDetail.getUnitsInStock(), 
            productDetail.getDiscontinued(), productDetail.getPicture(), 
            productDetail.getDescription(), productDetail.getCategoryID());
        return result;
    }

}
