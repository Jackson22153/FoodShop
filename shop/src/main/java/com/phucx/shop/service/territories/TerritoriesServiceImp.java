package com.phucx.shop.service.territories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.Territories;
import com.phucx.shop.repository.TerritoriesRepository;

@Service
public class TerritoriesServiceImp implements TerritoriesService{
    @Autowired
    private TerritoriesRepository territoriesRepository;

    public List<Territories> getTerritories(){
        return territoriesRepository.findAll();
    }

    public Page<Territories> getTerritories(int pageNumber, int pageSize){
        Pageable page = PageRequest.of(pageNumber, pageSize);
        var result = territoriesRepository.findAll(page);
        return result;
    }

    @Override
    public Territories getTerritory(String territoryID) {
        @SuppressWarnings("null")
        var category = territoriesRepository.findById(territoryID);
        if(category.isPresent()) return category.get();
        return new Territories();
    }
}
