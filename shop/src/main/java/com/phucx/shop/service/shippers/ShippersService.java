package com.phucx.shop.service.shippers;

import java.util.List;

import org.springframework.data.domain.Page;
import com.phucx.shop.model.Shippers;

public interface ShippersService {
    public Shippers getShipper(int shipperID);
    public List<Shippers> getShippers();
    public Page<Shippers> getShippers(int pageNumber, int pageSize);
}
