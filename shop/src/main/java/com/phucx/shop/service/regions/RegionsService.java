package com.phucx.shop.service.regions;

import java.util.List;

import org.springframework.data.domain.Page;
import com.phucx.shop.model.Region;

public interface RegionsService {
    public Region getRegion(int regionID);
    public List<Region> getRegions();
    public Page<Region> getRegions(int pageNumber, int pageSize);
}
