package com.phucx.payment.service.shipping;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.payment.exception.NotFoundException;
import com.phucx.payment.model.District;
import com.phucx.payment.model.Location;
import com.phucx.payment.model.Province;
import com.phucx.payment.model.ShippingResponse;
import com.phucx.payment.model.Ward;

public interface ShippingService {
    // estimate shipping cost
    public ShippingResponse costEstimate(Integer userCityID, Integer userDistrictID, String userWardCode, String userID, String encodedCartJson) 
        throws JsonProcessingException;
    public List<Province> getProvinces();
    public List<District> getDistricts(Integer provinceID);
    public List<Ward> getWards(Integer districtID);
    public Location getStoreLocation() throws NotFoundException;
}
