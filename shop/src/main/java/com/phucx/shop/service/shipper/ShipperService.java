package com.phucx.shop.service.shipper;

import java.util.List;

import org.springframework.data.domain.Page;
import com.phucx.shop.model.Shipper;

public interface ShipperService {
    public Shipper getShipper(int shipperID);
    public List<Shipper> getShippers();
    public Page<Shipper> getShippers(int pageNumber, int pageSize);
}
