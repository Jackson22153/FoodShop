package com.phucx.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.CurrentProduct;

@Repository
public interface CurrentProductRepository extends JpaRepository<CurrentProduct, Integer>{
    @Query("""
        SELECT c FROM CurrentProduct c \
        WHERE productName LIKE ?1     
        """)
    Page<CurrentProduct> searchCurrentProductsByProductName(String productName, Pageable page);


    Page<CurrentProduct> findByCategoryNameLike(String categoryName, Pageable page);

    Page<CurrentProduct> findByProductNameLike(String productName, Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT  * \
        FROM [Current Product List] \
        where CategoryName LIKE ?2 AND ProductID<>?1 \
        ORDER BY NEWID()
        """)
    Page<CurrentProduct> findRandomLikeCategoryNameWithoutProductID(int productID, String categoryName, Pageable page);

    @Query(nativeQuery = true, value = """
        SELECT  * \
        FROM [Current Product List] \
        ORDER BY NEWID()
        """)
    Page<CurrentProduct> findProductsRandom(Pageable page);

    @Query("""
        SELECT c FROM CurrentProduct c \
        WHERE c.productID IN ?1 \
        ORDER BY c.productID Asc
            """)
    List<CurrentProduct> findAllByProductIDOrderByProductIDAsc(List<Integer> productIDs);
}
