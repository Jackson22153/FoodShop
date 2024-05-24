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
import com.phucx.shop.service.cart.CartService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping
    public ResponseEntity<Void> updateCookie(
        HttpServletResponse response, @RequestBody CartOrderItem OrderItem,
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonMappingException, NotFoundException, JsonProcessingException, InsufficientResourcesException {
        cartService.updateCookie(cartJson, OrderItem, response);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productID}")
    public ResponseEntity<Void> deleteCookie(HttpServletResponse response, 
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson,
        @PathVariable("productID") Integer productID
    ) throws JsonMappingException, JsonProcessingException{
        cartService.removeProduct(productID, cartJson, response);
        return ResponseEntity.ok().build(); 
    }

    @GetMapping("/products")
    public ResponseEntity<OrderWithProducts> getOrderItems(
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson,
        Authentication authentication
    ) throws JsonMappingException, NotFoundException, JsonProcessingException{
        OrderWithProducts order = cartService.getOrder(cartJson, authentication);
        return ResponseEntity.ok().body(order);
    }

    @GetMapping("/products/number")
    public ResponseEntity<CartProductsCookie> getNumberOfOrderItems(
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson,
        Authentication authentication
    ) throws JsonMappingException, NotFoundException, JsonProcessingException{
        CartProductsCookie cartProductsCookie = cartService.getNumberOfProducts(cartJson);
        return ResponseEntity.ok().body(cartProductsCookie);
    }
}
