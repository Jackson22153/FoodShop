package com.phucx.shop.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phucx.shop.model.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{
    Optional<Customer> findByUserID(String userID);
}