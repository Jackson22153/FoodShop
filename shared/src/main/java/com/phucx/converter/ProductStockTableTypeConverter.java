package com.phucx.converter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.phucx.model.ProductStockTableType;

public class ProductStockTableTypeConverter {
    public static ProductStockTableType productStockTableTypeConverter(LinkedHashMap<String, Object> map) {
        ProductStockTableType stockTable = new ProductStockTableType();
        
        if (map.containsKey("productID")) {
            stockTable.setProductID((Integer) map.get("productID"));
        }
        if (map.containsKey("unitsInStock")) {
            stockTable.setUnitsInStock((Integer) map.get("unitsInStock"));
        }
        
        return stockTable;
    }

    public static List<ProductStockTableType> productStockTableTypesConverter(List<LinkedHashMap<String, Object>> maps) {
        return maps.stream().map(map -> {
            ProductStockTableType stockTable = new ProductStockTableType();
            
            if (map.containsKey("productID")) {
                stockTable.setProductID((Integer) map.get("productID"));
            }
            if (map.containsKey("unitsInStock")) {
                stockTable.setUnitsInStock((Integer) map.get("unitsInStock"));
            }
            
            return stockTable;
        }).collect(Collectors.toList());
    }
}
