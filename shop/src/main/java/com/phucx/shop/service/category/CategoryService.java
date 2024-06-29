package com.phucx.shop.service.category;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

import com.phucx.shop.exceptions.EntityExistsException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.Category;

@CacheConfig(cacheNames = "category")
public interface CategoryService {
    @Cacheable
    public List<Category> getCategories();
    @Cacheable(key = "#pageNumber")
    public Page<Category> getCategories(int pageNumber, int pageSize);
    @Cacheable(key = "#categoryID")
    public Category getCategory(int categoryID) throws NotFoundException;
    @Cacheable(key = "#categoryName")
    public Category getCategory(String categoryName) throws NotFoundException;
    @Cacheable(key = "'like:' + #categoryName")
    public List<Category> getCategoryLike(String categoryName);
    // update 
    @CachePut(key = "#category.categoryID")
    Category updateCategory(Category category) throws NotFoundException;
    // create
    Boolean createCategory(Category category) throws EntityExistsException;
} 
