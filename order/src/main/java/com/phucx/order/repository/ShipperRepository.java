package com.phucx.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.order.model.Shipper;

@Repository
public interface ShipperRepository extends JpaRepository<Shipper, Integer>{

    
}
