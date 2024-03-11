package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.Shippers;

@Repository
public interface ShippersRepository extends JpaRepository<Shippers, Integer>{
    // @Query("SELECT s FROM Shippers s")
    // Page<Shippers> finAllShippers(Pageable page);
}
