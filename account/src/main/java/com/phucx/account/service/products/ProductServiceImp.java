package com.phucx.account.service.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.phucx.account.model.ProductDetails;
import com.phucx.account.repository.ProductDetailsRepository;

@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    @Override
    public boolean updateProductDetails(ProductDetails productDetails) {
        try {
            productDetailsRepository.updateProduct(
                productDetails.getProductID(), productDetails.getProductName(), productDetails.getQuantityPerUnit(), 
                productDetails.getUnitPrice(), productDetails.getUnitsInStock(), productDetails.getUnitsOnOrder(), 
                productDetails.getReorderLevel(), productDetails.getDiscontinued(), productDetails.getPicture(), 
                productDetails.getCategoryID(), productDetails.getSupplierID());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
