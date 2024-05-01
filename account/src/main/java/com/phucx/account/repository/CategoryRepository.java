package com.phucx.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.Categories;


@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer> {
    
    Optional<Categories> findByCategoryName(String categoryName);

    @Modifying
    @Transactional
    @Query(""" 
        UPDATE Categories SET categoryName=?1, description=?2, picture=?3 \
        WHERE categoryID=?4
            """)
    int updateCategory(String categoryName, String description, String picture, Integer categoryID);

    
}
