package com.phucx.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phucx.order.model.CustomerAccount;
import java.util.Optional;

@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, String>{
    Optional<CustomerAccount> findByUsername(String username);
    Optional<CustomerAccount> findByCustomerID(String customerID);
    Optional<CustomerAccount> findByEmail(String email);
    Optional<CustomerAccount> findByUserID(String userID);
}
