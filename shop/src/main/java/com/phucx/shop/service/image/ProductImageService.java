package com.phucx.shop.service.image;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.phucx.shop.model.CurrentProduct;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ProductDetail;

public interface ProductImageService {
    // get image
    public byte[] getProductImage(String imageName) throws IOException;
    // upload image
    public String uploadProductImage(MultipartFile file) throws IOException;
    // get mimetype for response
    public String getProductMimeType(String file) throws IOException;

    public String getCurrentUrl(String requestUri, Integer serverPort);
    // set image for product
    public List<Product> setProductsImage(List<Product> products);
    public Product setProductImage(Product product);
    // set image for current product
    public CurrentProduct setCurrentProductImage(CurrentProduct product);
    public List<CurrentProduct> setCurrentProductsImage(List<CurrentProduct> products);
    // set image for productDetail
    public ProductDetail setProductDetailImage(ProductDetail product);
    public List<ProductDetail> setProductDetailsImage(List<ProductDetail> products);
}
