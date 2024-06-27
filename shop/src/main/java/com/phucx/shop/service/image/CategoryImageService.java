package com.phucx.shop.service.image;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.Category;

public interface CategoryImageService {
    public byte[] getCategoryImage(String imageName) throws IOException;
    public String uploadCategoryImage(MultipartFile file) throws IOException, NotFoundException;
    public String getCategoryMimeType(String file) throws IOException;
    public String getCurrentUrl(String requestUri, Integer serverPort);
    // set image for category
    public Category setCategoryImage(Category category);
    public List<Category> setCategoriesImage(List<Category> categories);
}
