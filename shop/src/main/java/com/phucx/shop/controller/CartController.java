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

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Operation(summary = "Update cart", tags = {"tutorials", "post", "customer"},
        description = "Update cart's products")
    @PostMapping
    public ResponseEntity<CartOrderInfo> updateCartCookie(
        HttpServletResponse response, @RequestBody List<CartProduct> products,
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException, InsufficientResourcesException, NotFoundException {
        CartOrderInfo cartOrder = cartService.updateCartCookie(cartJson, products, response);
        return ResponseEntity.ok().body(cartOrder);
    }

    @Operation(summary = "Add product to cart", tags = {"tutorials", "post", "customer"},
        description = "Increate or decreate products's quantity or add a new product to cart")
    @PostMapping("/product")
    public ResponseEntity<CartOrderInfo> addProduct(
        HttpServletResponse response, @RequestBody List<CartProduct> products,
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException, InsufficientResourcesException, NotFoundException {
        CartOrderInfo cartOrder = cartService.addProduct(cartJson, products, response);
        return ResponseEntity.ok().body(cartOrder);
    }

    @Operation(summary = "Remove some products from cart", tags = {"tutorials", "post", "customer"})
    @PostMapping("/products")
    public ResponseEntity<CartOrderInfo> removeCartProductsCookie(HttpServletResponse response, 
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson,
        @RequestBody List<Integer> productIDs
    ) throws JsonProcessingException, NotFoundException{
        CartOrderInfo cartOrder = cartService.removeProducts(productIDs, cartJson, response);
        return ResponseEntity.ok().body(cartOrder); 
    }

    @Operation(summary = "Remove product from cart", tags = {"tutorials", "delete", "customer"})
    @DeleteMapping("/product/{productID}")
    public ResponseEntity<CartOrderInfo> deleteCartProductCookie(HttpServletResponse response, 
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson,
        @PathVariable("productID") Integer productID
    ) throws JsonProcessingException, NotFoundException{
        CartOrderInfo cartOrder = cartService.removeProduct(productID, cartJson, response);
        return ResponseEntity.ok().body(cartOrder); 
    }

    @Operation(summary = "Remove all products in cart", tags = {"tutorials", "delete", "customer"})
    @DeleteMapping
    public ResponseEntity<CartOrderInfo> deleteCartProductsCookie(HttpServletResponse response, 
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException{
        CartOrderInfo cartOrder = cartService.removeProducts(response);
        return ResponseEntity.ok().body(cartOrder); 
    }

    @Operation(summary = "Get products in cart", tags = {"tutorials", "get", "customer"})
    @GetMapping("/products")
    public ResponseEntity<CartOrderInfo> getOrderItems(
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException, NotFoundException{
        CartOrderInfo order = cartService.getCartProducts(cartJson);
        return ResponseEntity.ok().body(order);
    }

    @Operation(summary = "Get number of products in cart", tags = {"tutorials", "get", "customer"})
    @GetMapping("/products/number")
    public ResponseEntity<CartProductsCookie> getNumberOfOrderItems(
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson
    ) throws JsonProcessingException{
        CartProductsCookie cartProductsCookie = cartService.getNumberOfProducts(cartJson);
        return ResponseEntity.ok().body(cartProductsCookie);
    }

    @Operation(summary = "Get order of selected products in cart", tags = {"tutorials", "get", "customer"},
        description = "Get an order based on the seletecd products")
    @GetMapping("/order")
    public ResponseEntity<OrderWithProducts> getOrder(
        @CookieValue(name = CookieConstant.CART_COOKIE, required = false) String cartJson,
        Authentication authentication
    ) throws JsonProcessingException, EmptyCartException, InvalidOrderException, NotFoundException{
        OrderWithProducts order = cartService.getOrder(cartJson, authentication.getName());
        return ResponseEntity.ok().body(order);
    }
}
