package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.account.constant.DiscountType;
import com.phucx.account.model.CurrentValidDiscount;
import java.util.List;



@Repository
public interface CurrentValidDiscountRepository extends JpaRepository<CurrentValidDiscount, Integer>{
    
    List<CurrentValidDiscount> findByDiscountType(DiscountType discountType);
}
