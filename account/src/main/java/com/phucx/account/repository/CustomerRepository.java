package com.phucx.account.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phucx.account.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{
    // @Query("SELECT c FROM Customer c ORDER BY c.contactName")
    // Page<Customer> findAllCustomer(Pageable page);

    // @Query("SELECT c FROM Customer c WHERE c.customerID=:customerID")
    // Customer findByCustomerID(@Size(max = 36, min = 36) String customerID);

    // @Query("""
    //     SELECT c \
    //     FROM Customer c \
    //     WHERE c.contactName LIKE %?1% \
    //     ORDER BY c.contactName\
    //     """)
    // Page<Customer> findByLetter(@Size(max = 40) String letter, Pageable page);

    // @Modifying
    // @Transactional
    // @Query("""
    //     UPDATE Customer \
    //     SET companyName=?1, contactName=?2, contactTitle=?3, address=?4, \
    //         city=?5, region=?6, postalCode=?7, country=?8, phone=?9, fax=?10, picture=?11 \
    //     WHERE customerID=?12\
    //     """)
    // Integer updateCustomer(@Size(max = 40) String companyName,
    //     @Size(max = 30) String contactName,@Size(max = 30) String contactTitle,
    //     @Size(max = 60) String address,@Size(max = 15) String city,@Size(max = 15) String region,
    //     @Size(max = 10) String postalCode,@Size(max = 15) String country,@Size(max = 24) String phone,
    //     @Size(max = 24) String fax,@Size(max = 256) String picture, @Size(min = 36, max = 36) String customerID);

}