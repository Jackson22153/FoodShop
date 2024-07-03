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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.shop.constant.CookieConstant;
import com.phucx.shop.exceptions.EmptyCartException;
import com.phucx.shop.exceptions.InvalidOrderException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.CartOrderInfo;
import com.phucx.shop.model.CartProduct;
import com.phucx.shop.model.CartProductInfo;
import com.phucx.shop.model.CartProductsCookie;
import com.phucx.shop.model.CurrentProduct;
import com.phucx.shop.model.Customer;
import com.phucx.shop.model.OrderItem;
import com.phucx.shop.model.OrderItemDiscount;
import com.phucx.shop.model.OrderWithProducts;
import com.phucx.shop.service.bigdecimal.BigDecimalService;
import com.phucx.shop.service.customer.CustomerService;
import com.phucx.shop.service.product.ProductService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    public CartOrderInfo updateCartCookie(String encodedCartJson, List<CartProduct> products, HttpServletResponse response) 
        throws JsonProcessingException, InsufficientResourcesException, NotFoundException {
        log.info("updateCartCookie(encodedCartJson={}, orderItem={})", encodedCartJson, products);
        if(products==null || products.isEmpty()){ throw new NotFoundException("Product does not found");}
        // get existed cart from json format
        TypeReference<List<CartProduct>> typeRef = new TypeReference<List<CartProduct>>() {};
        List<CartProduct> items = new ArrayList<>();
        if(encodedCartJson!=null){
            String cartJson = this.decodeCookie(encodedCartJson);
            items = objectMapper.readValue(cartJson, typeRef);
        }
        // fetch product 
        List<Integer> productIDs = products.stream().map(CartProduct::getProductID).collect(Collectors.toList());
        List<CurrentProduct> fetchedProducts = this.productService.getCurrentProducts(productIDs);

        for (CurrentProduct currentProduct : fetchedProducts) {
            CartProduct cartProduct = this.findProduct(products, currentProduct.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + currentProduct.getProductID() + " name " + currentProduct.getProductName() + " does not found"));
            // check product's quantity with product's inStocks
            if(currentProduct.getUnitsInStock()<cartProduct.getQuantity())
                throw new InsufficientResourcesException("Product " + currentProduct.getProductName() + " exceeds available stock");
            
            // check whether the product exists in cart or not
            boolean isExisted = false;
            for(CartProduct item: items){
                if(item.getProductID().equals(cartProduct.getProductID())){
                    item.setQuantity(cartProduct.getQuantity());
                    item.setIsSelected(cartProduct.getIsSelected());
                    isExisted = true;
                    break;
                }
            }
            // product does not exist in cart
            if(!isExisted){
                items.add(cartProduct);
            }
        }
        // write cart as json format
        String updatedCartJson = objectMapper.writeValueAsString(items);
        // update cookie
        Cookie cookie = this.createCookie(updatedCartJson);
        response.addCookie(cookie);
        return this.createCartOrder(items);
    }

    private Optional<CartProduct> findProduct(List<CartProduct> products, Integer productID){
        return products.stream().filter(product -> product.getProductID().equals(productID)).findFirst();
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
    public CartOrderInfo removeProduct(Integer productID, String encodedcartJson, HttpServletResponse response) 
         throws JsonProcessingException, NotFoundException {
        if(productID!=null){
            TypeReference<List<CartProduct>> typeRef = new TypeReference<List<CartProduct>>() {};
            List<CartProduct> items = new ArrayList<>();
            if(encodedcartJson!=null){
                String cartJson = this.decodeCookie(encodedcartJson);
                items = objectMapper.readValue(cartJson, typeRef);
                List<CartProduct> orderItems = items.stream()
                    .filter(item -> item.getProductID()!=productID)
                    .collect(Collectors.toList());
                // convert into json format
                String updatedCartJson = objectMapper.writeValueAsString(orderItems);
                // update cookie
                Cookie cookie = this.createCookie(updatedCartJson);
                response.addCookie(cookie);
                return this.createCartOrder(orderItems);
            }
        }
        return this.getCartOrder(encodedcartJson);
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
    // create an order from cart items 
    private OrderWithProducts createOrderDetail(List<CartProduct> products) throws NotFoundException{   
        log.info("createOrderDetail({})", products);
        List<Integer> productIDs = products.stream()
            .map(CartProduct::getProductID)
            .collect(Collectors.toList());
        // fetch products from database
        List<CurrentProduct> fetchedProducts = this.productService.getCurrentProducts(productIDs);
        // convert 
        OrderWithProducts order = new OrderWithProducts();
        for (CartProduct product: products) {
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
    public List<CartProduct> getListProducts(String encodedCartJson) throws JsonProcessingException {
        if(encodedCartJson!=null){
            String cartJson = this.decodeCookie(encodedCartJson);
            TypeReference<List<CartProduct>> typeRef = new TypeReference<List<CartProduct>>() {};
            List<CartProduct> listProducts = objectMapper.readValue(cartJson, typeRef);
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
    public OrderWithProducts getOrder(String encodedCartJson, String userID) throws JsonProcessingException, EmptyCartException, InvalidOrderException, com.phucx.shop.exceptions.NotFoundException {
        log.info("getOrder(encodedCartJson={}, userID={})", encodedCartJson, userID);
        Customer customer = this.customerService.getCustomerByUserID(userID);
        // create an order
        OrderWithProducts order = this.getPurchaseOrder(encodedCartJson);
        order.setShipName(customer.getContactName());
        order.setShipAddress(customer.getAddress());
        order.setShipCity(customer.getCity());
        order.setPhone(customer.getPhone());
        return order;
    }

    // get order in cart
    private OrderWithProducts getPurchaseOrder(String encodedCartJson) throws JsonProcessingException, EmptyCartException, InvalidOrderException, NotFoundException{
        if(encodedCartJson==null){
            throw new EmptyCartException("Your cart does not have any products");
        } 
        String cartJson = this.decodeCookie(encodedCartJson);
        TypeReference<List<CartProduct>> typeRef = new TypeReference<List<CartProduct>>() {};
        List<CartProduct> listProducts = objectMapper.readValue(cartJson, typeRef);
        // fitler selected products
        listProducts = listProducts.stream().filter(CartProduct::getIsSelected).collect(Collectors.toList());
        // create an order
        OrderWithProducts order = this.createOrderDetail(listProducts);
        if(order==null || order.getProducts().isEmpty()){
            throw new InvalidOrderException("Your order does not contain any products");
        }
        return order;
    }


    // return order including ship address, name, phone and products of order in cart
    @Override
    public CartOrderInfo getCartProducts(String encodedCartJson) throws JsonProcessingException, NotFoundException {
        log.info("getCartProducts(encodedCartJson={})", encodedCartJson);
        return this.getCartOrder(encodedCartJson);
    }



    // get order in cart
    private CartOrderInfo getCartOrder(String encodedCartJson) throws JsonProcessingException, NotFoundException{
        if(encodedCartJson==null){
            return new CartOrderInfo();
        }   
        String cartJson = this.decodeCookie(encodedCartJson);
        TypeReference<List<CartProduct>> typeRef = new TypeReference<List<CartProduct>>() {};
        List<CartProduct> listProducts = objectMapper.readValue(cartJson, typeRef);

        // create an order
        return this.createCartOrder(listProducts);
    }

    private CartOrderInfo createCartOrder(List<CartProduct> products) throws NotFoundException{
        log.info("createCartOrder({})", products);
        if(products==null || products.isEmpty()) return new CartOrderInfo();
        // extract productIDs from products
        List<Integer> productIDs = products.stream()
            .map(CartProduct::getProductID)
            .collect(Collectors.toList());
        // fetch products from database
        List<CurrentProduct> fetchedProducts = this.productService.getCurrentProducts(productIDs);
        // convert 
        CartOrderInfo order = new CartOrderInfo();
        for (CartProduct product: products) {
            // find product
            CurrentProduct fetchedProduct = this.findCurrentProduct(fetchedProducts, product.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + product.getProductID() + " does not found"));
            // add product to order
            CartProductInfo item = new CartProductInfo(product.getProductID(), fetchedProduct.getProductName(),
                fetchedProduct.getCategoryName(), product.getQuantity(), fetchedProduct.getUnitsInStock(), 
                fetchedProduct.getPicture(), fetchedProduct.getUnitPrice(), product.getIsSelected());     
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
            
            item.setExtendedPrice(bigDecimalService.formatter(extendedPrice));

            // add product
            order.getProducts().add(item);
            // calculate total price of order 
            if(product.getIsSelected()){
                order.setTotalPrice(bigDecimalService.formatter(order.getTotalPrice().add(extendedPrice)));
            }
        }
        return order;
    }

    @Override
    public CartOrderInfo removeProducts(HttpServletResponse response) throws JsonProcessingException {
        log.info("removeProducts()");
        Cookie cookie = this.createCookie("[]");
        response.addCookie(cookie);
        return new CartOrderInfo();
    }

    @Override
    public CartOrderInfo addProduct(String encodedCartJson, List<CartProduct> cartProducts,
            HttpServletResponse response) throws JsonProcessingException, InsufficientResourcesException, NotFoundException {
        log.info("addProduct(encodedCartJson={}, cartProducts={})", encodedCartJson, cartProducts);
        if(cartProducts==null || cartProducts.isEmpty()){ throw new NotFoundException("Product does not found");}
        // get existed cart from json format
        TypeReference<List<CartProduct>> typeRef = new TypeReference<List<CartProduct>>() {};
        List<CartProduct> items = new ArrayList<>();
        if(encodedCartJson!=null){
            String cartJson = this.decodeCookie(encodedCartJson);
            items = objectMapper.readValue(cartJson, typeRef);
        }
        // fetch product 
        List<Integer> productIDs = cartProducts.stream().map(CartProduct::getProductID).collect(Collectors.toList());
        List<CurrentProduct> fetchedProducts = this.productService.getCurrentProducts(productIDs);

        for (CurrentProduct currentProduct : fetchedProducts) {
            CartProduct cartProduct = this.findProduct(cartProducts, currentProduct.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + currentProduct.getProductID() + " name " + currentProduct.getProductName() + " does not found"));
            // check product's quantity with product's inStocks
            if(currentProduct.getUnitsInStock()<cartProduct.getQuantity())
                throw new InsufficientResourcesException("Product " + currentProduct.getProductName() + " exceeds available stock");
            
            // check whether the product exists in cart or not
            boolean isExisted = false;
            for(CartProduct item: items){
                if(item.getProductID().equals(cartProduct.getProductID())){
                    item.setQuantity(cartProduct.getQuantity() + item.getQuantity());
                    item.setIsSelected(cartProduct.getIsSelected());
                    isExisted = true;
                    break;
                }
            }
            // product does not exist in cart
            if(!isExisted){
                items.add(cartProduct);
            }
        }
        // write cart as json format
        String updatedCartJson = objectMapper.writeValueAsString(items);
        // update cookie
        Cookie cookie = this.createCookie(updatedCartJson);
        response.addCookie(cookie);
        return this.createCartOrder(items);

    }

    @Override
    public CartOrderInfo removeProducts(List<Integer> productIDs, String encodedCartJson, HttpServletResponse response)
            throws JsonProcessingException, NotFoundException {
        log.info("removeProducts(productIDs={}, encodedCartJson={}", productIDs, encodedCartJson);
        
        TypeReference<List<CartProduct>> typeRef = new TypeReference<List<CartProduct>>() {};
        List<CartProduct> items = new ArrayList<>();
        if(encodedCartJson!=null){
            String cartJson = this.decodeCookie(encodedCartJson);
            items = objectMapper.readValue(cartJson, typeRef);
            // filter products
            List<CartProduct> orderItems = items.stream()
                .filter(item -> !productIDs.contains(item.getProductID()))
                .collect(Collectors.toList());
            // convert into json format
            String updatedCartJson = objectMapper.writeValueAsString(orderItems);
            // update cookie
            Cookie cookie = this.createCookie(updatedCartJson);
            response.addCookie(cookie);
            return this.createCartOrder(orderItems);
        }

        return this.getCartOrder(encodedCartJson);
    }
}
