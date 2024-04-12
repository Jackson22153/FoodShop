package com.phucx.account.model;

import java.time.LocalDateTime;
import com.phucx.account.compositeKey.OrderDetailsDiscountsID;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "OrderDetailsDiscounts")
@NamedStoredProcedureQuery(name = "OrderDetailsDiscounts.insertOrderDetailDiscount", 
    procedureName = "InsertOrderDetailDiscount", 
    parameters = {
        @StoredProcedureParameter(name="orderID", type = Integer.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="productID", type = Integer.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="discountID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="appliedDate", type = LocalDateTime.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    })
public class OrderDetailsDiscounts {
    @EmbeddedId
    private OrderDetailsDiscountsID id;
    private LocalDateTime appliedDate;
}
