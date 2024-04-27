package com.phucx.shop.controller;

import javax.naming.InsufficientResourcesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.phucx.shop.constant.CookieConstant;
import com.phucx.shop.model.CartOrderItem;
import com.phucx.shop.model.CartProductsCookie;
import com.phucx.shop.model.OrderWithProducts;
import com.phucx.shop.service.cookie.CookieService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CookieService cookieService;
    @PostMapping
    public ResponseEntity<Void> updateCookie(
        HttpServletResponse response, @RequestBody CartOrderItem OrderItem,
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonMappingException, NotFoundException, JsonProcessingException, InsufficientResourcesException {
        cookieService.updateCookie(cartJson, OrderItem, response);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productID}")
    public ResponseEntity<Void> deleteCookie(HttpServletResponse response, 
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson,
        @PathVariable("productID") Integer productID
    ) throws JsonMappingException, JsonProcessingException{
        cookieService.removeProduct(productID, cartJson, response);
        return ResponseEntity.ok().build(); 
    }

    @GetMapping("/products")
    public ResponseEntity<OrderWithProducts> getOrderItems(
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson,
        Authentication authentication
    ) throws JsonMappingException, NotFoundException, JsonProcessingException{
        OrderWithProducts order = cookieService.getOrder(cartJson, authentication);
        return ResponseEntity.ok().body(order);
    }

    @GetMapping("/products/number")
    public ResponseEntity<CartProductsCookie> getNumberOfOrderItems(
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson,
        Authentication authentication
    ) throws JsonMappingException, NotFoundException, JsonProcessingException{
        CartProductsCookie cartProductsCookie = cookieService.getNumberOfProducts(cartJson);
        return ResponseEntity.ok().body(cartProductsCookie);
    }
}
