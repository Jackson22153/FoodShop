package com.phucx.account.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "Discounts.insertDiscount",
        procedureName = "insertDiscount",
        parameters = {
            @StoredProcedureParameter(name="@discountAmount", mode = ParameterMode.IN, type = Float.class),
            @StoredProcedureParameter(name="@startDate", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="@endDate", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="@productID", mode = ParameterMode.IN, type = Integer.class),
        }
    ),
    @NamedStoredProcedureQuery(name = "Discounts.updateDiscount",
        procedureName = "updateDiscount",
        parameters = {
            @StoredProcedureParameter(name="@discountAmount", mode = ParameterMode.IN, type = Float.class),
            @StoredProcedureParameter(name="@startDate", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="@endDate", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="@productID", mode = ParameterMode.IN, type = Integer.class),
        }
    ),

})
public class Discounts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer discountID;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Float discountAmount;

    @OneToOne
    @JoinColumn(name = "ProductID", referencedColumnName = "productID")
    private Products product;
}
