package com.phucx.shop.service.image.imp;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.shop.config.FileProperties;
import com.phucx.shop.model.Category;
import com.phucx.shop.service.image.CategoryImageService;
import com.phucx.shop.service.image.ImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryImageServiceImp implements CategoryImageService{
    @Value("${spring.application.name}")
    private String serverName;
    @Autowired
    private FileProperties fileProperties;
    @Autowired
    private ImageService imageService;

    private final String imageUri = "/image/category";

    @Override
    public Category setCategoryImage(Category category) {
        // filtering product 
        if(!(category.getPicture()!=null && category.getPicture().length()>0)) return category;
        // product has image
        String imageUrl = "/" + serverName + imageUri;
        category.setPicture(imageUrl + "/" + category.getPicture());
        return category;
    }

    @Override
    public List<Category> setCategoriesImage(List<Category> categories) {
        categories.stream().forEach(category ->{
            if(category.getPicture()!=null && category.getPicture().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                category.setPicture(uri + "/" + category.getPicture());
            }
        });
        return categories;
    }

    @Override
    public byte[] getCategoryImage(String imageName) throws IOException {
        log.info("getCategoryImage({})", imageName);
        return imageService.getImage(imageName, fileProperties.getCategoryImageLocation());
    }

    @Override
    public String uploadCategoryImage(MultipartFile file) throws IOException {
        log.info("uploadCategoryImage({})", file);
        return imageService.uploadImage(file, fileProperties.getCategoryImageLocation());
    }

    @Override
    public String getCategoryMimeType(String file) throws IOException {
        log.info("getCategoryMimeType({})", file);
        return imageService.getMimeType(file, fileProperties.getCategoryImageLocation());
    }

    @Override
    public String getCurrentUrl(String requestUri, Integer serverPort) {
        return imageService.getCurrentUrl(requestUri, serverPort);
    }
}
