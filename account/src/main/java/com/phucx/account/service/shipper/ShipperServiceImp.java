package com.phucx.account.service.shipper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.account.exception.ShipperNotFoundException;
import com.phucx.account.model.Shipper;
import com.phucx.account.repository.ShipperRepository;

@Service
public class ShipperServiceImp implements ShipperService{
    @Autowired
    private ShipperRepository shipperRepository;

    @Override
    public Shipper getShipperByID(Integer shipperID) throws ShipperNotFoundException {
        return shipperRepository.findById(shipperID)
            .orElseThrow(()-> new ShipperNotFoundException("Shipper "+ shipperID + " does not found"));
    }
    
}
