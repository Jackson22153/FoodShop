package com.phucx.order.service.discount;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.order.constant.EventType;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.model.DataDTO;
import com.phucx.order.model.DiscountDetail;
import com.phucx.order.model.DiscountDTO;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.ProductDiscountsDTO;
import com.phucx.order.model.ResponseFormat;
import com.phucx.order.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DiscountServiceImp implements DiscountService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public DiscountDetail getDiscount(String discountID) throws JsonProcessingException {
        log.info("getDiscount(discountID={})", discountID);
        // create a request for discount
        DiscountDTO discountDDiscountDTO = new DiscountDTO();
        discountDDiscountDTO.setDiscountID(discountID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetDiscountByID);
        eventMessage.setPayload(discountDDiscountDTO);
        // receive data
        EventMessage<DiscountDetail> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.DISCOUNT_QUEUE, 
            MessageQueueConstant.DISCOUNT_ROUTING_KEY,
            DiscountDetail.class);
        log.info("response={}", response);
        return response.getPayload();
    }

    @Override
    public List<DiscountDetail> getDiscounts(List<String> discountIDs) throws JsonProcessingException {
        log.info("getDiscount(getDiscounts={})", discountIDs);
        // create a request for discount
        DiscountDTO discountDDiscountDTO = new DiscountDTO();
        discountDDiscountDTO.setDiscountIDs(discountIDs);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetDiscountsByIDs);
        eventMessage.setPayload(discountDDiscountDTO);
        // receive data
        TypeReference<List<DiscountDetail>> typeReference = 
            new TypeReference<List<DiscountDetail>>() {};
        EventMessage<List<DiscountDetail>> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.DISCOUNT_QUEUE, 
            MessageQueueConstant.DISCOUNT_ROUTING_KEY, typeReference);
        log.info("response={}", response);
        return response.getPayload();
    }

    @Override
    public Boolean validateDiscount(List<ProductDiscountsDTO> productsDiscounts) throws JsonProcessingException {
        log.info("validateDiscount(productDiscounts={})", productsDiscounts);
        // create a request for discount
        DiscountDTO discountDDiscountDTO = new DiscountDTO();
        discountDDiscountDTO.setProductsDiscounts(productsDiscounts);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.ValidateDiscounts);
        eventMessage.setPayload(discountDDiscountDTO);
        // receive data
        EventMessage<ResponseFormat> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.DISCOUNT_QUEUE, 
            MessageQueueConstant.DISCOUNT_ROUTING_KEY, 
            ResponseFormat.class);
        log.info("response={}", response);
        return  response.getPayload().getStatus();
    }

    // @Transactional
    // public Discount insertDiscount(DiscountWithProduct discount) throws InvalidDiscountException, RuntimeException{
    //     String newDiscountID = UUID.randomUUID().toString();
    //     logger.info("create new discount {}, id: {}", discount.toString(), newDiscountID);
    //     Integer productID = discount.getProductID();
    //     if(productID==null) throw new RuntimeException("Missing ProductID");

    //     // get discountType
    //     DiscountType discountType = discountTypeRepository.findByDiscountType(discount.getDiscountType())
    //         .orElseThrow(()-> new InvalidDiscountException("Discount type not found"));
    //     if(discountType.getDiscountType().equalsIgnoreCase(DiscountTypeConst.Percentage_based.getValue())){
    //         if(discount.getDiscountCode()==null) 
    //             throw new InvalidDiscountException("Missing discount code for Code Discount");
    //     }else {
    //         discount.setDiscountCode(UUID.randomUUID().toString());
    //     }

    //     if(discount.getDiscountPercent()==null) 
    //         throw new InvalidDiscountException("Missing Discount Percentage");
    //     if(discount.getStartDate()==null || discount.getEndDate()==null) 
    //         throw new InvalidDiscountException("Missing Discount Start date or End date");
    //     if(discount.getStartDate().isAfter(discount.getEndDate()))
    //         throw new InvalidDiscountException("Invalid Discount Start Date and End Date");



    //     // create new discount
    //     // Discount newDiscount = new Discount(newDiscountID, discount.getDiscountPercent(), 
    //     //     discountType, discount.getDiscountCode(), discount.getStartDate(),
    //     //      discount.getEndDate(), DiscountActive.DEACTIVE.getValue());
    //     // get product
    //     Product product = productRepository.findById(productID)
    //         .orElseThrow(()-> new NotFoundException("Product not found"));
    //     // save discount along with product
    //     Boolean check = discountDetailRepository.insertDiscount(
    //         newDiscountID, discount.getDiscountPercent(), 
    //         discount.getDiscountCode(), discount.getStartDate(), 
    //         discount.getEndDate(), discount.getActive(), 
    //         discount.getDiscountType(), product.getProductID());
    //     if(check) {
    //         return discountRepository.findById(newDiscountID)
    //             .orElseThrow(()-> new NotFoundException("Discount " + newDiscountID + " does not found"));
    //     }
    //     throw new RuntimeException("Discount " + newDiscountID + " can not be saved");
    // }
    // @Override
    // public Boolean updateDiscount(DiscountWithProduct discount) throws InvalidDiscountException {
    //     if(discount.getDiscountID()==null) throw new NotFoundException("Discount ID not found");
        
    //     DiscountType discountType = discountTypeRepository.findByDiscountType(discount.getDiscountType())
    //         .orElseThrow(()-> new InvalidDiscountException("Discount type not found"));
    //     logger.info("DiscountType={}", discountType.toString());

    //     if(discountType.getDiscountType().equalsIgnoreCase(DiscountTypeConst.Percentage_based.getValue())){
    //         if(discount.getDiscountCode()==null) 
    //             throw new InvalidDiscountException("Missing discount code for Code Discount");
    //     }else {
    //         discount.setDiscountCode(UUID.randomUUID().toString());
    //     }

    //     if(discount.getDiscountPercent()==null) 
    //         throw new InvalidDiscountException("Missing Discount Percentage");
    //     if(discount.getStartDate()==null || discount.getEndDate()==null) 
    //         throw new InvalidDiscountException("Missing Discount Start date or End date");
    //     if(discount.getStartDate().isAfter(discount.getEndDate()))
    //         throw new InvalidDiscountException("Invalid Discount Start Date and End Date");

    //     Discount fetchedDiscount = getDiscount(discount.getDiscountID());


    //     if(fetchedDiscount!=null){
    //         Boolean check = discountDetailRepository.updateDiscount(
    //             discount.getDiscountID(), discount.getDiscountPercent(), 
    //             discount.getDiscountCode(), discount.getStartDate(), 
    //             discount.getEndDate(), discount.getActive(), 
    //             discount.getDiscountType());
    //         return check;
    //     }
    //     throw new InvalidDiscountException("Discount not found");
    // }


    // @Override
    // public Boolean validateDiscountsOfProduct(OrderItem product) throws InvalidDiscountException {
    //     logger.info("validateDiscountsOfProduct({})", product);
    //     try {
    //         Integer productID = product.getProductID();
    //         for(OrderItemDiscount discount: product.getDiscounts()){
    //             // validate number of discount type of a product

    //             boolean isValid = false;
    //             // validate applied date
    //             LocalDateTime currentDateTime = discount.getAppliedDate();
    //             if(currentDateTime==null) {
    //                 currentDateTime = LocalDateTime.now();
    //                 discount.setAppliedDate(currentDateTime);
    //             }
    //             // validate discount according to discount type
    //             isValid = this.validateDiscount(productID, discount);
    //             if(!isValid) {
    //                 return false;
    //             }
    //         }
    //         return true;
    //     } catch (NoSuchElementException e) {
    //         logger.error(e.getMessage());
    //         return false;
    //     }
    // }


    // // validate discount for percenage-based discount
    // private boolean validatePercenageBasedDiscount(Integer productID, OrderItemDiscount itemDiscount)
    //     throws NoSuchElementException
    // {
    //     logger.info("validatePercenageBasedDiscount(productID={}, itemDiscount={})", productID, itemDiscount.getDiscountID());
    //     return discountRepository.findByDiscountIDAndProductID(itemDiscount.getDiscountID(), productID)
    //         .map(discount -> {
    //             boolean isValid = false;
    //             Boolean isActive = discount.getActive();
    //             LocalDateTime currentDateTime = itemDiscount.getAppliedDate();
    //             if((currentDateTime.isEqual(discount.getStartDate()) || currentDateTime.isAfter(discount.getStartDate()))&&
    //                 (currentDateTime.isEqual(discount.getEndDate()) || currentDateTime.isBefore(discount.getEndDate()))){
    //                     isValid = true;
    //             }
    //             if(!isValid)
    //                 logger.info("Discount {} is out of date", discount.getDiscountID());
    //             if(!isActive)
    //                 logger.info("Discount {} is not available", discount.getDiscountID());
    //             return isActive && isValid;
    //         }).orElseThrow(() -> new NoSuchElementException("Discount " +itemDiscount.getDiscountID()+" not found"));
    // }

    // private boolean validateCodeDiscount(Integer productID, OrderItemDiscount itemDiscount){
    //     logger.info("validateCodeDiscount(productID={}, itemDiscount={})", productID, itemDiscount.getDiscountID());
    //     return true;
    // }

    // @Override
    // public Discount getDiscount(String discountID) throws InvalidDiscountException {
    //     logger.info("getDiscount(discountID={})", discountID);
    //     if(discountID==null) throw new InvalidDiscountException("DiscountID is null");
    //     return discountRepository.findById(discountID)
    //         .orElseThrow(()-> new InvalidDiscountException("Discount does not found"));
    // }
    // @Override
    // public Page<Discount> getDiscounts(int pageNumber, int pageSize) {
    //     Pageable page = PageRequest.of(pageNumber, pageSize);
    //     return discountRepository.findAll(page);
    // }

    // @Override
    // public Boolean updateDiscountStatus(Discount discount) throws InvalidDiscountException {
    //     logger.info("updateDiscountStatus(discount={})", discount.getDiscountID());
    //     if(discount.getDiscountID()==null) throw new InvalidDiscountException("Missing DiscountID");
    //     Discount fetchedDiscount = this.getDiscount(discount.getDiscountID());
    //     if(fetchedDiscount!=null){
    //         return discountDetailRepository.updateDiscountStatus(fetchedDiscount.getDiscountID(), discount.getActive());
    //     }
    //     throw new InvalidDiscountException("Discount "+discount.getDiscountID()+" is not valid");
    // }
    // @Override
    // public Boolean validateDiscount(Integer productID, OrderItemDiscount orderDiscount) throws InvalidDiscountException {
    //     logger.info("validateDiscount(productID={}, OrderItemDiscount={})", 
    //         productID, orderDiscount.getDiscountID());
    //     // get discountType
    //     Discount discount = this.getDiscount(orderDiscount.getDiscountID());
    //     String discountType = discount.getDiscountType().getDiscountType();

    //     logger.info("Discount: ", discountType);
    //     // validate according to discount's type
    //     if(DiscountTypeConst.Percentage_based.getValue().equalsIgnoreCase(discountType)){
    //         return this.validatePercenageBasedDiscount(productID, orderDiscount);
    //     }else if(DiscountTypeConst.Code.getValue().equalsIgnoreCase(discountType)){
    //         return this.validateCodeDiscount(productID, orderDiscount);
    //     }else throw new InvalidDiscountException("Invalid Discount Type: {}" + discountType);
    // }
    // @Override
    // public Page<DiscountType> getDiscountTypes(int pageNumber, int pageSize) {
    //     Pageable page = PageRequest.of(pageNumber, pageSize);
    //     return  discountTypeRepository.findAll(page);
    // }
    // @Override
    // public Page<DiscountDetail> getDiscountsByProduct(int productID, int pageNumber, int pageSize) {
    //     Product product = productRepository.findById(productID)
    //         .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));

    //     Pageable pageable = PageRequest.of(pageNumber, pageSize);
    //     Page<DiscountDetail> discounts = discountDetailRepository.findByProductID(product.getProductID(), pageable);
    //     return discounts;
    // }
    // @Override
    // public DiscountDetail getDiscountDetail(String discountID){
    //     DiscountDetail discount = discountDetailRepository.findById(discountID)
    //         .orElseThrow(()-> new NotFoundException("Discount " + discountID + " does not found"));
    //     return discount;
    // }
}
