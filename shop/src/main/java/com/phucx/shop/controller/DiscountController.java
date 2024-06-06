package com.phucx.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.shop.config.WebConfig;
import com.phucx.shop.exceptions.InvalidDiscountException;
import com.phucx.shop.model.Discount;
import com.phucx.shop.model.DiscountDetail;
import com.phucx.shop.model.DiscountType;
import com.phucx.shop.model.DiscountWithProduct;
import com.phucx.shop.model.ResponseFormat;
import com.phucx.shop.service.discount.DiscountService;

@RestController
@RequestMapping("/discount")
public class DiscountController {
    @Autowired
    private DiscountService discountService;
     // discount
    @PutMapping
    public ResponseEntity<ResponseFormat> insertDiscount(
        @RequestBody DiscountWithProduct discount
    ) throws InvalidDiscountException, RuntimeException{
        Discount newDiscount = discountService.insertDiscount(discount);
        boolean status = newDiscount!=null?true:false;
        ResponseFormat data = new ResponseFormat(status);

        return ResponseEntity.ok().body(data);
    }

    @PostMapping
    public ResponseEntity<ResponseFormat> updateDiscount(
        @RequestBody DiscountWithProduct discount
    ) throws InvalidDiscountException{
        Boolean status = discountService.updateDiscount(discount);
        ResponseFormat data = new ResponseFormat(status);
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/status")
    public ResponseEntity<ResponseFormat> updateDiscountStatus(
        @RequestBody Discount discount
    ) throws InvalidDiscountException{
        Boolean check = discountService.updateDiscountStatus(discount);
        ResponseFormat data = new ResponseFormat(check);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/product/{productID}")
    public ResponseEntity<Page<DiscountDetail>> getDiscountsByProductID(
        @PathVariable(name = "productID") Integer productID,
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber: 0;
        Page<DiscountDetail> discounts = discountService.getDiscountsByProduct(
            productID, pageNumber, WebConfig.PAGE_SIZE);
        return ResponseEntity.ok().body(discounts);
    }

    @GetMapping("/{discountID}")
    public ResponseEntity<DiscountDetail> getDiscountDetail(
        @PathVariable(name = "discountID") String discountID
    ){
        DiscountDetail discount = discountService.getDiscountDetail(discountID);
        return ResponseEntity.ok().body(discount);
    }

    @GetMapping("/discountTypes")
    public ResponseEntity<Page<DiscountType>> getDiscountTypes(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber: 0;
        Page<DiscountType> types = discountService.getDiscountTypes(
            pageNumber, WebConfig.PAGE_SIZE);
        return ResponseEntity.ok().body(types);
    }
}
