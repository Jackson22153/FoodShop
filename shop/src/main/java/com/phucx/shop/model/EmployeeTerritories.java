package com.phucx.shop.model;

import com.phucx.shop.compositeKey.EmployeeTerritoriesKey;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity @Data
@Table(name = "EmployeeTerritories")
public class EmployeeTerritories {
    @EmbeddedId
    private EmployeeTerritoriesKey id;

    @ManyToOne
    @JoinColumn(name = "TerritoryID", referencedColumnName = "TerritoryID", insertable=false, updatable=false)
    private Territories territories;

    @ManyToOne
    @JoinColumn(name = "EmployeeID", referencedColumnName = "EmployeeID", insertable=false, updatable=false)
    private Employees employees;
}
