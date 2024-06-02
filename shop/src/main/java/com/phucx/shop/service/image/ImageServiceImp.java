package com.phucx.shop.service.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.phucx.shop.config.FileProperties;
import com.phucx.shop.model.Category;
import com.phucx.shop.model.CurrentProduct;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ProductDetail;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageServiceImp implements ImageService{
    @Autowired
    private FileProperties fileProperties;

    @Value("${spring.application.name}")
    private String serverName;

    private final String imagePath = "/image";
    private final String imageUri = "/home" + imagePath;

    @Override
    public byte[] getImage(String imagename) throws IOException {
        log.info("getImage({})", imagename);
        String filePath = fileProperties.getImageStoredLocation();
        if(filePath.charAt(filePath.length()-1)!='/') filePath+='/';
        Path path = Paths.get(filePath+imagename);
        return Files.readAllBytes(path);
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        log.info("uploadImage({})", file.getOriginalFilename());
        if(file.isEmpty()) throw new NotFoundException("Image does not found");
        // set filename
        String filename = file.getOriginalFilename();
        int dotIndex = filename.lastIndexOf(".");
        String extension = filename.substring(dotIndex+1);
        String randomName = UUID.randomUUID().toString();
        String newFile = randomName + "." + extension;
        // set stored location
        Path targetPath = Path.of(fileProperties.getImageStoredLocation(), newFile);
        // copy image to stored location
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return newFile;
    }

    @Override
    public String getCurrentUrl(String requestUri, Integer serverPort) {
        String host = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        String imageUri = this.getUri(requestUri);
        String url = host + ":" + serverPort + imageUri;
        return url;
    }

    private String getUri(String requestUri){
        int slashIndex = requestUri.lastIndexOf("/");
        String imageUri = requestUri.substring(0, slashIndex) + this.imagePath;
        return imageUri;
    }

    @Override
    public String getMimeType(String imagename) throws IOException {
        log.info("getMimeType({})", imagename);
        String storedLocation = fileProperties.getImageStoredLocation();
        if(storedLocation.charAt(storedLocation.length()-1)!='/') storedLocation+='/';
        Path path = Paths.get(storedLocation + imagename);
        String mimeType = Files.probeContentType(path);
        return mimeType;
    }

    @Override
    public List<Product> setProductsImage(List<Product> products) {
        products.stream().forEach(product ->{
            if(product.getPicture()!=null && product.getPicture().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + this.getUri(imageUri);
                product.setPicture(uri + "/" + product.getPicture());
            }
        });
        return products;
    }

    @Override
    public Product setProductImage(Product product) {
        // filtering product 
        if(!(product.getPicture()!=null && product.getPicture().length()>0)) return product;
        // product has image
        String imageUrl = "/" + serverName + this.getUri(imageUri);
        product.setPicture(imageUrl + "/" + product.getPicture());
        return product;
    }

    @Override
    public CurrentProduct setCurrentProductImage(CurrentProduct product) {
        // filtering product 
        if(!(product.getPicture()!=null && product.getPicture().length()>0)) return product;
        // product has image
        String imageUrl = "/" + serverName + this.getUri(imageUri);
        product.setPicture(imageUrl + "/" + product.getPicture());
        return product;
    }

    @Override
    public List<CurrentProduct> setCurrentProductsImage(List<CurrentProduct> products) {
        products.stream().forEach(product ->{
            if(product.getPicture()!=null && product.getPicture().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + this.getUri(imageUri);
                product.setPicture(uri + "/" + product.getPicture());
            }
        });
        return products;
    }

    @Override
    public ProductDetail setProductDetailImage(ProductDetail product) {
        // filtering product 
        if(!(product.getPicture()!=null && product.getPicture().length()>0)) return product;
        // product has image
        String imageUrl = "/" + serverName + this.getUri(imageUri);
        product.setPicture(imageUrl + "/" + product.getPicture());
        return product;
    }

    @Override
    public List<ProductDetail> setProductDetailsImage(List<ProductDetail> products) {
        products.stream().forEach(product ->{
            if(product.getPicture()!=null && product.getPicture().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + this.getUri(imageUri);
                product.setPicture(uri + "/" + product.getPicture());
            }
        });
        return products;
    }

    @Override
    public String getImageName(String url) {
        if(url==null) return null;
        int indexOfSlash = url.lastIndexOf("/");
        String filename = url.substring(indexOfSlash + 1);
        log.info("filename: {}", filename);
        return filename;
    }

    @Override
    public Category setCategoryImage(Category category) {
        // filtering product 
        if(!(category.getPicture()!=null && category.getPicture().length()>0)) return category;
        // product has image
        String imageUrl = "/" + serverName + this.getUri(imageUri);
        category.setPicture(imageUrl + "/" + category.getPicture());
        return category;
    }

    @Override
    public List<Category> setCategoriesImage(List<Category> categories) {
        categories.stream().forEach(category ->{
            if(category.getPicture()!=null && category.getPicture().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + this.getUri(imageUri);
                category.setPicture(uri + "/" + category.getPicture());
            }
        });
        return categories;
    }
    
}
