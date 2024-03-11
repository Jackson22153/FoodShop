package com.phucx.shop.service.shippers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.Shippers;
import com.phucx.shop.repository.ShippersRepository;

@Service
public class ShippersServiceImp implements ShippersService{
    @Autowired
    private ShippersRepository shipperRepository;
    @Override
    public List<Shippers> getShippers() {
        return shipperRepository.findAll();
    }

    @Override
    public Page<Shippers> getShippers(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return shipperRepository.findAll(page);
    }

    @Override
    public Shippers getShipper(int shipperID) {
        var shipper = shipperRepository.findById(shipperID);
        if(shipper.isPresent()) return shipper.get();
        return new Shippers();
    }
    
}
