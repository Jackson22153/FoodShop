package com.phucx.shop.service.image.imp;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.shop.config.FileProperties;
import com.phucx.shop.model.CurrentProduct;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ProductDetail;
import com.phucx.shop.service.image.ImageService;
import com.phucx.shop.service.image.ProductImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductImageServiceImp implements ProductImageService{
    @Autowired
    private FileProperties fileProperties;
    @Autowired
    private ImageService imageService;

    @Value("${spring.application.name}")
    private String serverName;

    private final String imageUri = "/image/product";

    @Override
    public byte[] getProductImage(String imageName) throws IOException {
        log.info("getProductImage({})", imageName);
        byte[] image = imageService.getImage(imageName, fileProperties.getProductImageLocation());
        return image;
    }

    @Override
    public String uploadProductImage(MultipartFile file) throws IOException {
        log.info("uploadProductImage({})", file);
        String imageName = imageService.uploadImage(file, fileProperties.getProductImageLocation());
        return imageName;
    }

    @Override
    public String getProductMimeType(String file) throws IOException {
        log.info("getProductMimeType({})", file);
        String mimetype = imageService.getMimeType(file, fileProperties.getProductImageLocation());
        return mimetype;
    }

    @Override
    public List<Product> setProductsImage(List<Product> products) {
        products.stream().forEach(product ->{
            if(product.getPicture()!=null && product.getPicture().length()>0){
                String picture = product.getPicture();
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                if(!picture.contains(uri)){
                    product.setPicture(uri + "/" + product.getPicture());
                }
            }
        });
        return products;
    }

    @Override
    public Product setProductImage(Product product) {
        // filtering product 
        if(!(product.getPicture()!=null && product.getPicture().length()>0)) return product;
        // product has image
        String picture = product.getPicture();
        // setting image with image uri
        String uri = "/" + serverName + imageUri;
        if(!picture.contains(uri)){
            product.setPicture(uri + "/" + product.getPicture());
        }
        return product;
    }

    @Override
    public CurrentProduct setCurrentProductImage(CurrentProduct product) {
        // filtering product 
        if(!(product.getPicture()!=null && product.getPicture().length()>0)) return product;
        String picture = product.getPicture();
        // setting image with image uri
        String uri = "/" + serverName + imageUri;
        if(!picture.contains(uri)){
            product.setPicture(uri + "/" + product.getPicture());
        }
        return product;
    }

    @Override
    public List<CurrentProduct> setCurrentProductsImage(List<CurrentProduct> products) {
        products.stream().forEach(product ->{
            if(product.getPicture()!=null && product.getPicture().length()>0){
                String picture = product.getPicture();
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                if(!picture.contains(uri)){
                    product.setPicture(uri + "/" + product.getPicture());
                }
            }
        });
        return products;
    }

    @Override
    public ProductDetail setProductDetailImage(ProductDetail product) {
        // filtering product 
        if(!(product.getPicture()!=null && product.getPicture().length()>0)) return product;
        String picture = product.getPicture();
        // setting image with image uri
        String uri = "/" + serverName + imageUri;
        if(!picture.contains(uri)){
            product.setPicture(uri + "/" + product.getPicture());
        }
        return product;
    }

    @Override
    public List<ProductDetail> setProductDetailsImage(List<ProductDetail> products) {
        products.stream().forEach(product ->{
            if(product.getPicture()!=null && product.getPicture().length()>0){
                String picture = product.getPicture();
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                if(!picture.contains(uri)){
                    product.setPicture(uri + "/" + product.getPicture());
                }
            }
        });
        return products;
    }

    @Override
    public String getCurrentUrl(String requestUri, Integer serverPort) {
        return imageService.getCurrentUrl(requestUri, serverPort);
    }
    
}
