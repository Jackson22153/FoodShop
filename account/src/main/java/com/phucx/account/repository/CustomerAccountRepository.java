package com.phucx.account.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.CustomerAccount;
import java.util.Optional;



@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, String>{
    public CustomerAccount findByUsername(String username);

    @Modifying
    @Transactional
    @Procedure(name = "createCustomerInfo")
    public void createCustomerInfo(@Param("customerID") String customerID, 
        @Param("contactName") String contactName, @Param("username") String username);
    
    Optional<CustomerAccount> findByCustomerID(String customerID);

    Page<CustomerAccount> findByEmailLike(String email, Pageable pageable);
    Page<CustomerAccount> findByCustomerIDLike(String customerID, Pageable pageable);
    Page<CustomerAccount> findByContactNameLike(String contactName, Pageable pageable);
    Page<CustomerAccount> findByUsernameLike(String username, Pageable pageable);
}
