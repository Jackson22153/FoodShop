package com.phucx.shop.service.category;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.shop.model.Category;

public interface CategoryService {
    public List<Category> getCategories();
    public Page<Category> getCategories(int pageNumber, int pageSize);
    public Category getCategory(int categoryID);
    public Category getCategory(String categoryName);
    // update 
    boolean updateCategory(Category category);
    // create
    boolean createCategory(Category category);
} 
