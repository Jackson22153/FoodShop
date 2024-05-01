package com.phucx.account.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Immutable;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import lombok.Data;

@Data
@Entity 
@Immutable
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "ProductDetails.updateProduct",
    procedureName = "updateProduct", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "productId", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "productName", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "quantityPerUnit", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "unitPrice", type = BigDecimal.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "unitsInStock", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "unitsOnOrder", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "reorderLevel", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "discontinued", type = Boolean.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "picture", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "description", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "categoryID", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "supplierID", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = Boolean.class)
    }),
    @NamedStoredProcedureQuery(name = "ProductDetails.insertProduct",
    procedureName = "insertProduct", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "productName", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "quantityPerUnit", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "unitPrice", type = BigDecimal.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "unitsInStock", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "unitsOnOrder", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "reorderLevel", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "discontinued", type = Boolean.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "picture", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "description", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "categoryID", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "supplierID", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = Boolean.class)
    })
})
public class ProductDetails{
    @Id
    private Integer productID;
    private String productName;
    private Integer supplierID;
    private Integer categoryID;
    private String quantityPerUnit;
    private BigDecimal unitPrice;
    private Integer unitsInStock;
    private Integer unitsOnOrder;
    private Integer reorderLevel;
    private Boolean discontinued;
    private String picture;
    private String description;

    private String discountID;
    private Float discountPercent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    private String categoryName;
    private String companyName;
}
