package com.phucx.shop.service.jsonFilter;

import java.util.Set;

import org.springframework.http.converter.json.MappingJacksonValue;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public interface JsonFilterService {
    public MappingJacksonValue serializeAll(
        String filterName, Object data, SimpleFilterProvider filterProvider);
    public MappingJacksonValue serializeAllExcept(String filterName, Set<String> properties, 
        Object data, SimpleFilterProvider filterProvider);
    public MappingJacksonValue filterOutAll(String filterName, Object data, 
        SimpleFilterProvider filterProvider);
    public MappingJacksonValue filterOutAllExcept(String filterName, Set<String> properties, 
        Object data, SimpleFilterProvider filterProvider);
    
}
