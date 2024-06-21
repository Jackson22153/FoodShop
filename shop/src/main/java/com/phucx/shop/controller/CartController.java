package com.phucx.shop.controller;

import java.util.List;

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
import com.phucx.shop.constant.CookieConstant;
import com.phucx.shop.exceptions.EmptyCartException;
import com.phucx.shop.exceptions.InvalidOrderException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.CartOrderInfo;
import com.phucx.shop.model.CartProduct;
import com.phucx.shop.model.CartProductsCookie;
import com.phucx.shop.model.OrderWithProducts;
import com.phucx.shop.service.cart.CartService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<CartOrderInfo> updateCartCookie(
        HttpServletResponse response, @RequestBody List<CartProduct> products,
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException, InsufficientResourcesException {
        CartOrderInfo cartOrder = cartService.updateCartCookie(cartJson, products, response);
        return ResponseEntity.ok().body(cartOrder);
    }

    @PostMapping("/product")
    public ResponseEntity<CartOrderInfo> addProduct(
        HttpServletResponse response, @RequestBody List<CartProduct> products,
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException, InsufficientResourcesException {
        CartOrderInfo cartOrder = cartService.addProduct(cartJson, products, response);
        return ResponseEntity.ok().body(cartOrder);
    }

    @DeleteMapping("/product/{productID}")
    public ResponseEntity<CartOrderInfo> deleteCartProductCookie(HttpServletResponse response, 
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson,
        @PathVariable("productID") Integer productID
    ) throws JsonProcessingException{
        CartOrderInfo cartOrder = cartService.removeProduct(productID, cartJson, response);
        return ResponseEntity.ok().body(cartOrder); 
    }

    @DeleteMapping
    public ResponseEntity<CartOrderInfo> deleteCartProductsCookie(HttpServletResponse response, 
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException{
        CartOrderInfo cartOrder = cartService.removeProducts(response);
        return ResponseEntity.ok().body(cartOrder); 
    }

    @GetMapping("/products")
    public ResponseEntity<CartOrderInfo> getOrderItems(
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException{
        CartOrderInfo order = cartService.getCartProducts(cartJson);
        return ResponseEntity.ok().body(order);
    }

    @GetMapping("/products/number")
    public ResponseEntity<CartProductsCookie> getNumberOfOrderItems(
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException{
        CartProductsCookie cartProductsCookie = cartService.getNumberOfProducts(cartJson);
        return ResponseEntity.ok().body(cartProductsCookie);
    }

    @GetMapping("/order")
    public ResponseEntity<OrderWithProducts> getOrder(
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson,
        Authentication authentication
    ) throws JsonProcessingException, EmptyCartException, InvalidOrderException, NotFoundException{
        OrderWithProducts order = cartService.getOrder(cartJson, authentication.getName());
        return ResponseEntity.ok().body(order);
    }
}
