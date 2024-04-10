package com.phucx.shop.service.cookie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.shop.constant.CookieConstant;
import com.phucx.shop.constant.ProductStatus;
import com.phucx.shop.model.OrderItem;
import com.phucx.shop.model.Products;
import com.phucx.shop.repository.ProductsRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;

@Service
public class CookieServiceImp implements CookieService{
    private ObjectMapper objectMapper;

    @Autowired
    private ProductsRepository productsRepository;

    public CookieServiceImp(){
        objectMapper = new ObjectMapper();
    }

    @Override
    public void updateCookie(String cartJson, OrderItem orderProduct, HttpServletResponse response) 
        throws JsonMappingException, JsonProcessingException, NotFoundException {
        
        if(orderProduct.getProductID()!=null){
            TypeReference<List<OrderItem>> typeRef = new TypeReference<List<OrderItem>>() {};
            List<OrderItem> items = new ArrayList<>();
            if(cartJson!=null){
                items = objectMapper.readValue(cartJson, typeRef);
            }
            Products product = productsRepository.findByProductIDAndDiscontinued(
                orderProduct.getProductID(), ProductStatus.Coninuted.getStatus());
            if(product!=null){
                items.add(orderProduct);
                
            }else throw new NotFoundException("Product does not found");
        }
    }

    private Cookie createCookie(String cartJson){
        Cookie cookie = new Cookie(CookieConstant.CART_COOKIE, cartJson);
        cookie.setPath(CookieConstant.PATH_COOKIE);
        cookie.setMaxAge(CookieConstant.MAX_AGE);
        return cookie;
    }
    
    @Override
    public void removeProduct(Integer productID, String cartJson, HttpServletResponse response) 
         throws JsonMappingException, JsonProcessingException {
        if(productID!=null){
            TypeReference<List<OrderItem>> typeRef = new TypeReference<List<OrderItem>>() {};
            List<OrderItem> items = new ArrayList<>();
            if(cartJson!=null){
                items = objectMapper.readValue(cartJson, typeRef);
            }
            List<OrderItem> orderItems = items.stream()
                .filter(item -> item.getProductID()!=productID)
                .collect(Collectors.toList());
            String updatedCartJson = objectMapper.writeValueAsString(orderItems);
            Cookie cookie = this.createCookie(updatedCartJson);
            response.addCookie(cookie);
        }
    }

    @Override
    public  List<OrderItem> getListProduct(String cartJson) 
        throws JsonMappingException, JsonProcessingException, NotFoundException {
            
        TypeReference<List<OrderItem>> typeRef = new TypeReference<List<OrderItem>>() {};
        List<OrderItem> listProducts = objectMapper.readValue(cartJson, typeRef);
        listProducts.stream().forEach(product ->{
            var productOp = productsRepository.findById(product.getProductID());
            if(!productOp.isPresent()) throw new NotFoundException("Product not found"); 
        });

        return listProducts;
    }
    
}
