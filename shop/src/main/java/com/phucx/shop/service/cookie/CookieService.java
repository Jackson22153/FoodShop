package com.phucx.shop.service.cookie;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.phucx.shop.model.OrderItem;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;

public interface CookieService {
    public void removeProduct(Integer productID, String cartJson, HttpServletResponse response) 
        throws JsonMappingException, JsonProcessingException;
    public List<OrderItem> getListProduct(String cartJson) 
        throws JsonMappingException, JsonProcessingException, NotFoundException;
    public void updateCookie(String cartJson, OrderItem orderProduct, HttpServletResponse response) 
        throws JsonMappingException, JsonProcessingException, NotFoundException;
}
