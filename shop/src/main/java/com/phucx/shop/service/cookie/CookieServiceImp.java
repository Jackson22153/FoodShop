package com.phucx.shop.service.cookie;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.InsufficientResourcesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.shop.constant.CookieConstant;
import com.phucx.shop.constant.ProductStatus;
import com.phucx.shop.model.CartOrderItem;
import com.phucx.shop.model.CartProductsCookie;
import com.phucx.shop.model.CurrentProductList;
import com.phucx.shop.model.Customer;
import com.phucx.shop.model.OrderItem;
import com.phucx.shop.model.OrderItemDiscount;
import com.phucx.shop.model.OrderWithProducts;
import com.phucx.shop.model.Product;
import com.phucx.shop.repository.CurrentProductListRepository;
import com.phucx.shop.repository.ProductRepository;
import com.phucx.shop.service.bigdecimal.BigDecimalService;
import com.phucx.shop.service.customer.CustomerService;
import com.phucx.shop.service.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CookieServiceImp implements CookieService{
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CurrentProductListRepository currentProductListRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private BigDecimalService bigDecimalService;
    @Override
    public void updateCookie(String encodedCartJson, CartOrderItem orderProduct, HttpServletResponse response) 
        throws JsonMappingException, JsonProcessingException, NotFoundException, InsufficientResourcesException {
        log.info("updateCookie(encodedCartJson={}, orderItem={})", encodedCartJson, orderProduct);
        if(orderProduct.getProductID()!=null){
            // get existed cart from json format
            TypeReference<List<CartOrderItem>> typeRef = new TypeReference<List<CartOrderItem>>() {};
            List<CartOrderItem> items = new ArrayList<>();
            if(encodedCartJson!=null){
                String cartJson = this.decodeCookie(encodedCartJson);
                items = objectMapper.readValue(cartJson, typeRef);
            }
            Product product = productRepository.findByProductIDAndDiscontinued(
                orderProduct.getProductID(), ProductStatus.Coninuted.getStatus())
                .orElseThrow(()-> new NotFoundException("Product " + orderProduct.getProductID() +" does not found"));

            // check product's quantity with product's inStocks
            if(product.getUnitsInStock()<orderProduct.getQuantity())
                throw new InsufficientResourcesException("Product " + product.getProductName() + " exceeds available stock");
            
            boolean isExisted = false;
            // product exists in cart
            for(CartOrderItem item: items){
                if(item.getProductID().equals(orderProduct.getProductID())){
                    item.setQuantity(item.getQuantity()+ orderProduct.getQuantity());
                    isExisted = true;
                    break;
                }
            }

            // product does not exist in cart
            if(!isExisted){
                items.add(orderProduct);
            }
            // write cart as json format
            String updatedCartJson = objectMapper.writeValueAsString(items);

            log.info("updated cartJson: {}", updatedCartJson);
            Cookie cookie = this.createCookie(updatedCartJson);
            response.addCookie(cookie);
        }
    }
    // create a new cookie
    private Cookie createCookie(String cartJson){
        String encodedData = this.encodeCookie(cartJson);
        Cookie cookie = new Cookie(CookieConstant.CART_COOKIE, encodedData);
        cookie.setPath(CookieConstant.PATH_COOKIE);
        cookie.setMaxAge(CookieConstant.MAX_AGE);
        return cookie;
    }
    
    @Override
    public void removeProduct(Integer productID, String encodedcartJson, HttpServletResponse response) 
         throws JsonMappingException, JsonProcessingException {
        if(productID!=null){
            TypeReference<List<OrderItem>> typeRef = new TypeReference<List<OrderItem>>() {};
            List<OrderItem> items = new ArrayList<>();
            if(encodedcartJson!=null){
                String cartJson = this.decodeCookie(encodedcartJson);
                items = objectMapper.readValue(cartJson, typeRef);
                List<OrderItem> orderItems = items.stream()
                    .filter(item -> item.getProductID()!=productID)
                    .collect(Collectors.toList());
                // convert into json format
                String updatedCartJson = objectMapper.writeValueAsString(orderItems);
                // update cookie
                Cookie cookie = this.createCookie(updatedCartJson);
                response.addCookie(cookie);
            }
        }
    }
    // return order including ship address, name, phone and products of order in cart
    @Override
    public OrderWithProducts getOrder(String encodedCartJson, Authentication authentication) 
        throws JsonMappingException, JsonProcessingException, NotFoundException {
        
        log.info("getOrder(encodedCartJson={}, userID={})", encodedCartJson, authentication.getName());
        if(encodedCartJson!=null){
            String cartJson = this.decodeCookie(encodedCartJson);
            TypeReference<List<CartOrderItem>> typeRef = new TypeReference<List<CartOrderItem>>() {};
            List<CartOrderItem> listProducts = objectMapper.readValue(cartJson, typeRef);
            // fetch customer
            String userID = userService.getUserID(authentication);
            Customer customer = customerService.getCustomerByUserID(userID);
            // create an order
            return this.createOrderDetail(listProducts, customer);
        }   
        return new OrderWithProducts();
    }
    // decode from base64
    private String decodeCookie(String cookie){
        byte[] cookieDecoded = Base64.getDecoder().decode(cookie);
        return new String(cookieDecoded);
    }
    // encode to base64
    private String encodeCookie(String cookie){
        return Base64.getEncoder().encodeToString(cookie.getBytes());
    }
    // create a new order
    private OrderWithProducts createOrderDetail(List<CartOrderItem> products, Customer customer){   
        log.info("createOrderDetail(products={}, customer={})", products, customer);
        List<Integer> productIDs = products.stream()
            .map(CartOrderItem::getProductID)
            .collect(Collectors.toList());
        // fetch products from database
        List<CurrentProductList> fetchedProducts = currentProductListRepository
            .findAllByProductIDOrderByProductIDAsc(productIDs);
        // sort products
        Collections.sort(products, Comparator.comparingInt(CartOrderItem::getProductID));
        OrderWithProducts order = new OrderWithProducts();
        // add default shipping detail of customer
        order.setShipAddress(customer.getAddress());
        order.setShipCity(customer.getCity());
        order.setShipName(customer.getContactName());
        order.setPhone(customer.getPhone());
        // convert 
        for (int i=0;i<products.size();i++) {
            CartOrderItem product = products.get(i);
            CurrentProductList fetchedProduct = null;
            // get corresponding product
            for(int j=i;j<fetchedProducts.size();j++){
                if(product.getProductID().equals(fetchedProducts.get(i).getProductID())){
                    fetchedProduct = fetchedProducts.get(i);
                }
            }
            // add product to order
            if(fetchedProduct!=null){
                // add product to cart
                OrderItem item = new OrderItem(product.getProductID(), fetchedProduct.getProductName(),
                    fetchedProduct.getCategoryName(), product.getQuantity(), fetchedProduct.getUnitsInStock(), 
                    fetchedProduct.getPicture(), fetchedProduct.getUnitPrice());

                // add discount
                OrderItemDiscount discount = new OrderItemDiscount();
                discount.setDiscountID(fetchedProduct.getDiscountID());
                discount.setDiscountPercent(fetchedProduct.getDiscountPercent());
                item.getDiscounts().add(discount);

                // add total discount to product
                item.setTotalDiscount(discount.getDiscountPercent());

                // calculate extended price
                Double productDiscount = 1- Double.valueOf(fetchedProduct.getDiscountPercent())/100;
                BigDecimal price = BigDecimal.valueOf(product.getQuantity()).multiply(item.getUnitPrice());
                BigDecimal extendedPrice = price.multiply(BigDecimal.valueOf(productDiscount));
                // log.info("discount: {}", (extendedPrice));
                item.setExtendedPrice(bigDecimalService.formatter(extendedPrice));

                // add product
                order.getProducts().add(item);
                // calculate total price of order 
                order.setTotalPrice(bigDecimalService.formatter(order.getTotalPrice().add(extendedPrice)));
            }
        }
        log.info("OrderDetail: {}", order);
        return order;
    }
    @Override
    public List<CartOrderItem> getListProducts(String encodedCartJson) 
    throws JsonMappingException, JsonProcessingException 
    {
        if(encodedCartJson!=null){
            String cartJson = this.decodeCookie(encodedCartJson);
            TypeReference<List<CartOrderItem>> typeRef = new TypeReference<List<CartOrderItem>>() {};
            List<CartOrderItem> listProducts = objectMapper.readValue(cartJson, typeRef);
            return listProducts;
        }   
        return new ArrayList<>();
    }
    @Override
    public CartProductsCookie getNumberOfProducts(String encodedCartJson) throws JsonMappingException, JsonProcessingException {
        int numberOfCartItems = this.getListProducts(encodedCartJson).size();
        CartProductsCookie cartProductsCookie = new CartProductsCookie(numberOfCartItems);
        return cartProductsCookie;
    }
    
}
