package com.phucx.model;

import java.util.List;

public class ShipperDTO extends DataDTO{
    private Integer shipperID;
    private List<Integer> shipperIDs;
    public ShipperDTO(Integer shipperID, List<Integer> shipperIDs) {
        this.shipperID = shipperID;
        this.shipperIDs = shipperIDs;
    }
    public ShipperDTO() {
    }
    public Integer getShipperID() {
        return shipperID;
    }
    public void setShipperID(Integer shipperID) {
        this.shipperID = shipperID;
    }
    public List<Integer> getShipperIDs() {
        return shipperIDs;
    }
    public void setShipperIDs(List<Integer> shipperIDs) {
        this.shipperIDs = shipperIDs;
    }
}   
