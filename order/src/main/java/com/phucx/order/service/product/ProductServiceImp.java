package com.phucx.order.service.product;

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
import com.phucx.model.ProductDiscountsDTO;
import com.phucx.model.ProductStockTableType;
import com.phucx.order.constant.MessageQueueConstant;
import com.phucx.order.exception.NotFoundException;
import com.phucx.order.model.Product;
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
    public ResponseFormat validateAndProcessProducts(List<ProductDiscountsDTO> products) throws JsonProcessingException {
        log.info("validateAndProcessProducts({})", products);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProducts(products);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.ValidateAndProcessProducts);
        eventMessage.setPayload(productDTO);
        // receive data
        EventMessage<ResponseFormat> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.SHOP_EXCHANGE, 
            MessageQueueConstant.PRODUCT_ROUTING_KEY,
            ResponseFormat.class);
        log.info("response={}", response);
        return response.getPayload();
    }
    @Override
    public Boolean updateProductsInStocks(List<ProductStockTableType> productStocks) throws JsonProcessingException {
        log.info("updateProductsInStocks({})", productStocks);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductStocks(productStocks);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.UpdateProductsUnitsInStock);
        eventMessage.setPayload(productDTO);
        // receive data
        EventMessage<ResponseFormat> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.SHOP_EXCHANGE, 
            MessageQueueConstant.PRODUCT_ROUTING_KEY,
            ResponseFormat.class);
        log.info("response={}", response);
        return response.getPayload().getStatus();
    }
    @Override
    public ResponseFormat validateProducts(List<ProductDiscountsDTO> products) throws JsonProcessingException {
        log.info("validateProducts({})", products);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProducts(products);
        // create a request message
        String eventID = UUID.randomUUID().toString();
        EventMessage<DataDTO> eventMessage = new EventMessage<>();
        eventMessage.setEventId(eventID);
        eventMessage.setEventType(EventType.ValidateProducts);
        eventMessage.setPayload(productDTO);
        // receive data
        EventMessage<ResponseFormat> response = messageQueueService.sendAndReceiveData(
            eventMessage, MessageQueueConstant.SHOP_EXCHANGE, 
            MessageQueueConstant.PRODUCT_ROUTING_KEY,
            ResponseFormat.class);
        log.info("response={}", response);
        return response.getPayload();
    }
}
