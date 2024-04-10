package com.phucx.account.service.discounts;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.account.constant.DiscountActive;
import com.phucx.account.constant.DiscountType;
import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.model.Discount;
import com.phucx.account.model.DiscountWithProduct;
import com.phucx.account.model.OrderItem;
import com.phucx.account.model.OrderItemDiscount;
import com.phucx.account.model.Products;
import com.phucx.account.model.ProductsDiscounts;
import com.phucx.account.repository.DiscountRepository;
import com.phucx.account.repository.ProductsDiscountsRepository;
import com.phucx.account.repository.ProductsRepository;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@Service
public class DiscountServiceImp implements DiscountService{
    private Logger logger = LoggerFactory.getLogger(DiscountServiceImp.class);
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private ProductsDiscountsRepository productsDiscountsRepository;

    @Transactional
    public Discount insertDiscount(DiscountWithProduct discount) throws InvalidDiscountException, RuntimeException{
        String newDiscountID = UUID.randomUUID().toString();
        logger.info("create new discount {}, id: {}", discount.toString(), newDiscountID);
        Integer productID = discount.getProductID();
        if(productID==null) throw new RuntimeException("Missing ProductID");
        if(DiscountType.Percentage_based.equals(discount.getDiscountType())){
            discount.setDiscountCode(getDiscountID());
        }else if(DiscountType.Code.equals(discount.getDiscountType())){
            if(discount.getDiscountCode()==null) 
                throw new InvalidDiscountException("Missing Discount Code");
        }else {
            throw new InvalidDiscountException("Invalid Discount Type");
        }
        if(discount.getDiscountPercent()==null) 
            throw new InvalidDiscountException("Missing Discount Percentage");
        if(discount.getStartDate()==null || discount.getEndDate()==null) 
            throw new InvalidDiscountException("Missing Discount Start date or End date");
        if(discount.getStartDate().isAfter(discount.getEndDate()))
            throw new InvalidDiscountException("Invalid Discount Start Date and End Date");
        // create new discount
        Discount newDiscount = new Discount(newDiscountID, discount.getDiscountPercent(), 
            discount.getDiscountType(), discount.getDiscountCode(), discount.getStartDate(),
             discount.getEndDate(), DiscountActive.DEACTIVE.getValue());
        // get product
        Products product = productsRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product not found"));
        // save discount along with product
        Discount savedDiscount = discountRepository.save(newDiscount);
        ProductsDiscounts productsDiscount = new ProductsDiscounts(product, savedDiscount);
        productsDiscountsRepository.save(productsDiscount);

        return savedDiscount;
        
    }
    @Override
    public Boolean updateDiscount(Discount discount) throws InvalidDiscountException {
        if(discount.getDiscountID()==null) 
            throw new NotFoundException("Discount ID not found");
        if(DiscountType.Percentage_based.equals(discount.getDiscountType())){
            discount.setDiscountCode(getDiscountID());
        }else if(DiscountType.Code.equals(discount.getDiscountType())){
            if(discount.getDiscountCode()==null) 
                throw new InvalidDiscountException("Missing Discount Code");
        }else {
            throw new InvalidDiscountException("Invalid Discount Type");
        }
        if(discount.getDiscountPercent()==null) 
            throw new InvalidDiscountException("Missing Discount Percentage");
        if(discount.getStartDate()==null || discount.getEndDate()==null) 
            throw new InvalidDiscountException("Missing Discount Start date or End date");
        if(discount.getStartDate().isAfter(discount.getEndDate()))
            throw new InvalidDiscountException("Invalid Discount Start Date and End Date");
        Discount fetchedDiscount = getDiscount(discount.getDiscountID());
        if(fetchedDiscount!=null){
            Integer check = discountRepository.updateDiscount(
                discount.getDiscountID(), discount.getDiscountPercent(),
                discount.getDiscountType(), discount.getDiscountCode(), 
                discount.getStartDate(), discount.getEndDate());
            if(check>0) return true;
            else return false;
        }
        throw new NotFoundException("Discount not found");
    }
    @Override
    public Boolean validateDiscount(OrderItem item){
        try {
            Integer productID = item.getProductID();
            for(OrderItemDiscount discount: item.getDiscounts()){
                boolean isValid = false;
                if(discount.getDiscountType().getValue().equalsIgnoreCase(DiscountType.Percentage_based.getValue())){
                    isValid =this.validatePercenageBasedDiscount(productID, discount);
                }else if(discount.getDiscountType().getValue().equalsIgnoreCase(DiscountType.Code.getValue())){
                    isValid = this.validateCodeDiscount(productID, discount);
                }
                if(!isValid) {
                    return false;
                }
            }
            return true;
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    // validate discount for percenage-based discount
    private boolean validatePercenageBasedDiscount(Integer productID, OrderItemDiscount itemDiscount)
        throws NoSuchElementException
    {
        return discountRepository.findByDiscountIDAndProductID(itemDiscount.getDiscountID(), productID)
            .map(discount -> {
                boolean isValid = false;
                Boolean isActive = discount.getActive();
                LocalDateTime currentDateTime = itemDiscount.getAppliedDate();
                if((currentDateTime.isEqual(discount.getStartDate()) || currentDateTime.isAfter(discount.getStartDate()))&&
                    (currentDateTime.isEqual(discount.getEndDate()) || currentDateTime.isBefore(discount.getEndDate()))){
                        isValid = true;
                }
                return isActive && isValid;
            }).orElseThrow(() -> new NoSuchElementException("Discount " +itemDiscount.getDiscountID()+" not found"));
    }

    private boolean validateCodeDiscount(Integer productID, OrderItemDiscount itemDiscount){
        return false;
    }

    @Override
    public Discount getDiscount(String discountID) throws NoSuchElementException, NullPointerException {
        if(discountID==null) throw new NullPointerException("DiscountID is null");
        return discountRepository.findById(discountID)
            .orElseThrow(()-> new NoSuchElementException("Discount does not found"));
    }
    @Override
    public Page<Discount> getDiscounts(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return discountRepository.findAll(page);
    }

    private String getDiscountID(){
        return UUID.randomUUID().toString();
    }
    @Override
    public Boolean updateDiscountStatus(Discount discount) throws InvalidDiscountException {
        if(discount.getDiscountID()==null) throw new InvalidDiscountException("Missing DiscountID");
        Discount fetchedDiscount = this.getDiscount(discount.getDiscountID());
        if(fetchedDiscount!=null){
            Integer check =discountRepository.updateDiscountStatus(
                fetchedDiscount.getDiscountID(), discount.getActive());
            if(check>0) return true;
            return false;
        }
        throw new InvalidDiscountException("Discount "+discount.getDiscountID()+" is not valid");
    }
}
