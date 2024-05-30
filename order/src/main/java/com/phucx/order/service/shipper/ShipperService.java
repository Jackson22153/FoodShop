package com.phucx.order.service.shipper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.order.model.Shipper;

public interface ShipperService {
    public Shipper getShipper(Integer shipperID) throws JsonProcessingException;
    
} 
