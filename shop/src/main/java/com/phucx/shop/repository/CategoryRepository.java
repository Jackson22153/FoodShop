package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phucx.shop.model.Categories;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer> {
    
    Categories findByCategoryName(String categoryName);
    // @Query("SELECT c FROM Categories c WHERE c.categoryID= ?1")
    // Categories findByID(int categoryID);

    // @Query("SELECT c FROM Categories c WHERE c.categoryName= ?1")
    // Categories findByName(@Size(max = 15) String categoryName);
    
}
