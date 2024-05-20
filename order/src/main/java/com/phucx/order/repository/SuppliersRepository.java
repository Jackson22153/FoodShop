package com.phucx.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.order.model.Supplier;

@Repository
public interface SuppliersRepository extends JpaRepository<Supplier, Integer> {

}
