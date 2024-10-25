package com.phucx.payment.service.product;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.constant.EventType;
import com.phucx.model.DataDTO;
import com.phucx.model.EventMessage;
import com.phucx.model.ProductDTO;
import com.phucx.model.ShippingProductDTO;
import com.phucx.payment.constant.MessageQueueConstant;
import com.phucx.payment.exception.NotFoundException;
import com.phucx.payment.model.Product;
import com.phucx.payment.model.ShippingProduct;
import com.phucx.payment.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public List<Product> getProducts(List<Integer> productIDs) throws JsonProcessingException {
        log.info("getProducts(productIDs={})", productIDs);
        // create a request for user
        ProductDTO productDProductDTO = new ProductDTO();
        productDProductDTO.setProductIds(productIDs);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetProductsByIDs);
        eventMessage.setPayload(productDProductDTO);
        // receive data
        TypeReference<EventMessage<List<Product>>> typeReference = new TypeReference<EventMessage<List<Product>>>() {};
        EventMessage<List<Product>> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.SHOP_EXCHANGE, 
            MessageQueueConstant.PRODUCT_ROUTING_KEY,
            typeReference);
        log.info("response={}", response);
        return response.getPayload();
    }
    @Override
    public Product getProduct(int productID) throws JsonProcessingException, NotFoundException {
        log.info("getProduct(productID={})", productID);
        ProductDTO productDProductDTO = new ProductDTO();
        productDProductDTO.setProductID(productID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetProductByID);
        eventMessage.setPayload(productDProductDTO);
        // receive data
        EventMessage<Product> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.SHOP_EXCHANGE, 
            MessageQueueConstant.PRODUCT_ROUTING_KEY,
            Product.class);
        log.info("response={}", response);
        if(response.getEventType().equals(EventType.NotFoundException)){
            throw new NotFoundException(response.getErrorMessage());
        }
        return response.getPayload();
    }

    @Override
    public ShippingProduct getShippingProduct(String encodedCartJson) throws JsonProcessingException {
        log.info("getShippingProduct(encodedCartJson={})", encodedCartJson);
        ShippingProductDTO productDTO = new ShippingProductDTO();
        productDTO.setEncodedCartJson(encodedCartJson);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetShippingProduct);
        eventMessage.setPayload(productDTO);
        // receive data
        EventMessage<ShippingProduct> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.SHOP_EXCHANGE, 
            MessageQueueConstant.PRODUCT_ROUTING_KEY,
            ShippingProduct.class);
        log.info("response={}", response);
        if(response.getEventType().equals(EventType.NotFoundException)){
            throw new NotFoundException(response.getErrorMessage());
        }
        return response.getPayload();
    }
}
