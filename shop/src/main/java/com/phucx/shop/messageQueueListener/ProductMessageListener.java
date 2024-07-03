package com.phucx.shop.messageQueueListener;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.shop.config.MessageQueueConfig;
import com.phucx.shop.constant.EventType;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.EventMessage;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ProductDTO;
import com.phucx.shop.model.ProductDiscountsDTO;
import com.phucx.shop.model.ProductStockTableType;
import com.phucx.shop.model.ResponseFormat;
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
    
    // get product
    @RabbitHandler
    public String fetchProduct(String message){
        log.info("fetchProduct({})", message);
        EventMessage<Object> responseMessage = this.createResponseMessage(Object.class);
        try {
            TypeReference<EventMessage<ProductDTO>> typeRef = new TypeReference<EventMessage<ProductDTO>>() {};
            EventMessage<ProductDTO> eventMessage = objectMapper.readValue(message, typeRef);
            ProductDTO payload = eventMessage.getPayload();
            // fetch data
            if(eventMessage.getEventType().equals(EventType.GetProductByID)){
                // get product by id
                Product product = productService.getProduct(payload.getProductID());
                // set response message
                responseMessage.setEventType(EventType.ReturnProductByID);
                responseMessage.setPayload(product);
            }else if(eventMessage.getEventType().equals(EventType.GetProductsByIDs)){
                // get products by ids
                List<Integer> productIds = payload.getProductIds();
                List<Product> product = productService.getProducts(productIds);
                // set response message
                responseMessage.setEventType(EventType.ReturnProductsByIDs);
                responseMessage.setPayload(product);
            }else if(eventMessage.getEventType().equals(EventType.UpdateProductsUnitsInStock)){
                // update product instocks
                List<ProductStockTableType> productStocks = payload.getProductStocks();
                Boolean status = productService.updateProductInStock(productStocks);
                responseMessage.setEventType(EventType.ReturnUpdateProductsUnitsInStock);
                responseMessage.setPayload(new ResponseFormat(status));
            }else if(eventMessage.getEventType().equals(EventType.ValidateProducts)){
                // validate products
                List<ProductDiscountsDTO> products = payload.getProducts();
                ResponseFormat responseFormat = productService.validateProducts(products);
                responseMessage.setEventType(EventType.ReturnValidateProducts);
                responseMessage.setPayload(responseFormat);
            }
            return objectMapper.writeValueAsString(responseMessage);
        } catch (JsonProcessingException e) {
            log.error("Error: {}", e.getMessage());
            return null;
        } catch (NotFoundException e){
            log.error("Error: {}", e.getMessage());
            try {
                responseMessage.setErrorMessage(e.getMessage());
                responseMessage.setEventType(EventType.NotFoundException);
                String responsemessage = objectMapper.writeValueAsString(responseMessage);
                return responsemessage;
            } catch (JsonProcessingException exception) {
                log.error("Error: {}", e.getMessage());
                return null;
            }
        }
    }

    private <T> EventMessage<T> createResponseMessage(Class<T> type){
        EventMessage<T> responseMessage = new EventMessage<>();
        String eventID = UUID.randomUUID().toString();
        responseMessage.setEventId(eventID);
        return responseMessage;
    }
}
