package com.phucx.shop.model;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFilter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @Entity @ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonFilter("ProductsFilter")
// sql query
// @NamedNativeQuery(
//     name = "Products.findTopSales", 
//     query = """
//             SELECT * \
//             FROM Products p \
//             WHERE p.ProductID IN (SELECT TOP 10 p.ProductID \
//             FROM Products p INNER JOIN [Order Details] od ON p.ProductID = od.ProductID \
//                             INNER JOIN Orders o ON od.OrderID = o.OrderID \
//                                   JOIN Categories c ON p.CategoryID = c.CategoryID \
//             WHERE o.ShippedDate IS NOT NULL AND p.Discontinued=0 \
//             GROUP BY p.ProductID \
//             ORDER BY sum(od.Quantity) DESC)\
//             """,
//     resultSetMapping = "ProductsSuppliersCategoryMapping")
// @SqlResultSetMapping(
//     name = "ProductsSuppliersCategoryMapping",
//     entities = {
//         @EntityResult(
//             entityClass = Products.class,
//             fields = {
//                 @FieldResult(name = "ProductID", column = "ProductID"),
//                 @FieldResult(name = "ProductName", column = "ProductName"),
//                 @FieldResult(name = "SupplierID", column = "SupplierID"),
//                 @FieldResult(name = "CategoryID", column = "CategoryID"),
//                 @FieldResult(name = "QuantityPerUnit", column = "QuantityPerUnit"),
//                 @FieldResult(name = "UnitPrice", column = "UnitPrice"),
//                 @FieldResult(name = "UnitsInStock", column = "UnitsInStock"),
//                 @FieldResult(name = "UnitsOnOrder", column = "UnitsOnOrder"),
//                 @FieldResult(name = "ReorderLevel", column = "ReorderLevel"),
//                 @FieldResult(name = "Discontinued", column = "Discontinued"),
//                 @FieldResult(name = "picture", column = "picture")
//             }
//         ),
//         @EntityResult(
//             entityClass = Categories.class,
//             fields = {
//                 @FieldResult(name = "CategoryID", column = "CategoryID")
//             }
//         ),
//         @EntityResult(
//             entityClass = Suppliers.class,
//             fields = {
//                 @FieldResult(name = "SupplierID", column = "SupplierID")
//             }
//         )
//     }
// )
public class Products {
    @Id
    @GeneratedValue(generator = "native", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "ProductID", nullable = false)
    private Integer productID;

    @Column(name = "ProductName", length = 40, nullable = false)
    private String productName;

    @ManyToOne
    @JoinColumn(name = "SupplierID", referencedColumnName = "SupplierID")
    private Suppliers supplierID;

    @ManyToOne
    @JoinColumn(name = "CategoryID", referencedColumnName = "CategoryID")
    private Categories categoryID;

    @Column(name = "QuantityPerUnit", length = 20)
    private String quantityPerUnit;

    @Column(name = "UnitPrice")
    private double unitPrice;

    @Column(name = "UnitsInStock")
    private Integer unitsInStock;

    @Column(name = "UnitsOnOrder")
    private Integer unitsOnOrder;

    @Column(name = "ReorderLevel")
    private Integer reorderLevel;

    @Column(name = "Discontinued", nullable = false)
    private Boolean discontinued;

    private String picture;
}
