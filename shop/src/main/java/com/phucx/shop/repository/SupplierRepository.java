package com.phucx.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.shop.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

}
