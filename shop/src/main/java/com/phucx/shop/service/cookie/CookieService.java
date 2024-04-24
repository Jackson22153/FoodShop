package com.phucx.shop.service.cookie;

import java.util.List;

import javax.naming.InsufficientResourcesException;

import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.phucx.shop.model.CartOrderItem;
import com.phucx.shop.model.OrderWithProducts;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;

public interface CookieService {
    public void removeProduct(Integer productID, String encodedCartJson, HttpServletResponse response) 
        throws JsonMappingException, JsonProcessingException;
    public OrderWithProducts getOrder(String encodedCartJson, Authentication authentication) 
        throws JsonMappingException, JsonProcessingException, NotFoundException;
    public void updateCookie(String encodedCartJson, CartOrderItem orderProduct, HttpServletResponse response) 
        throws JsonMappingException, JsonProcessingException, NotFoundException, InsufficientResourcesException;
    public List<CartOrderItem> getListProducts(String encodedCartJson)    
        throws JsonMappingException, JsonProcessingException;
}
