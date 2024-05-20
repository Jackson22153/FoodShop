package com.phucx.order.service.shipper;

import org.springframework.stereotype.Service;

import com.phucx.order.model.Shipper;
import com.phucx.order.repository.ShipperRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class ShipperServiceImp implements ShipperService{
    private ShipperRepository shipperRepository;


    @Override
    public Shipper getShipper(Integer shipperID) {
        return shipperRepository.findById(shipperID)
            .orElseThrow(()-> new NotFoundException("Shipper "+ shipperID +" does not found"));
    }
    
}
