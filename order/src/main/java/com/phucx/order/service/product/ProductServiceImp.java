package com.phucx.order.service.product;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.order.constant.EventType;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.model.EventMessage;
import com.phucx.order.model.Product;
import com.phucx.order.model.ProductRequest;
import com.phucx.order.service.messageQueue.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    @SuppressWarnings("unchecked")
    public List<Product> getProducts(List<Integer> productIDs) {
        log.info("getProducts(productIDs={})", productIDs);
        // create a request for user
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductIds(productIDs);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<ProductRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetProductsByIDs);
        eventMessage.setPayload(productRequest);
        // receive data
        EventMessage<Object> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.PRODUCT_QUEUE, 
            MessageQueueConstant.PRODUCT_ROUTING_KEY);
        log.info("response={}", response);
        return (List<Product>) response.getPayload();
    }
    @Override
    public Product getProduct(int productID) {
        log.info("getProduct(productID={})", productID);
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductID(productID);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<ProductRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetProductByID);
        eventMessage.setPayload(productRequest);
        // receive data
        EventMessage<Object> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.PRODUCT_QUEUE, 
            MessageQueueConstant.PRODUCT_ROUTING_KEY);
        log.info("response={}", response);
        return  (Product) response.getPayload();
    }
    @Override
    public Boolean updateProductInStocks(List<Product> products) {
        log.info("updateProductInStocks(products={})", products);
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProducts(products);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<ProductRequest> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.GetProductByID);
        eventMessage.setPayload(productRequest);
        // receive data
        EventMessage<Object> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.PRODUCT_QUEUE, 
            MessageQueueConstant.PRODUCT_ROUTING_KEY);
        log.info("response={}", response);
        return (Boolean) response.getPayload();
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
