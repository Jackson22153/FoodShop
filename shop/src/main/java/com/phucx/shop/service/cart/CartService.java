package com.phucx.shop.service.cart;

import java.util.List;

import javax.naming.InsufficientResourcesException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.shop.exceptions.EmptyCartException;
import com.phucx.shop.exceptions.InvalidOrderException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.CartOrderInfo;
import com.phucx.shop.model.CartProduct;
import com.phucx.shop.model.CartProductsCookie;
import com.phucx.shop.model.OrderWithProducts;
import com.phucx.shop.model.ShippingProduct;

import jakarta.servlet.http.HttpServletResponse;

public interface CartService {
    // add product to cart
    public CartOrderInfo addProduct(String encodedCartJson, List<CartProduct> cartProducts, HttpServletResponse response) 
        throws JsonProcessingException, InsufficientResourcesException, NotFoundException;
    // remove product from cart
    public CartOrderInfo removeProduct(Integer productID, String encodedCartJson, HttpServletResponse response) throws JsonProcessingException, NotFoundException;
    public CartOrderInfo removeProducts(HttpServletResponse response) throws JsonProcessingException;
    public CartOrderInfo removeProducts(List<Integer> productIDs, String encodedCartJson, HttpServletResponse response) throws JsonProcessingException, NotFoundException;
    // get order cart product
    public CartOrderInfo getCartProducts(String encodedCartJson) throws JsonProcessingException, NotFoundException;
    // get order for user to check out
    public OrderWithProducts getOrder(String encodedCartJson, String userID) 
        throws JsonProcessingException, EmptyCartException, InvalidOrderException, NotFoundException;
    // update product in cart
    public CartOrderInfo updateCartCookie(String encodedCartJson, List<CartProduct> cartProducts, HttpServletResponse response) 
        throws JsonProcessingException, InsufficientResourcesException, NotFoundException;
    // get number of products
        public CartProductsCookie getNumberOfProducts(String encodedCartJson) throws JsonProcessingException;
    // get list of product in cart
    public List<CartProduct> getListProducts(String encodedCartJson) throws JsonProcessingException;

    public ShippingProduct getShippingProduct(String encodedCartJson) throws JsonProcessingException, EmptyCartException, InvalidOrderException, NotFoundException;
}
