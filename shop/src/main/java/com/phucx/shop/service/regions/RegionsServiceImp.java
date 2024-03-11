package com.phucx.shop.service.regions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.Region;
import com.phucx.shop.repository.RegionRepository;

@Service
public class RegionsServiceImp implements RegionsService{
    @Autowired
    private RegionRepository regionRepository;
    @Override
    public List<Region> getRegions() {
        return regionRepository.findAll();
    }

    @Override
    public Page<Region> getRegions(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return regionRepository.findAll(page);
    }

    @Override
    public Region getRegion(int regionID) {
        var region = regionRepository.findById(regionID);
        if(region.isPresent()) return region.get();
        return new Region();
    }
    
}
