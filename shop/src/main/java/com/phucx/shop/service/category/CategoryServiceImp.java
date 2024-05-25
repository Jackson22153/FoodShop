package com.phucx.shop.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.Category;
import com.phucx.shop.repository.CategoryRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImp implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

    public Page<Category> getCategories(int pageNumber, int pageSize){
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return categoryRepository.findAll(page);
    }

    @Override
    public Category getCategory(int categoryID) {
        return categoryRepository.findById(categoryID)
        .orElseThrow(()-> new NotFoundException("Category " + categoryID + " does not found"));
    }

    @Override
    public Category getCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName)
            .orElseThrow(()-> new NotFoundException("Category " + categoryName + " does not found"));
    }

	@Override
	public boolean updateCategory(Category category) {
        log.info("updateCategory({})", category);
        if(category.getCategoryID()==null) throw new NullPointerException("Category Id is null");
        Category fetchedCategory = this.getCategory(category.getCategoryID());
        // update category
        Integer check = categoryRepository.updateCategory(
            category.getCategoryName(), category.getDescription(), 
            category.getPicture(), fetchedCategory.getCategoryID());
        if(check>0) return true;
        return false;
	}
    
	@Override
    @Modifying
    @Transactional
	public boolean createCategory(Category category) {
        log.info("createCategory({})", category);
        Optional<Category> fetchedCategoryOp = categoryRepository.findByCategoryName(category.getCategoryName());
        if(fetchedCategoryOp.isPresent()){
            throw new EntityExistsException("Category " + fetchedCategoryOp.get().getCategoryName() + " is already existed");
        }
        Category newCategory = categoryRepository.save(category);
        if(newCategory!=null && newCategory.getCategoryID()>0) return true;
        return false;
        
	}
}
