package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.shop.compositeKey.EmployeeTerritoriesKey;
import com.phucx.shop.model.EmployeeTerritories;


@Repository
public interface EmployeeTerritoriesRepository extends JpaRepository<EmployeeTerritories, EmployeeTerritoriesKey>{
    
}
