package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.Territories;

@Repository
public interface TerritoriesRepository extends JpaRepository<Territories, String> {
    
}
