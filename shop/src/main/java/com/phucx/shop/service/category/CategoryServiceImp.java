package com.phucx.shop.service.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.Category;
import com.phucx.shop.repository.CategoryRepository;

@Service
public class CategoryServiceImp implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

    public Page<Category> getCategories(int pageNumber, int pageSize){
        Pageable page = PageRequest.of(pageNumber, pageSize);
        var result = categoryRepository.findAll(page);
        return result;
    }

    @Override
    public Category getCategory(int categoryID) {
        var category = categoryRepository.findById(categoryID);
        if(category.isPresent()) return category.get();
        return new Category();
    }

    @Override
    public Category getCategory(String categoryName) {
        var category = categoryRepository.findByCategoryName(categoryName);
        return category;
    }
}
