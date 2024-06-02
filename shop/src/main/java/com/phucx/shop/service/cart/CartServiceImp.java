package com.phucx.shop.service.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.naming.InsufficientResourcesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.shop.constant.CookieConstant;
import com.phucx.shop.constant.ProductStatus;
import com.phucx.shop.model.CartOrderItem;
import com.phucx.shop.model.CartProductsCookie;
import com.phucx.shop.model.CurrentProduct;
import com.phucx.shop.model.Customer;
import com.phucx.shop.model.OrderItem;
import com.phucx.shop.model.OrderItemDiscount;
import com.phucx.shop.model.OrderWithProducts;
import com.phucx.shop.model.Product;
import com.phucx.shop.service.bigdecimal.BigDecimalService;
import com.phucx.shop.service.customer.CustomerService;
import com.phucx.shop.service.product.ProductService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartServiceImp implements CartService{
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private BigDecimalService bigDecimalService;
    @Autowired
    private CustomerService customerService;

    @Override
    public void updateCookie(String encodedCartJson, CartOrderItem orderProduct, HttpServletResponse response) 
        throws JsonProcessingException, InsufficientResourcesException {
        log.info("updateCookie(encodedCartJson={}, orderItem={})", encodedCartJson, orderProduct);
        if(orderProduct.getProductID()!=null){
            // get existed cart from json format
            TypeReference<List<CartOrderItem>> typeRef = new TypeReference<List<CartOrderItem>>() {};
            List<CartOrderItem> items = new ArrayList<>();
            if(encodedCartJson!=null){
                String cartJson = this.decodeCookie(encodedCartJson);
                items = objectMapper.readValue(cartJson, typeRef);
            }
            // fetch product 
            Product product = this.productService.getProduct(
                orderProduct.getProductID(), ProductStatus.Coninuted.getStatus());
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
         throws JsonProcessingException {
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
    private OrderWithProducts createOrderDetail(List<CartOrderItem> products){   
        log.info("createOrderDetail(products={})");
        List<Integer> productIDs = products.stream()
            .map(CartOrderItem::getProductID)
            .collect(Collectors.toList());
        // fetch products from database
        List<CurrentProduct> fetchedProducts = this.productService.getCurrentProducts(productIDs);
        // convert 
        OrderWithProducts order = new OrderWithProducts();
        for (CartOrderItem product: products) {
            // find product
            CurrentProduct fetchedProduct = this.findCurrentProduct(fetchedProducts, product.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + product.getProductID() + " does not found"));
            // add product to order
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
        log.info("OrderDetail: {}", order);
        return order;
    }

    // find products
    private Optional<CurrentProduct> findCurrentProduct(List<CurrentProduct> products, Integer productID){
        return products.stream().filter(product-> product.getProductID().equals(productID)).findFirst();
    }

    @Override
    public List<CartOrderItem> getListProducts(String encodedCartJson) throws JsonProcessingException 
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
    public CartProductsCookie getNumberOfProducts(String encodedCartJson) throws JsonProcessingException {
        int numberOfCartItems = this.getListProducts(encodedCartJson).size();
        CartProductsCookie cartProductsCookie = new CartProductsCookie(numberOfCartItems);
        return cartProductsCookie;
    }
    @Override
    public OrderWithProducts getOrder(String encodedCartJson, String userID) throws JsonProcessingException {
        log.info("getOrder(encodedCartJson={}, userID={})", encodedCartJson, userID);
        Customer customer = this.customerService.getCustomerByUserID(userID);
        // create an order
        OrderWithProducts order = this.getCartOrder(encodedCartJson);
        order.setShipName(customer.getContactName());
        order.setShipAddress(customer.getAddress());
        order.setShipCity(customer.getCity());
        order.setPhone(customer.getPhone());
        return order;
    }
    // return order including ship address, name, phone and products of order in cart
    @Override
    public OrderWithProducts getCartProducts(String encodedCartJson) throws JsonProcessingException {
        log.info("getCartProducts(encodedCartJson={})", encodedCartJson);
        return this.getCartOrder(encodedCartJson);
    }

    private OrderWithProducts getCartOrder(String encodedCartJson) throws JsonMappingException, JsonProcessingException{
        if(encodedCartJson!=null){
            String cartJson = this.decodeCookie(encodedCartJson);
            TypeReference<List<CartOrderItem>> typeRef = new TypeReference<List<CartOrderItem>>() {};
            List<CartOrderItem> listProducts = objectMapper.readValue(cartJson, typeRef);
            // create an order
            return this.createOrderDetail(listProducts);
        }   
        return new OrderWithProducts();
    }
}
