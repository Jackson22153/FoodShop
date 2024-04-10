package com.phucx.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.phucx.shop.constant.CookieConstant;
import com.phucx.shop.model.OrderItem;
import com.phucx.shop.service.cookie.CookieService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CookieService cookieService;
    @PostMapping
    public ResponseEntity<Void> updateCookie(HttpServletResponse response,
        @CookieValue(CookieConstant.CART_COOKIE) String cartJson,
        @RequestBody OrderItem OrderItem
    ) throws JsonMappingException, NotFoundException, JsonProcessingException {
        cookieService.updateCookie(cartJson, OrderItem, response);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productID}")
    public ResponseEntity<Void> deleteCookie(HttpServletResponse response, 
        @CookieValue(CookieConstant.CART_COOKIE) String cartJson,
        @RequestParam("productID") Integer productID
    ) throws JsonMappingException, JsonProcessingException{
        cookieService.removeProduct(productID, cartJson, response);
        return ResponseEntity.ok().build(); 
    }

    @GetMapping("/products")
    public ResponseEntity<List<OrderItem>> getOrderItems(
        @CookieValue(CookieConstant.CART_COOKIE) String cartJson
    ) throws JsonMappingException, NotFoundException, JsonProcessingException{
        List<OrderItem> orderItems = cookieService.getListProduct(cartJson);
        return ResponseEntity.ok().body(orderItems);
    }
}
