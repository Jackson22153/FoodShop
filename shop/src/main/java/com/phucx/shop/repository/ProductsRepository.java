package com.phucx.shop.repository;

import java.util.List;
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

       @Query("""
              SELECT p \
              FROM Products p \
              WHERE p.productName Like ?1       
              """)
       Page<Products> findByProductNameLike(String productName, Pageable page);
       // @Query("""
       //        SELECT p \
       //        FROM Products p\
       //        WHERE p.categoryID.categoryName=?1 \
       //        AND p.productName=?2
       //        """)
       // Products findByCategoryNameAndProductName(String categoryName, String productName);
       // @Query("""
       //        SELECT p \
       //        FROM Products p \
       //        WHERE p.productName LIKE CONCAT('%', :letter, '%') \
       //        AND p.discontinued=0 \
       //        ORDER BY p.productName\
       //        """)
       // Page<Products> findByLetter(@Size(max = 40) String letter, Pageable page);


       // @Query("""
       //        SELECT p \
       //        FROM Products p \
       //        WHERE p.discontinued = ?1 \
       //        ORDER BY p.productName\
       //        """)
       // Page<Products> findByDiscontinuedOrderByProductName(boolean discontinued, Pageable page);

       // @Query("SELECT COUNT(*) FROM Products p")
       // int numberOfProducts();

       // @Query("""
       //        SELECT p \
       //        FROM Products p \
       //        ORDER BY p.productName\
       //        """)
       // Page<Products> findAllProducts(Pageable page);

       // List<Products> findTopSales();

       // @Query("SELECT p FROM Products p WHERE p.productName=:productName")
       // Products findByProductName(@Size(max = 40)String productName);

       // @Query("""
       //        SELECT p \
       //        FROM Products p \
       //        WHERE p.discontinued=0 \
       //        AND p.unitsInStock > 0 \
       //        AND p.categoryID.categoryName=?1\
       //        """)
       // Page<Products> findProductsByCategory(@Size(max = 15)String categoryName,Pageable pageable);

       // @Query("""
       //        SELECT p FROM Products p \
       //        WHERE p.productID IN ?1 AND p.productName IN ?2 \
       //        ORDER BY p.productID\
       //        """)
       // List<Products> findProductsWithList(List<Integer> id, List<String> name);
       
       // @Query("""
       //        SELECT p FROM Products p, Suppliers s \
       //        WHERE p.supplierID.supplierID=s.supplierID AND s.supplierID=?1\
       //        """)
       // Page<Products> findProductsBySupplier(int id, Pageable page);

       // @Query("""
       //        SELECT p FROM Products p \
       //        WHERE p.productID= ?1 AND p.productName= ?2 \
       //        """)
       // Products findByProductIdAndProductName(Integer productId, @Size(max = 40) String productName);
}
