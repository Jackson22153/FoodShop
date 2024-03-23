package com.phucx.shop.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data @Entity
public class Territories {
    @Id
    @Column(nullable = false, length = 20 ,name = "TerritoryID", columnDefinition = "TerritoryID")
    private String territoryID;
    @Column(nullable = false, length = 50 ,name = "TerritoryDescription", columnDefinition = "TerritoryDescription")
    private String territoryDescription;
    @ManyToOne
    @JoinColumn(name = "RegionID", referencedColumnName = "RegionID")
    private Region regionID;

    @ManyToMany(mappedBy = "territories")
    private List<Employees> employees;

}
