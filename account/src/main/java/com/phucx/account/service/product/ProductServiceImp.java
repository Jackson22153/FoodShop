package com.phucx.account.service.product;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.account.model.ProductDetails;
import com.phucx.account.repository.ProductDetailsRepository;
import com.phucx.account.service.github.GithubService;

import jakarta.persistence.EntityExistsException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    @Autowired
    private GithubService githubService;

    @Override
    public boolean updateProductDetails(ProductDetails productDetails) {  
        log.info("updateProductDetails()", productDetails.toString());
        if(productDetails.getProductID()==null) throw new NullPointerException("Product Id is null");
        ProductDetails fetchedProduct = productDetailsRepository.findById(productDetails.getProductID())
            .orElseThrow(()-> new NotFoundException("Product " + productDetails.getProductID() + " does not found"));
        
        Boolean result = productDetailsRepository.updateProduct(fetchedProduct.getProductID(), productDetails.getProductName(), 
            productDetails.getQuantityPerUnit(), productDetails.getUnitPrice(), productDetails.getUnitsInStock(), 
            productDetails.getUnitsOnOrder(), productDetails.getReorderLevel(), productDetails.getDiscontinued(), 
            productDetails.getPicture(), productDetails.getDescription(), productDetails.getCategoryID(), 
            productDetails.getSupplierID());
        return result;
    }
    @Override
    public boolean insertProductDetails(ProductDetails productDetails) {
        log.info("insertProductDetails({})", productDetails);
        if(productDetails.getProductID()==null) throw new NullPointerException("Product Id is null");
        Optional<ProductDetails> productOptional = productDetailsRepository.findById(productDetails.getProductID());
        if(productOptional.isEmpty()){
            Boolean result = productDetailsRepository.insertProduct(
                productDetails.getProductName(), productDetails.getQuantityPerUnit(), 
                productDetails.getUnitPrice(), productDetails.getUnitsInStock(), 
                productDetails.getUnitsOnOrder(), productDetails.getReorderLevel(), 
                productDetails.getDiscontinued(), productDetails.getPicture(), 
                productDetails.getDescription(), productDetails.getCategoryID(), 
                productDetails.getSupplierID());
            return result;
        }
        throw new EntityExistsException("Product " + productOptional.get().getProductID() + " already exists");
    }
    @Override
    public ProductDetails getProductDetails(int productID) {
        ProductDetails product = productDetailsRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        return product;
    }
    
}
