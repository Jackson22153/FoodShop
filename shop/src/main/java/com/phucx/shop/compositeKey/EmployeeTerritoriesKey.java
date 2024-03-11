package com.phucx.shop.compositeKey;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data @Embeddable
public class EmployeeTerritoriesKey {
    private Integer employeeID;
    private Integer territoryID;
}
