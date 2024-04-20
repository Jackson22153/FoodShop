package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.CustomerDetail;
import java.util.Optional;


@Repository
public interface CustomerDetailRepository extends JpaRepository<CustomerDetail, String>{
    Optional<CustomerDetail> findByUsername(String username);

    @Modifying
    @Transactional
    @Procedure(name = "UpdateCustomerInfo")
    Boolean updateCustomerInfo(String customerID, String email, 
        String contactName, String address, String city, String phone, 
        String picture);
}
