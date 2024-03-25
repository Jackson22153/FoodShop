package com.phucx.account.service.discounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.account.model.Discounts;
import com.phucx.account.repository.DiscountsRepository;
import com.phucx.account.repository.ProductsRepository;

@Service
public class DiscountsServiceImp implements DiscountsService{
    @Autowired
    private DiscountsRepository discountsRepository;
    @Autowired
    private ProductsRepository productsRepository;
    public Boolean insertDiscount(Discounts discount){
        try {
            int productID = discount.getProduct().getProductID();
            var opProduct = productsRepository.findById(productID);
            if(opProduct.isPresent()){
                discountsRepository.insertDiscount(discount.getDiscountAmount(), discount.getStartDate(), 
                    discount.getEndDate(), productID);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public Boolean updateDiscount(Discounts discount) {
        int productID = discount.getProduct().getProductID();
        try {
            var opcheck = productsRepository.findById(productID);
            if(opcheck.isPresent()){
                int check = discountsRepository.updateDiscount(
                    discount.getDiscountAmount(), discount.getStartDate(), 
                    discount.getEndDate(), productID);
                if(check>0) return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
