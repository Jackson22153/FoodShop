package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.Shipper;

@Repository
public interface ShipperRepository extends JpaRepository<Shipper, Integer>{
    // @Query("SELECT s FROM Shipper s")
    // Page<Shipper> finAllShipper(Pageable page);
}
