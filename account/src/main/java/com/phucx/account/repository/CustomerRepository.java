package com.phucx.account.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phucx.account.model.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{
}
