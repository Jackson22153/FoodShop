package com.phucx.shop.service.suppliers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.Suppliers;
import com.phucx.shop.repository.SuppliersRepository;

@Service
public class SuppliersServiceImp implements SuppliersService{
    @Autowired
    private SuppliersRepository supplierRepository;
    @Override
    public List<Suppliers> getSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public Page<Suppliers> getSuppliers(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return supplierRepository.findAll(page);
    }

    @Override
    public Suppliers getSupplier(int supplierID) {
        var supplier = supplierRepository.findById(supplierID);
        if(supplier.isPresent()) return supplier.get();
        return new Suppliers();
    }
    
}
