package com.phucx.model;

public class ShippingProductDTO extends ProductDTO{
    private String encodedCartJson;

    public ShippingProductDTO(String encodedCartJson) {
        this.encodedCartJson = encodedCartJson;
    }

    public ShippingProductDTO() {
    }

    public String getEncodedCartJson() {
        return encodedCartJson;
    }

    public void setEncodedCartJson(String encodedCartJson) {
        this.encodedCartJson = encodedCartJson;
    }
}
