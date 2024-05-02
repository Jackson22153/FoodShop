package com.phucx.shop.service.shipper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.Shipper;
import com.phucx.shop.repository.ShipperRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class ShipperServiceImp implements ShipperService{
    @Autowired
    private ShipperRepository shipperRepository;
    @Override
    public List<Shipper> getShippers() {
        return shipperRepository.findAll();
    }

    @Override
    public Page<Shipper> getShippers(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return shipperRepository.findAll(page);
    }

    @Override
    public Shipper getShipper(int shipperID) {
        var shipper = shipperRepository.findById(shipperID)
            .orElseThrow(()-> new NotFoundException("Shipper " + shipperID + " does not found"));
        return shipper;
    }
    
}
