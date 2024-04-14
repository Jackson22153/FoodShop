package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.CustomerAccounts;
import java.util.Optional;



@Repository
public interface CustomerAccountsRepository extends JpaRepository<CustomerAccounts, String>{
    public CustomerAccounts findByUsername(String username);

    @Modifying
    @Transactional
    @Procedure(name = "createCustomerInfo")
    public void createCustomerInfo(@Param("customerID") String customerID, 
        @Param("contactName") String contactName, @Param("username") String username);
    
    Optional<CustomerAccounts> findByCustomerID(String customerID);
}
