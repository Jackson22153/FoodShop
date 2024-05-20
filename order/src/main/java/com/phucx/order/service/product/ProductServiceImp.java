package com.phucx.order.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.order.model.Product;
import com.phucx.order.repository.ProductRepository;
import jakarta.ws.rs.NotFoundException;

@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product getProduct(int productID) {
        return productRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
    }

    @Override
    public Boolean updateProductInStocks(int productID, int value) {
        Product product = this.getProduct(productID);
        Integer check = productRepository.updateProductInStocks(product.getProductID(), value);
        if(check>0) return true;
        return false;
    }

    @Override
    public List<Product> getProducts(List<Integer> productIDs) {
        return productRepository.findAllById(productIDs);
    }
    
}
