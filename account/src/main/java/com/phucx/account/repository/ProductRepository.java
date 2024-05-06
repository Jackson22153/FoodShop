package com.phucx.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("""
        SELECT p FROM Product p \
        WHERE p.productID IN ?1 \
        ORDER BY p.productID ASC
        """)
    List<Product> findAllByIdAscending(List<Integer> productIds);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Product SET unitsInStock=?2 \
        WHERE productID=?1
        """)
    Integer updateProductInStocks(Integer productID, Integer quantity);

    Optional<Product> findByProductName(String productName);
}
