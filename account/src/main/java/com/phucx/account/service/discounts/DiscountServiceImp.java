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

import com.phucx.account.constant.DiscountTypeConst;
import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.model.Discount;
import com.phucx.account.model.DiscountDetail;
import com.phucx.account.model.DiscountType;
import com.phucx.account.model.DiscountWithProduct;
import com.phucx.account.model.OrderItem;
import com.phucx.account.model.OrderItemDiscount;
import com.phucx.account.model.Products;
import com.phucx.account.repository.DiscountDetailRepository;
import com.phucx.account.repository.DiscountRepository;
import com.phucx.account.repository.DiscountTypeRepository;
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
    private DiscountTypeRepository discountTypeRepository;
    @Autowired
    private DiscountDetailRepository discountDetailRepository;

    @Transactional
    public Discount insertDiscount(DiscountWithProduct discount) throws InvalidDiscountException, RuntimeException{
        String newDiscountID = UUID.randomUUID().toString();
        logger.info("create new discount {}, id: {}", discount.toString(), newDiscountID);
        Integer productID = discount.getProductID();
        if(productID==null) throw new RuntimeException("Missing ProductID");

        // get discountType
        DiscountType discountType = discountTypeRepository.findByDiscountType(discount.getDiscountType())
            .orElseThrow(()-> new InvalidDiscountException("Discount type not found"));
        if(discountType.getDiscountType().equalsIgnoreCase(DiscountTypeConst.Percentage_based.getValue())){
            if(discount.getDiscountCode()==null) 
                throw new InvalidDiscountException("Missing discount code for Code Discount");
        }else {
            discount.setDiscountCode(UUID.randomUUID().toString());
        }

        if(discount.getDiscountPercent()==null) 
            throw new InvalidDiscountException("Missing Discount Percentage");
        if(discount.getStartDate()==null || discount.getEndDate()==null) 
            throw new InvalidDiscountException("Missing Discount Start date or End date");
        if(discount.getStartDate().isAfter(discount.getEndDate()))
            throw new InvalidDiscountException("Invalid Discount Start Date and End Date");



        // create new discount
        // Discount newDiscount = new Discount(newDiscountID, discount.getDiscountPercent(), 
        //     discountType, discount.getDiscountCode(), discount.getStartDate(),
        //      discount.getEndDate(), DiscountActive.DEACTIVE.getValue());
        // get product
        Products product = productsRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product not found"));
        // save discount along with product
        Boolean check = discountDetailRepository.insertDiscount(
            newDiscountID, discount.getDiscountPercent(), 
            discount.getDiscountCode(), discount.getStartDate(), 
            discount.getEndDate(), discount.getActive(), 
            discount.getDiscountType(), product.getProductID());
        if(check) {
            return discountRepository.findById(newDiscountID)
                .orElseThrow(()-> new NotFoundException("Discount " + newDiscountID + " does not found"));
        }
        throw new RuntimeException("Discount " + newDiscountID + " can not be saved");
    }
    @Override
    public Boolean updateDiscount(DiscountWithProduct discount) throws InvalidDiscountException {
        if(discount.getDiscountID()==null) throw new NotFoundException("Discount ID not found");
        
        DiscountType discountType = discountTypeRepository.findByDiscountType(discount.getDiscountType())
            .orElseThrow(()-> new InvalidDiscountException("Discount type not found"));
        logger.info("DiscountType={}", discountType.toString());

        if(discountType.getDiscountType().equalsIgnoreCase(DiscountTypeConst.Percentage_based.getValue())){
            if(discount.getDiscountCode()==null) 
                throw new InvalidDiscountException("Missing discount code for Code Discount");
        }else {
            discount.setDiscountCode(UUID.randomUUID().toString());
        }

        if(discount.getDiscountPercent()==null) 
            throw new InvalidDiscountException("Missing Discount Percentage");
        if(discount.getStartDate()==null || discount.getEndDate()==null) 
            throw new InvalidDiscountException("Missing Discount Start date or End date");
        if(discount.getStartDate().isAfter(discount.getEndDate()))
            throw new InvalidDiscountException("Invalid Discount Start Date and End Date");

        Discount fetchedDiscount = getDiscount(discount.getDiscountID());


        if(fetchedDiscount!=null){
            Boolean check = discountDetailRepository.updateDiscount(
                discount.getDiscountID(), discount.getDiscountPercent(), 
                discount.getDiscountCode(), discount.getStartDate(), 
                discount.getEndDate(), discount.getActive(), 
                discount.getDiscountType());
            return check;
        }
        throw new InvalidDiscountException("Discount not found");
    }


    @Override
    public Boolean validateDiscountsOfProduct(OrderItem product) throws InvalidDiscountException {
        try {
            Integer productID = product.getProductID();
            for(OrderItemDiscount discount: product.getDiscounts()){
                // validate number of discount type of a product

                boolean isValid = false;
                // validate applied date
                LocalDateTime currentDateTime = discount.getAppliedDate();
                if(currentDateTime==null) {
                    currentDateTime = LocalDateTime.now();
                    discount.setAppliedDate(currentDateTime);
                }
                // validate discount according to discount type
                isValid = this.validateDiscount(productID, discount);
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
        logger.info("validatePercenageBasedDiscount(productID={}, itemDiscount={})", productID, itemDiscount.getDiscountID());
        return discountRepository.findByDiscountIDAndProductID(itemDiscount.getDiscountID(), productID)
            .map(discount -> {
                boolean isValid = false;
                Boolean isActive = discount.getActive();
                LocalDateTime currentDateTime = itemDiscount.getAppliedDate();
                if((currentDateTime.isEqual(discount.getStartDate()) || currentDateTime.isAfter(discount.getStartDate()))&&
                    (currentDateTime.isEqual(discount.getEndDate()) || currentDateTime.isBefore(discount.getEndDate()))){
                        isValid = true;
                }
                if(!isValid)
                    logger.info("Discount {} is out of date", discount.getDiscountID());
                if(!isActive)
                    logger.info("Discount {} is not available", discount.getDiscountID());
                return isActive && isValid;
            }).orElseThrow(() -> new NoSuchElementException("Discount " +itemDiscount.getDiscountID()+" not found"));
    }

    private boolean validateCodeDiscount(Integer productID, OrderItemDiscount itemDiscount){
        logger.info("validateCodeDiscount(productID={}, itemDiscount={})", productID, itemDiscount.getDiscountID());
        return true;
    }

    @Override
    public Discount getDiscount(String discountID) throws InvalidDiscountException {
        logger.info("getDiscount(discountID={})", discountID);
        if(discountID==null) throw new InvalidDiscountException("DiscountID is null");
        return discountRepository.findById(discountID)
            .orElseThrow(()-> new InvalidDiscountException("Discount does not found"));
    }
    @Override
    public Page<Discount> getDiscounts(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return discountRepository.findAll(page);
    }

    @Override
    public Boolean updateDiscountStatus(Discount discount) throws InvalidDiscountException {
        logger.info("updateDiscountStatus(discount={})", discount.getDiscountID());
        if(discount.getDiscountID()==null) throw new InvalidDiscountException("Missing DiscountID");
        Discount fetchedDiscount = this.getDiscount(discount.getDiscountID());
        if(fetchedDiscount!=null){
            return discountDetailRepository.updateDiscountStatus(fetchedDiscount.getDiscountID(), discount.getActive());
        }
        throw new InvalidDiscountException("Discount "+discount.getDiscountID()+" is not valid");
    }
    @Override
    public Boolean validateDiscount(Integer productID, OrderItemDiscount orderDiscount) throws InvalidDiscountException {
        logger.info("validateDiscount(productID={}, OrderItemDiscount={})", 
            productID, orderDiscount.getDiscountID());
        // get discountType
        Discount discount = this.getDiscount(orderDiscount.getDiscountID());
        String discountType = discount.getDiscountType().getDiscountType();

        logger.info("Discount: ", discountType);
        // validate according to discount's type
        if(DiscountTypeConst.Percentage_based.getValue().equalsIgnoreCase(discountType)){
            return this.validatePercenageBasedDiscount(productID, orderDiscount);
        }else if(DiscountTypeConst.Code.getValue().equalsIgnoreCase(discountType)){
            return this.validateCodeDiscount(productID, orderDiscount);
        }else throw new InvalidDiscountException("Invalid Discount Type: {}" + discountType);
    }
    @Override
    public Page<DiscountType> getDiscountTypes(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return  discountTypeRepository.findAll(page);
    }
    @Override
    public Page<DiscountDetail> getDiscountsByProduct(int productID, int pageNumber, int pageSize) {
        Products product = productsRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<DiscountDetail> discounts = discountDetailRepository.findByProductID(product.getProductID(), pageable);
        return discounts;
    }
    @Override
    public DiscountDetail getDiscountDetail(String discountID){
        DiscountDetail discount = discountDetailRepository.findById(discountID)
            .orElseThrow(()-> new NotFoundException("Discount " + discountID + " does not found"));
        return discount;
    }
}
