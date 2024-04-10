package com.phucx.account.model;

import java.time.LocalDateTime;
import com.phucx.account.compositeKey.OrderDetailsDiscountsID;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
public class OrderDetailsDiscounts {
    @EmbeddedId
    private OrderDetailsDiscountsID id;
    private LocalDateTime appliedDate;
}
