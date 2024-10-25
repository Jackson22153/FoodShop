package com.phucx.payment.service.shipping.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.payment.config.GHNProperties;
import com.phucx.payment.model.District;
import com.phucx.payment.model.Location;
import com.phucx.payment.model.LocationBuilder;
import com.phucx.payment.model.Province;
import com.phucx.payment.model.ShippingInfo;
import com.phucx.payment.model.ShippingInfoBuilder;
import com.phucx.payment.model.ShippingProduct;
import com.phucx.payment.model.ShippingResponse;
import com.phucx.payment.model.StoreLocation;
import com.phucx.payment.model.Ward;
import com.phucx.payment.repository.StoreLocationRepository;
import com.phucx.payment.service.currency.CurrencyService;
import com.phucx.payment.service.product.ProductService;
import com.phucx.payment.service.shipping.ShippingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShippingServiceImp implements ShippingService{
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GHNProperties shippingProperties;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private ProductService productService;
    @Autowired
    private StoreLocationRepository storeLocationRepository;

    private Integer getShippingCost(ShippingInfo shippingInfo) {
        log.info("getShippingCost(shippingInfo={})", shippingInfo);
        // create header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", shippingProperties.getToken());
        headers.set("shop_id", shippingProperties.getShopId());

        // create body
        Map<String, Object> params = new HashMap<>();

        params.put("service_id", shippingInfo.getServiceId());
        params.put("insurance_value", shippingInfo.getInsuranceValue());
        params.put("coupon", shippingInfo.getCoupon());
        params.put("to_ward_code", shippingInfo.getToWardCode());
        params.put("to_district_id", shippingInfo.getToDistrictId());
        params.put("from_district_id", shippingInfo.getFromDistrictId());
        params.put("height", shippingInfo.getHeight());
        params.put("length", shippingInfo.getLength());
        params.put("weight", shippingInfo.getWeight());
        params.put("width", shippingInfo.getWidth());

        log.info("Params: {}", params);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);
    
        ResponseEntity<Map> response = restTemplate.postForEntity(
            shippingProperties.getFeeUrl(), entity, Map.class);
        if(!response.getStatusCode().is2xxSuccessful()) return null;
        Map<String, Object> body = response.getBody();
        Map<String, Object> data = (Map<String, Object>) body.get("data");
        Integer total = (Integer) data.get("total");
        return total;
    }

    @Override
    public List<Province> getProvinces() {
        log.info("getProvinces()");
        // create header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", shippingProperties.getToken());
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);
    
        ResponseEntity<Map> response = restTemplate.postForEntity(
            shippingProperties.getProvinceUrl(), entity, Map.class);
        if(!response.getStatusCode().is2xxSuccessful()) return null;
        Map<String, Object> body = response.getBody();
        List<Province> data =  (List<Province>) body.get("data");
        
        return data;
    }

    @Override
    public List<District> getDistricts(Integer provinceID) {
        log.info("getDistricts(provinceID={})", provinceID);
        // create header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", shippingProperties.getToken());
        // create body
        Map<String, Object> params = new HashMap<>();
        params.put("province_id", provinceID);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);
    
        ResponseEntity<Map> response = restTemplate.postForEntity(
            shippingProperties.getDistrictUrl(), entity, Map.class);
        if(!response.getStatusCode().is2xxSuccessful()) return null;
        Map<String, Object> body = response.getBody();
        List<District> data = (List<District> ) body.get("data");
        return data;
    }

    @Override
    public List<Ward> getWards(Integer districtID) {
        log.info("getWards(districtID={})", districtID);
        // create header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", shippingProperties.getToken());
        // create body
        Map<String, Object> params = new HashMap<>();
        params.put("district_id", districtID);
        // request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);
    
        ResponseEntity<Map> response = restTemplate.postForEntity(
            shippingProperties.getWardUrl(), entity, Map.class);
        if(!response.getStatusCode().is2xxSuccessful()) return null;
        Map<String, Object> body = response.getBody();
        List<Ward> data = (List<Ward> ) body.get("data");
        return data;
    }

    private List<ShippingService> getServices(Integer fromDistrictId, Integer toDistrictId){
        log.info("getServices(fromDistrictId={}, toDistrictId={})", fromDistrictId, toDistrictId);
        // create header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", shippingProperties.getToken());
        // create body
        Map<String, Object> params = new HashMap<>();
        params.put("shop_id", Integer.valueOf(shippingProperties.getShopId()));
        params.put("from_district", fromDistrictId);
        params.put("to_district", toDistrictId);
        // request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
            shippingProperties.getServicesUrl(), entity, Map.class);
        if(!response.getStatusCode().is2xxSuccessful()) return null;
        Map<String, Object> body = response.getBody();
        List<ShippingService> data = (List<ShippingService>) body.get("data");
        return data;
    }

    @Override
    public Location getStoreLocation() {
        log.info("getStoreLocation()");
        try {
            List<StoreLocation> storeLocations = storeLocationRepository.findAll();
            StoreLocation storeLocation = storeLocations.get(0);
            log.info("Location: {}", storeLocation);

            Integer provinceID = null;
            Integer districtID = null;
            String wardCode = null;
            List<?> provinces = this.getProvinces();
            for (Object object : provinces) {
                Map<String, Object> province = (Map<String, Object>) object;
                if(province.get("ProvinceName").toString().equalsIgnoreCase(storeLocation.getCity())){
                    provinceID = (Integer) province.get("ProvinceID");
                }
            }
            List<?> districts = this.getDistricts(provinceID);
            for (Object object : districts) {
                Map<String, Object> district = (Map<String, Object>) object;

                if(district.get("DistrictName").toString().equalsIgnoreCase(storeLocation.getDistrict())){
                    districtID = (Integer) district.get("DistrictID");
                }
            }
            List<?> wards = this.getWards(districtID);
            for (Object object : wards) {
                Map<String, Object> ward = (Map<String, Object>) object;
                if(ward.get("WardName").toString().equalsIgnoreCase(storeLocation.getWard())){
                    wardCode = ward.get("WardCode").toString();
                }
            }

            Location location = new LocationBuilder()
                .withWard(storeLocation.getWard())
                .withDistrict(storeLocation.getWard())
                .withCity(storeLocation.getCity())
                .withWardCode(wardCode)
                .withDistrictId(districtID)
                .withCityId(provinceID)
                .build();
            return location;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }
        
    }

    @Override
    public ShippingResponse costEstimate(Integer userCityID, Integer userDistrictID, 
        String userWardCode, String userID, String encodedCartJson) throws JsonProcessingException{
        log.info("costEstimate(userCityID={}, userDistrictID={}, userWardCode={}, userID={}, encodedCartJson={})", 
            userCityID, userDistrictID, userWardCode, userID, encodedCartJson);
        // get store location
        Location storeLocation = this.getStoreLocation();
        List<?> shippingServices = this.getServices(
            storeLocation.getDistrictId(), userDistrictID);
        // get user's order
        ShippingProduct shippingProduct = productService
            .getShippingProduct(encodedCartJson);
        // exchange total price
        String price = currencyService.exchangeRateFromUSDToVND(
            shippingProduct.getTotalPrice().doubleValue());
        // get shipping services
        List<Map<String, Object>> services = shippingServices.stream()
            .map(service -> (Map<String, Object>) service)
            .collect(Collectors.toList());
        Integer serviceID = (Integer)services.get(0).get("service_id");
        // get shipping cost
        ShippingInfo shippingInfo = new ShippingInfoBuilder()
            .withServiceId(serviceID)
            .withInsuranceValue(Integer.valueOf(price))
            .withFromDistrictId(storeLocation.getDistrictId())
            .withToDistrictId(userDistrictID)
            .withToWardCode(userWardCode)
            .withHeight(shippingProduct.getTotalHeight())
            .withWeight(shippingProduct.getTotalWeight())
            .withLength(shippingProduct.getTotalLength())
            .withWidth(shippingProduct.getTotalWidth())
            .build();
        Integer total = this.getShippingCost(shippingInfo);
        String totalUSD = currencyService.exchangeRateFromVNDToUSD(total.longValue());
        ShippingResponse response = new ShippingResponse(Double.valueOf(totalUSD));
        return response;
    }


    
}
