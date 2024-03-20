package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.account.model.Suppliers;

@Repository
public interface SuppliersRepository extends JpaRepository<Suppliers, Integer> {
    // @Query("SELECT s FROM Suppliers s")
    // Page<Suppliers> findAllSuppliers(Pageable page);
}
