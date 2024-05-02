package com.phucx.shop.service.supplier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.Supplier;
import com.phucx.shop.repository.SupplierRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class SupplierServiceImp implements SupplierService{
    @Autowired
    private SupplierRepository supplierRepository;
    @Override
    public List<Supplier> getSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public Page<Supplier> getSuppliers(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return supplierRepository.findAll(page);
    }

    @Override
    public Supplier getSupplier(int supplierID) {
        var supplier = supplierRepository.findById(supplierID)
            .orElseThrow(()-> new NotFoundException("Supplier " + supplierID + " does not found"));
        return  supplier;
    }
    
}
