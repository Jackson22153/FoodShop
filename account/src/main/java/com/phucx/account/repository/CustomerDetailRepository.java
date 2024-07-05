package com.phucx.account.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.CustomerDetail;
import java.util.Optional;



@Repository
public interface CustomerDetailRepository extends JpaRepository<CustomerDetail, String>{
    @Modifying
    @Transactional
    @Procedure(name = "UpdateCustomerInfo")
    Boolean updateCustomerInfo(String customerID, String contactName, 
        String firstName, String lastName, String address, String city, 
        String phone, String picture);

    @Modifying
    @Transactional
    @Procedure(name = "AddNewCustomer")
    public Boolean addNewCustomer(String profileID, String userID, String username, String email,
        String customerID, String firstName, String lastName, String contactName);

    Optional<CustomerDetail> findByUserID(String userID);

    Page<CustomerDetail> findByCustomerIDLike(String customerID, Pageable pageable);
    Page<CustomerDetail> findByContactNameLike(String contactName, Pageable pageable);
    Page<CustomerDetail> findByUsernameLike(String username, Pageable pageable);
    Page<CustomerDetail> findByEmailLike(String email, Pageable pageable);
        
}
