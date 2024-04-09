package com.phucx.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.Products;


@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {
    @Query("""
        SELECT p FROM Products p \
        WHERE p.productID IN ?1 \
        ORDER BY p.productID ASC
        """)
    List<Products> findAllByIdAscending(List<Integer> productIds);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Products SET unitsInStock=?2 \
        WHERE productID=?1
        """)
    Integer updateProductInStocks(Integer productID, Integer quantity);
}
