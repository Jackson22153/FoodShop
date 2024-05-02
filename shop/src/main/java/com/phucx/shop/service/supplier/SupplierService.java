package com.phucx.shop.service.supplier;

import java.util.List;

import org.springframework.data.domain.Page;
import com.phucx.shop.model.Supplier;

public interface SupplierService {
    public Supplier getSupplier(int supplierID);
    public List<Supplier> getSuppliers();
    public Page<Supplier> getSuppliers(int pageNumber, int pageSize);
}
