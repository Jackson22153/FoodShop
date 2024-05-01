package com.phucx.account.service.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.account.model.Categories;
import com.phucx.account.repository.CategoryRepository;
import com.phucx.account.service.github.GithubService;

import jakarta.persistence.EntityExistsException;
import jakarta.ws.rs.NotFoundException;

@Service
public class CategoriesServiceImp implements CategoriesService{
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GithubService githubService;
	@Override
	public boolean updateCategory(Categories category) {
        if(category.getCategoryID()==null) throw new NullPointerException("Category Id is null");
        Categories fetchedCategory = categoryRepository.findById(category.getCategoryID())
            .orElseThrow(()-> new NotFoundException("Category " + category.getCategoryName() + " does not found"));


        Integer check = categoryRepository.updateCategory(
            category.getCategoryName(), category.getDescription(), 
            category.getPicture(), fetchedCategory.getCategoryID());
        if(check>0) return true;
        return false;
        // try {
        //     Categories fechedCategory = categoryRepository
        //         .findByCategoryName(category.getCategoryName());   
        //     if(fechedCategory!=null){
        //         String picture = category.getPicture();
        //         if(picture!=null){
        //             if(fechedCategory.getPicture()==null){
        //                 picture = githubService.uploadImage(picture);
        //             }else{
        //                 int comparedPicture =fechedCategory.getPicture()
        //                     .compareToIgnoreCase(picture);
        //                 if(comparedPicture!=0){
        //                     picture = githubService.uploadImage(picture);
        //                 }else if(comparedPicture==0){
        //                     picture = fechedCategory.getPicture();
        //                 }
        //             }
        //         }
        //         int check = categoryRepository.updateCategory(category.getCategoryName(), 
        //             category.getDescription(), picture, category.getCategoryID());
        //         if(check>0){
        //             return true;
        //         }
        //     } 
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        
	}
	@Override
	public boolean createCategory(Categories category) {
        var fetchedCategoryOp = categoryRepository.findByCategoryName(category.getCategoryName());
        Boolean isExisted = fetchedCategoryOp.isPresent();
        if(!isExisted){
            Categories newCategory = categoryRepository.save(category);
            if(newCategory!=null && newCategory.getCategoryID()>0) return true;
            return false;
        }
        throw new EntityExistsException("Category " + fetchedCategoryOp.get().getCategoryName() + " already exists");
		// try {
        //     String picture = category.getPicture();
        //     if(fechedCategory==null){
        //         if(picture!=null){
        //             picture = githubService.uploadImage(picture);
        //             category.setPicture(picture);
        //         }
        //         Categories newCategory = categoryRepository.save(category);
        //         if(newCategory!=null) return true;
        //     }
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
	}
    
}
