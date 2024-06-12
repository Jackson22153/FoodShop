package com.phucx.shop.messageQueueListener;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.shop.config.MessageQueueConfig;
import com.phucx.shop.constant.EventType;
import com.phucx.shop.model.EventMessage;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ProductDTO;
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
        String eventID = UUID.randomUUID().toString();
        EventMessage<Object> responseMessage = new EventMessage<>();
        responseMessage.setEventId(eventID);
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
                List<ProductStockTableType> productStocks = payload.getProductStocks();
                Boolean status = productService.updateProductsInStock(productStocks);
                responseMessage.setEventType(EventType.ReturnUpdateProductsUnitsInStock);
                responseMessage.setPayload(new ResponseFormat(status));
            }
            return objectMapper.writeValueAsString(responseMessage);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }
    }
}
