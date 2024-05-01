package com.phucx.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

       List<Product> findByProductName(String productName);

       Page<Product> findByProductName(String productName, Pageable page);

       @Query("""
              SELECT p \
              FROM Product p \
              WHERE p.categoryID.categoryName=:categoryName \
              """)
       Page<Product> findByCategoryName(String categoryName, Pageable page);

       @Query("""
              SELECT p \
              FROM Product p \
              WHERE p.categoryID.categoryName=?1 AND p.productName=?2
              """)
       Product findByCategoryNameAndProductName(String categoryName, String productName);

       @Query("""
              SELECT p \
              FROM Product p \
              WHERE p.categoryID.categoryName LIKE ?1       
              """)
       Page<Product> findByCategoryNameLike(String categoryName, Pageable page);

       Optional<Product> findByProductIDAndDiscontinued(Integer productID, Boolean discontinued);
}
