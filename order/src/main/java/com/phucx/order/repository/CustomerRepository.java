package com.phucx.order.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phucx.order.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{

}
