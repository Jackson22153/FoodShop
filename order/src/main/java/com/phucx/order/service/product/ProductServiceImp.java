package com.phucx.order.service.product;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phucx.order.constant.EventType;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.model.DataRequest;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.Product;
import com.phucx.order.model.ProductRequest;
import com.phucx.order.model.ResponseFormat;
import com.phucx.order.service.messageQueue.MessageQueueService;

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
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductIds(productIDs);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetProductsByIDs);
        eventMessage.setPayload(productRequest);
        // receive data
        TypeReference<List<Product>> typeReference = new TypeReference<List<Product>>() {};
        EventMessage<List<Product>> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.PRODUCT_QUEUE, 
            MessageQueueConstant.PRODUCT_ROUTING_KEY,
            typeReference);
        log.info("response={}", response);
        return response.getPayload();
    }
    @Override
    public Product getProduct(int productID) throws JsonProcessingException {
        log.info("getProduct(productID={})", productID);
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductID(productID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetProductByID);
        eventMessage.setPayload(productRequest);
        // receive data
        EventMessage<Product> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.PRODUCT_QUEUE, 
            MessageQueueConstant.PRODUCT_ROUTING_KEY,
            Product.class);
        log.info("response={}", response);
        return response.getPayload();
    }
    @Override
    public Boolean updateProductInStocks(List<Product> products) throws JsonProcessingException {
        log.info("updateProductInStocks(products={})", products);
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProducts(products);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetProductByID);
        eventMessage.setPayload(productRequest);
        // receive data
        EventMessage<ResponseFormat> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.PRODUCT_QUEUE, 
            MessageQueueConstant.PRODUCT_ROUTING_KEY,
            ResponseFormat.class);
        log.info("response={}", response);
        return response.getPayload().getStatus();
    }

    // @Override
    // public Boolean updateProductInStocks(int productID, int value) {
    //     Product product = this.getProduct(productID);
    //     Integer check = productRepository.updateProductInStocks(product.getProductID(), value);
    //     if(check>0) return true;
    //     return false;
    // }

    // @Override
    // public List<Product> getProducts(List<Integer> productIDs) {
    //     return productRepository.findAllById(productIDs);
    // }
    
}
