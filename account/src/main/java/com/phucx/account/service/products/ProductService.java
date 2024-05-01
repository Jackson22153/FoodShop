package com.phucx.account.service.products;

import com.phucx.account.model.ProductDetails;

public interface ProductService {
    public boolean updateProductDetails(ProductDetails productDetails);
    public boolean insertProductDetails(ProductDetails productDetails);
    public ProductDetails getProductDetails(int productID);
}
