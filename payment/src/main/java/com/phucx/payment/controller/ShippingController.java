package com.phucx.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.payment.model.ShippingResponse;
import com.phucx.payment.service.shipping.ShippingService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RestController
@RequestMapping(value = "/shipping", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShippingController {
    @Autowired
    private ShippingService shippingService;

    @GetMapping("/cost")
    @Operation(summary = "Estimate shipping cost", tags = {"customer", "shipping"})
    public ResponseEntity<ShippingResponse> shippingCost(
        @RequestParam(name = "wardCode") String wardCode,
        @RequestParam(name = "districtId") Integer districtID,
        @RequestParam(name = "cityId") Integer cityId,
        @CookieValue(name = "cart") String encodedCartJson,
        Authentication authentication
    ) throws JsonProcessingException{
        String username = authentication.getName();
        ShippingResponse response = shippingService.costEstimate(
            cityId, districtID, wardCode, username, encodedCartJson);
        return ResponseEntity.ok().body(response);
    }
}
