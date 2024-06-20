package com.phucx.account.service.shipper;

import com.phucx.account.exception.ShipperNotFoundException;
import com.phucx.account.model.Shipper;

public interface ShipperService {
    public Shipper getShipperByID(Integer shipperID) throws ShipperNotFoundException;
}
