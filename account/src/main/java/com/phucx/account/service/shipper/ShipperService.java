package com.phucx.account.service.shipper;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import com.phucx.account.exception.ShipperNotFoundException;
import com.phucx.account.model.Shipper;

@CacheConfig(cacheNames = "shipper")
public interface ShipperService {
    @Cacheable(key = "#shipperID")
    public Shipper getShipperByID(Integer shipperID) throws ShipperNotFoundException;
}
