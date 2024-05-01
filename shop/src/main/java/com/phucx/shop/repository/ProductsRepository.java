package com.phucx.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {

       List<Products> findByProductName(String productName);

       Page<Products> findByProductName(String productName, Pageable page);

       @Query("""
              SELECT p \
              FROM Products p \
              WHERE p.categoryID.categoryName=:categoryName \
              """)
       Page<Products> findByCategoryName(String categoryName, Pageable page);

       @Query("""
              SELECT p \
              FROM Products p \
              WHERE p.categoryID.categoryName=?1 AND p.productName=?2
              """)
       Products findByCategoryNameAndProductName(String categoryName, String productName);

       @Query("""
              SELECT p \
              FROM Products p \
              WHERE p.categoryID.categoryName LIKE ?1       
              """)
       Page<Products> findByCategoryNameLike(String categoryName, Pageable page);

       Optional<Products> findByProductIDAndDiscontinued(Integer productID, Boolean discontinued);
}
