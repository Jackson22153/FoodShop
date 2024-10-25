package com.phucx.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.payment.model.StoreLocation;

@Repository
public interface StoreLocationRepository extends JpaRepository<StoreLocation, String>{
    
}
