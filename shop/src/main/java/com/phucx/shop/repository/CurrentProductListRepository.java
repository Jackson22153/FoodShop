package com.phucx.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.CurrentProductList;
import java.util.List;




@Repository
public interface CurrentProductListRepository extends JpaRepository<CurrentProductList, Integer>{
    @Query("""
        SELECT c FROM CurrentProductList c \
        WHERE productName LIKE ?1     
        """)
    Page<CurrentProductList> searchCurrentProductsByProductName(String productName, Pageable page);

    Page<CurrentProductList> findByCategoryName(String categoryName, Pageable page);

    @Query("""
        SELECT c FROM CurrentProductList c \
        WHERE c.productID IN ?1
        """)
    List<CurrentProductList> findByProductILists(List<Integer> productIDs);
}
