package com.phucx.shop.service.categories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.Categories;
import com.phucx.shop.repository.CategoryRepository;

@Service
public class CategoriesServiceImp implements CategoriesService{
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Categories> getCategories(){
        return categoryRepository.findAll();
    }

    public Page<Categories> getCategories(int pageNumber, int pageSize){
        Pageable page = PageRequest.of(pageNumber, pageSize);
        var result = categoryRepository.findAll(page);
        return result;
    }

    @Override
    public Categories getCategory(int categoryID) {
        var category = categoryRepository.findById(categoryID);
        if(category.isPresent()) return category.get();
        return new Categories();
    }

    @Override
    public Categories getCategory(String categoryName) {
        var category = categoryRepository.findByCategoryName(categoryName);
        return category;
    }
}
