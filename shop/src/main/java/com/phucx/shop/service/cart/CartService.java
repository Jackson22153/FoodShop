package com.phucx.shop.service.cart;

import java.util.List;

import javax.naming.InsufficientResourcesException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.shop.model.CartOrderItem;
import com.phucx.shop.model.CartProductsCookie;
import com.phucx.shop.model.OrderWithProducts;

import jakarta.servlet.http.HttpServletResponse;

public interface CartService {
    // remove product from cart
    public void removeProduct(Integer productID, String encodedCartJson, HttpServletResponse response) throws JsonProcessingException;
    // get order cart product
    public OrderWithProducts getCartProducts(String encodedCartJson) throws JsonProcessingException;
    // get order for user to check out
    public OrderWithProducts getOrder(String encodedCartJson, String userID) throws JsonProcessingException;
    // update product in cart
    public void updateCookie(String encodedCartJson, CartOrderItem orderProduct, HttpServletResponse response) 
        throws JsonProcessingException, InsufficientResourcesException;
    // get number of products
        public CartProductsCookie getNumberOfProducts(String encodedCartJson) throws JsonProcessingException;
    // get list of product in cart
    public List<CartOrderItem> getListProducts(String encodedCartJson) throws JsonProcessingException;
}
