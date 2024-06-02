package com.phucx.shop.service.image;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.phucx.shop.model.Category;
import com.phucx.shop.model.CurrentProduct;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ProductDetail;

public interface ImageService {
    public byte[] getImage(String imagename) throws IOException;
    public String uploadImage(MultipartFile file) throws IOException;
    public String getCurrentUrl(String requestUri, Integer serverPort);
    public String getMimeType(String imagename) throws IOException;
    public String getImageName(String url);
    // set image for product
    public List<Product> setProductsImage(List<Product> products);
    public Product setProductImage(Product product);
    // set image for current product
    public CurrentProduct setCurrentProductImage(CurrentProduct product);
    public List<CurrentProduct> setCurrentProductsImage(List<CurrentProduct> products);
    // set image for productDetail
    public ProductDetail setProductDetailImage(ProductDetail product);
    public List<ProductDetail> setProductDetailsImage(List<ProductDetail> products);
    // set image for category
    public Category setCategoryImage(Category category);
    public List<Category> setCategoriesImage(List<Category> categories);
}
