package com.phucx.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.CurrentProductList;




@Repository
public interface CurrentProductListRepository extends JpaRepository<CurrentProductList, Integer>{
    @Query("""
        SELECT c FROM CurrentProductList c \
        WHERE productName LIKE ?1     
        """)
    Page<CurrentProductList> searchCurrentProductsByProductName(String productName, Pageable page);


    Page<CurrentProductList> findByCategoryNameLike(String categoryName, Pageable page);

    Page<CurrentProductList> findByProductNameLike(String productName, Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT  * \
        FROM [Current Product List] \
        where CategoryName=?2 AND ProductID<>?1 \
        ORDER BY NEWID()
        """)
    Page<CurrentProductList> findRandomByCategoryName(int productID, String categoryName, Pageable page);

    @Query(nativeQuery = true, value = """
        SELECT  * \
        FROM [Current Product List] \
        ORDER BY NEWID()
        """)
    Page<CurrentProductList> findProductsRandom(Pageable page);

    @Query("""
        SELECT c FROM CurrentProductList c \
        WHERE c.productID IN ?1 \
        ORDER BY c.productID Asc
            """)
    List<CurrentProductList> findAllByProductIDOrderByProductIDAsc(List<Integer> productIDs);
}
