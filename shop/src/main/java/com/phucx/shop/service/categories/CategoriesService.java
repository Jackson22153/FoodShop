package com.phucx.shop.service.categories;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.shop.model.Categories;

public interface CategoriesService {
    public List<Categories> getCategories();
    public Page<Categories> getCategories(int pageNumber, int pageSize);
    public Categories getCategory(int categoryID);
    public Categories getCategory(String categoryName);
} 
