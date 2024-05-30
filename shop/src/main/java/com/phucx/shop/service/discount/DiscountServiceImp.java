package com.phucx.shop.service.discount;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.constant.DiscountTypeConst;
import com.phucx.shop.exceptions.InvalidDiscountException;
import com.phucx.shop.model.Discount;
import com.phucx.shop.model.DiscountDetail;
import com.phucx.shop.model.DiscountType;
import com.phucx.shop.model.DiscountWithProduct;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ProductDiscountsDTO;
import com.phucx.shop.repository.DiscountDetailRepository;
import com.phucx.shop.repository.DiscountRepository;
import com.phucx.shop.repository.DiscountTypeRepository;
import com.phucx.shop.repository.ProductRepository;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DiscountServiceImp implements DiscountService{
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DiscountTypeRepository discountTypeRepository;
    @Autowired
    private DiscountDetailRepository discountDetailRepository;

    @Transactional
    public Discount insertDiscount(DiscountWithProduct discount) throws InvalidDiscountException{
        log.info("insertDiscount({})", discount);
        String newDiscountID = UUID.randomUUID().toString();
        log.info("create new discount {}, id: {}", discount.toString(), newDiscountID);
        Integer productID = discount.getProductID();
        if(productID==null) throw new InvalidDiscountException("Missing ProductID for discount ");

        // get discountType
        DiscountType discountType = discountTypeRepository.findByDiscountType(discount.getDiscountType())
            .orElseThrow(()-> new NotFoundException("Discount type "+ discount.getDiscountType()+  " does not found"));
        if(discountType.getDiscountType().equalsIgnoreCase(DiscountTypeConst.Percentage_based.getValue())){
            if(discount.getDiscountCode()==null) 
                throw new InvalidDiscountException("Missing discount code for Code Discount");
        }else {
            discount.setDiscountCode(UUID.randomUUID().toString());
        }

        if(discount.getDiscountPercent()==null) 
            throw new InvalidDiscountException("Missing Discount Percentage");
        if(discount.getStartDate()==null || discount.getEndDate()==null) 
            throw new InvalidDiscountException("Missing Discount start date or end date");
        if(discount.getStartDate().isAfter(discount.getEndDate()))
            throw new InvalidDiscountException("Invalid Discount start date and end date");

        // get product
        Product product = productRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product "+ productID +" does not found"));
        // save discount along with product
        Boolean check = discountDetailRepository.insertDiscount(
            newDiscountID, discount.getDiscountPercent(), 
            discount.getDiscountCode(), discount.getStartDate(), 
            discount.getEndDate(), discount.getActive(), 
            discount.getDiscountType(), product.getProductID());
        if(!check) throw new RuntimeException("New discount " + newDiscountID + " can not be saved");
        return discountRepository.findById(newDiscountID)
            .orElseThrow(()-> new NotFoundException("Discount " + newDiscountID + " does not found"));
        
    }
    @Override
    public Boolean updateDiscount(DiscountWithProduct discount) throws InvalidDiscountException {
        log.info("updateDiscount({})", discount);
        if(discount.getDiscountID()==null) throw new NotFoundException("Discount ID not found");
        
        DiscountType discountType = discountTypeRepository.findByDiscountType(discount.getDiscountType())
            .orElseThrow(()-> new NotFoundException("Discount type "+discount.getDiscountType()+" not found"));

        if(discountType.getDiscountType().equalsIgnoreCase(DiscountTypeConst.Percentage_based.getValue())){
            if(discount.getDiscountCode()==null) 
                throw new InvalidDiscountException("Missing discount code for Code Discount");
        }else {
            discount.setDiscountCode(UUID.randomUUID().toString());
        }

        if(discount.getDiscountPercent()==null) 
            throw new InvalidDiscountException("Missing Discount Percentage");
        if(discount.getStartDate()==null || discount.getEndDate()==null) 
            throw new InvalidDiscountException("Missing Discount start date or end date");
        if(discount.getStartDate().isAfter(discount.getEndDate()))
            throw new InvalidDiscountException("Invalid Discount start date and end date");

        Discount fetchedDiscount = getDiscount(discount.getDiscountID());
        // update discount infomation
        Boolean check = discountDetailRepository.updateDiscount(
            fetchedDiscount.getDiscountID(), discount.getDiscountPercent(), 
            discount.getDiscountCode(), discount.getStartDate(), 
            discount.getEndDate(), discount.getActive(), 
            discount.getDiscountType());
        return check;
    }


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
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return false;
        }
    }


    // validate discount for percenage-based discount
    private boolean validatePercenageBasedDiscount(Integer productID, String discountID, LocalDateTime appliedDate)
        throws NoSuchElementException
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
            }).orElseThrow(() -> new NoSuchElementException("Discount " +discountID+" does not found"));
    }
    // validate discount for code discount
    private boolean validateCodeDiscount(Integer productID, String discountID, LocalDateTime appliedDate){
        log.info("validateCodeDiscount(productID={}, discountID={}, appliedDate={})", productID, discountID, appliedDate);
        return false;
    }

    @Override
    public Discount getDiscount(String discountID) throws InvalidDiscountException {
        log.info("getDiscount(discountID={})", discountID);
        return discountRepository.findById(discountID)
            .orElseThrow(()-> new NotFoundException("Discount "+ discountID+" does not found"));
    }
    @Override
    public Page<Discount> getDiscounts(int pageNumber, int pageSize) {
        log.info("getDiscounts(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return discountRepository.findAll(page);
    }

    @Override
    public Boolean updateDiscountStatus(Discount discount) throws InvalidDiscountException {
        log.info("updateDiscountStatus(discount={})", discount.getDiscountID());
        if(discount.getDiscountID()==null) throw new NotFoundException("Missing DiscountID");
        Discount fetchedDiscount = this.getDiscount(discount.getDiscountID());
        // update status of discount
        return discountDetailRepository.updateDiscountStatus(fetchedDiscount.getDiscountID(), discount.getActive());
    }
    @Override
    public Boolean validateDiscount(Integer productID, String discountID, LocalDateTime appliedDate) throws InvalidDiscountException {
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
    public Page<DiscountType> getDiscountTypes(int pageNumber, int pageSize) {
        log.info("getDiscountTypes(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return  discountTypeRepository.findAll(page);
    }
    @Override
    public Page<DiscountDetail> getDiscountsByProduct(int productID, int pageNumber, int pageSize) {
        log.info("getDiscountsByProduct(productID={}, pageNumber={}, pageSize={})", productID, pageNumber, pageSize);
        Product product = productRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<DiscountDetail> discounts = discountDetailRepository.findByProductID(product.getProductID(), pageable);
        return discounts;
    }
    @Override
    public DiscountDetail getDiscountDetail(String discountID){
        log.info("getDiscountDetail(discountID={})", discountID);
        DiscountDetail discount = discountDetailRepository.findById(discountID)
            .orElseThrow(()-> new NotFoundException("Discount " + discountID + " does not found"));
        return discount;
    }
    @Override
    public DiscountType getDiscountType(int discountTypeID) {
        log.info("getDiscountType(discountTypeID={})", discountTypeID);
        return discountTypeRepository.findById(discountTypeID)
            .orElseThrow(() -> new NotFoundException("Discount type " + discountTypeID + " does not found"));
    }
    @Override
    public List<DiscountDetail> getDiscountDetails(List<String> discountIDs) {
        log.info("getDiscountDetails(discountIDs={})", discountIDs);
        return discountDetailRepository.findAllById(discountIDs);
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
}