package com.phucx.shop.messageQueueListener;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.constant.EventType;
import com.phucx.converter.ProductDiscountsDTOConverter;
import com.phucx.converter.ProductStockTableTypeConverter;
import com.phucx.model.EventMessage;
import com.phucx.model.ProductDiscountsDTO;
import com.phucx.model.ProductStockTableType;
import com.phucx.shop.config.MessageQueueConfig;
import com.phucx.shop.exceptions.EmptyCartException;
import com.phucx.shop.exceptions.InvalidOrderException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ResponseFormat;
import com.phucx.shop.model.ShippingProduct;
import com.phucx.shop.service.cart.CartService;
import com.phucx.shop.service.product.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RabbitListener(queues = MessageQueueConfig.PRODUCT_QUEUE)
public class ProductMessageListener {
    @Autowired
    private ProductService productService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CartService cartService;
    
    // get product
    @RabbitHandler
    public String fetchProduct(String message){
        log.info("fetchProduct({})", message);
        EventMessage<Object> responseMessage = this.createResponseMessage(Object.class);
        try {
            TypeReference<EventMessage<LinkedHashMap<String, Object>>> typeRef = 
                new TypeReference<EventMessage<LinkedHashMap<String, Object>>>() {};
            EventMessage<LinkedHashMap<String, Object>> eventMessage = 
                objectMapper.readValue(message, typeRef);
            LinkedHashMap<String, Object> payload = eventMessage.getPayload();
            // fetch data
            if(eventMessage.getEventType().equals(EventType.GetProductByID)){
                Integer productID = (Integer) payload.get("productID");
                // get product by id
                Product product = productService.getProduct(productID);
                // set response message
                responseMessage.setEventType(EventType.ReturnProductByID);
                responseMessage.setPayload(product);
            }else if(eventMessage.getEventType().equals(EventType.GetProductsByIDs)){
                // get products by ids
                List<Integer> productIds = (List<Integer>) payload.get("productIds");
                List<Product> product = productService.getProducts(productIds);
                // set response message
                responseMessage.setEventType(EventType.ReturnProductsByIDs);
                responseMessage.setPayload(product);
            }else if(eventMessage.getEventType().equals(EventType.UpdateProductsUnitsInStock)){
                // update product instocks
                List<LinkedHashMap<String, Object>> productStocks = 
                    (List<LinkedHashMap<String, Object>>) payload.get("productStocks");
                List<ProductStockTableType> products = ProductStockTableTypeConverter
                    .productStockTableTypesConverter(productStocks);
                Boolean status = productService.updateProductInStock(products);
                responseMessage.setEventType(EventType.ReturnUpdateProductsUnitsInStock);
                responseMessage.setPayload(new ResponseFormat(status));
            }else if(eventMessage.getEventType().equals(EventType.ValidateAndProcessProducts)){
                // validate and process products
                List<LinkedHashMap<String, Object>> products = 
                    (List<LinkedHashMap<String, Object>>) payload.get("products");
                List<ProductDiscountsDTO> productDiscounts = ProductDiscountsDTOConverter
                    .castProductDiscountDTOs(products);
                ResponseFormat responseFormat = productService.validateAndProcessProducts(productDiscounts);
                responseMessage.setEventType(EventType.ReturnValidateAndProcessProducts);
                responseMessage.setPayload(responseFormat);
            }else if(eventMessage.getEventType().equals(EventType.ValidateProducts)){
                // validate products
                List<LinkedHashMap<String, Object>> products = 
                    (List<LinkedHashMap<String, Object>>) payload.get("products");
                List<ProductDiscountsDTO> productDiscounts = ProductDiscountsDTOConverter
                    .castProductDiscountDTOs(products);
                ResponseFormat responseFormat = productService.validateProducts(productDiscounts);
                responseMessage.setEventType(EventType.ReturnValidateProducts);
                responseMessage.setPayload(responseFormat);
            }else if(eventMessage.getEventType().equals(EventType.GetShippingProduct)){
                // get shipping products
                ShippingProduct shippingProduct = getShippingProductInfo(eventMessage.getPayload());
                responseMessage.setEventType(EventType.ReturnShippingProduct);
                responseMessage.setPayload(shippingProduct);
            }
            return objectMapper.writeValueAsString(responseMessage);
        } catch (JsonProcessingException e) {
            log.error("Error: {}", e.getMessage());
            return null;
        } catch (NotFoundException | EmptyCartException | InvalidOrderException e){
            log.error("Error: {}", e.getMessage());
            return handleNotFoundException(responseMessage, e.getMessage());
        }
    }

    private ShippingProduct getShippingProductInfo(LinkedHashMap<String, Object> productDTO) 
    throws JsonProcessingException, EmptyCartException, InvalidOrderException, NotFoundException{
        String cartJson = productDTO.get("encodedCartJson").toString();
        return cartService.getShippingProduct(cartJson);  
    }

    // handle not found exception
    private String handleNotFoundException(EventMessage<Object> responseMessage, String errorMessage){
        try {
            responseMessage.setErrorMessage(errorMessage);
            responseMessage.setEventType(EventType.NotFoundException);
            String responsemessage = objectMapper.writeValueAsString(responseMessage);
            return responsemessage;
        } catch (Exception exception) {
            log.error("Error: {}", exception.getMessage());
            return null;
        }
    }

    private <T> EventMessage<T> createResponseMessage(Class<T> type){
        EventMessage<T> responseMessage = new EventMessage<>();
        String eventID = UUID.randomUUID().toString();
        responseMessage.setEventId(eventID);
        return responseMessage;
    }
}
