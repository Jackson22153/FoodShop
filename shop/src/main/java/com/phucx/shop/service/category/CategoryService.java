package com.phucx.shop.service.category;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.shop.exceptions.EntityExistsException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.Category;


public interface CategoryService {
    public List<Category> getCategories();
    public Page<Category> getCategories(int pageNumber, int pageSize);
    public Category getCategory(int categoryID) throws NotFoundException;
    public Category getCategory(String categoryName) throws NotFoundException;
    // update 
    Boolean updateCategory(Category category) throws NotFoundException;
    // create
    Boolean createCategory(Category category) throws EntityExistsException;
} 
