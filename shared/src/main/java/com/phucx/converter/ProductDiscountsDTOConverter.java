package com.phucx.converter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.phucx.model.ProductDiscountsDTO;

public class ProductDiscountsDTOConverter {
    public static List<ProductDiscountsDTO> castProductDiscountDTOs(List<LinkedHashMap<String, Object>> objects){
        return objects.stream().map(object ->{
            ProductDiscountsDTO dto = new ProductDiscountsDTO();
            if (object.containsKey("productID")) {
                dto.setProductID((Integer) object.get("productID"));
            }
            if (object.containsKey("quantity")) {
                dto.setQuantity((Integer) object.get("quantity"));
            }
            if (object.containsKey("discountIDs")) {
                dto.setDiscountIDs((List<String>) object.get("discountIDs"));
            }
            if (object.containsKey("appliedDate")) {
                dto.setAppliedDate((String) object.get("appliedDate"));
            }
        return dto;
        }).collect(Collectors.toList());
    }

    public static ProductDiscountsDTO castProductDiscountDTO(LinkedHashMap<String, Object> object){
        ProductDiscountsDTO dto = new ProductDiscountsDTO();
            if (object.containsKey("productID")) {
                dto.setProductID((Integer) object.get("productID"));
            }
            if (object.containsKey("quantity")) {
                dto.setQuantity((Integer) object.get("quantity"));
            }
            if (object.containsKey("discountIDs")) {
                dto.setDiscountIDs((List<String>) object.get("discountIDs"));
            }
            if (object.containsKey("appliedDate")) {
                dto.setAppliedDate((String) object.get("appliedDate"));
            }
        return dto;
    }
}
