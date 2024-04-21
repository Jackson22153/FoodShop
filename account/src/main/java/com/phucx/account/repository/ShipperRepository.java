package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.account.model.Shipper;

@Repository
public interface ShipperRepository extends JpaRepository<Shipper, Integer>{

    
}
