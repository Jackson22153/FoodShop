package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.account.model.CustomerDetail;
import java.util.Optional;
import java.util.List;




@Repository
public interface CustomerDetailRepository extends JpaRepository<CustomerDetail, String>{
    @Modifying
    @Transactional
    @Procedure(name = "UpdateCustomerInfo")
    Boolean updateCustomerInfo(String customerID, String contactName, 
        String address, String city, String phone, String picture);

    @Modifying
    @Transactional
    @Procedure(name = "AddNewCustomer")
    public Boolean addNewCustomer(String profileID, String userID, String customerID, String contactName);

    Optional<CustomerDetail> findByUserID(String userID);

    @Query("""
        SELECT c FROM CustomerDetail c WHERE userID IN ?1
            """)
    List<CustomerDetail> findAllByUserID(List<String> userID);
}
