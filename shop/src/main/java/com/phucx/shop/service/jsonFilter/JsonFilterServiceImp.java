package com.phucx.shop.service.jsonFilter;

import java.util.Set;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Service
public class JsonFilterServiceImp implements JsonFilterService{
    @Override
    @SuppressWarnings("null")
    public MappingJacksonValue serializeAll(String filterName, Object data, SimpleFilterProvider filterProvider) {
        filterProvider.addFilter(filterName, SimpleBeanPropertyFilter.serializeAll());
        MappingJacksonValue result = new MappingJacksonValue(data);
        result.setFilters(filterProvider);
        return result;
    }

    @Override
    @SuppressWarnings("null")
    public MappingJacksonValue filterOutAllExcept(String filterName, Set<String> properties, Object data, SimpleFilterProvider filterProvider) {
        filterProvider.addFilter(filterName, SimpleBeanPropertyFilter.filterOutAllExcept(properties));
        MappingJacksonValue result = new MappingJacksonValue(data);
        result.setFilters(filterProvider);
        return result;
    }

    @Override
    @SuppressWarnings("null")
    public MappingJacksonValue serializeAllExcept(String filterName, Set<String> properties, Object data, SimpleFilterProvider filterProvider) {
        filterProvider.addFilter(filterName, SimpleBeanPropertyFilter.serializeAllExcept(properties));
        MappingJacksonValue result = new MappingJacksonValue(data);
        result.setFilters(filterProvider);
        return result;
    }

    @Override
    @SuppressWarnings("null")
    public MappingJacksonValue filterOutAll(String filterName, Object data, SimpleFilterProvider filterProvider) {
        filterProvider.addFilter(filterName, SimpleBeanPropertyFilter.filterOutAll());
        MappingJacksonValue result = new MappingJacksonValue(data);
        result.setFilters(filterProvider);
        return result;
    }
    
}
