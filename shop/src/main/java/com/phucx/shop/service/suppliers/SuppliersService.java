package com.phucx.shop.service.suppliers;

import java.util.List;

import org.springframework.data.domain.Page;
import com.phucx.shop.model.Suppliers;

public interface SuppliersService {
    public Suppliers getSupplier(int supplierID);
    public List<Suppliers> getSuppliers();
    public Page<Suppliers> getSuppliers(int pageNumber, int pageSize);
}
