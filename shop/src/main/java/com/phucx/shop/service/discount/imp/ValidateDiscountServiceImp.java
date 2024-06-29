package com.phucx.shop.service.discount.imp;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.shop.constant.DiscountTypeConst;
import com.phucx.shop.exceptions.InvalidDiscountException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.Discount;
import com.phucx.shop.model.DiscountType;
import com.phucx.shop.model.ProductDiscountsDTO;
import com.phucx.shop.repository.DiscountRepository;
import com.phucx.shop.repository.DiscountTypeRepository;
import com.phucx.shop.service.discount.ValidateDiscountService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ValidateDiscountServiceImp implements ValidateDiscountService{
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private DiscountTypeRepository discountTypeRepository;

    @Override
    public Boolean validateDiscountsOfProduct(ProductDiscountsDTO productDiscounts) throws InvalidDiscountException {
        log.info("validateDiscountsOfProduct({})", productDiscounts);
        try {
            Integer productID = productDiscounts.getProductID();
            for(String discountID: productDiscounts.getDiscountIDs()){
                // validate number of discount type of a product 
                boolean isValid = false;
                // validate discount according to discount type
                isValid = this.validateDiscount(productID, discountID, productDiscounts.getAppliedDate());
                if(!isValid) return false;
            }
            return true;
        } catch (NotFoundException e) {
            log.error("Error: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean validateDiscount(Integer productID, String discountID, LocalDateTime appliedDate) throws NotFoundException {
        log.info("validateDiscount(productID={}, discountID={})", productID, discountID);
        // get discountType
        Discount discount = this.getDiscount(discountID);
        DiscountType fetchedDiscountType = this.getDiscountType(discount.getDiscountTypeID());
        String discountType = fetchedDiscountType.getDiscountType();
        // validate according to discount's type
        if(DiscountTypeConst.Percentage_based.getValue().equalsIgnoreCase(discountType)){
            return this.validatePercenageBasedDiscount(productID, discountID, appliedDate);
        }else if(DiscountTypeConst.Code.getValue().equalsIgnoreCase(discountType)){
            return this.validateCodeDiscount(productID, discountID, appliedDate);
        }
        throw new NotFoundException("Discount Type "+discountType+" does not found");
    }

    @Override
    public Boolean validateDiscountsOfProducts(List<ProductDiscountsDTO> productsDiscounts) {
        log.info("validateDiscountsOfProducts({})", productsDiscounts);
        try {
            for(ProductDiscountsDTO product: productsDiscounts){
                Boolean isValid = this.validateDiscountsOfProduct(product);
                if(!isValid) return false;
            }
            return true;
        } catch (InvalidDiscountException e) {
            log.warn("Error: ", e.getMessage());
            return false;
        }
    }

    // validate discount for code discount
    private boolean validateCodeDiscount(Integer productID, String discountID, LocalDateTime appliedDate){
        log.info("validateCodeDiscount(productID={}, discountID={}, appliedDate={})", productID, discountID, appliedDate);
        return false;
    }

    // validate discount for percenage-based discount
    private boolean validatePercenageBasedDiscount(Integer productID, String discountID, LocalDateTime appliedDate)
        throws NotFoundException
    {
        log.info("validatePercenageBasedDiscount(productID={}, itemDiscount={}, appliedDate={})", 
            productID, discountID, appliedDate);
        return discountRepository.findByDiscountIDAndProductID(discountID, productID)
            .map(discount -> {
                boolean isValid = false;
                Boolean isActive = discount.getActive();
   
                if((appliedDate.isEqual(discount.getStartDate()) || appliedDate.isAfter(discount.getStartDate()))&&
                    (appliedDate.isEqual(discount.getEndDate()) || appliedDate.isBefore(discount.getEndDate()))){
                        isValid = true;
                }
                if(!isValid)
                    log.info("Discount {} is out of date", discount.getDiscountID());
                if(!isActive)
                    log.info("Discount {} is not available", discount.getDiscountID());
                return isActive && isValid;
            }).orElseThrow(() -> new NotFoundException("Discount " +discountID+" does not found"));
    }


    private Discount getDiscount(String discountID) throws NotFoundException {
        log.info("getDiscount(discountID={})", discountID);
        return discountRepository.findById(discountID)
            .orElseThrow(()-> new NotFoundException("Discount "+ discountID+" does not found"));
    }

    private DiscountType getDiscountType(int discountTypeID) throws NotFoundException {
        log.info("getDiscountType(discountTypeID={})", discountTypeID);
        return discountTypeRepository.findById(discountTypeID)
            .orElseThrow(() -> new NotFoundException("Discount type " + discountTypeID + " does not found"));
    }
}
