package com.phucx.shop.service.territories;

import java.util.List;

import org.springframework.data.domain.Page;
import com.phucx.shop.model.Territories;

public interface TerritoriesService {
    public List<Territories> getTerritories();
    public Page<Territories> getTerritories(int pageNumber, int pageSize);
    public Territories getTerritory(String territoryID);
} 
