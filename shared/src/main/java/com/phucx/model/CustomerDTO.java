package com.phucx.model;

import java.util.List;

public class CustomerDTO extends UserDTO{
    private String customerID;
    private List<String> customerIDs;
    private String contactName;
    public CustomerDTO(String username, String userID, List<String> userIDs, String customerID,
            List<String> customerIDs, String contactName) {
        super(username, userID, userIDs);
        this.customerID = customerID;
        this.customerIDs = customerIDs;
        this.contactName = contactName;
    }
    public CustomerDTO(String username, String userID, String customerID, List<String> customerIDs,
            String contactName) {
        super(username, userID);
        this.customerID = customerID;
        this.customerIDs = customerIDs;
        this.contactName = contactName;
    }
    public CustomerDTO(String customerID, List<String> customerIDs, String contactName) {
        this.customerID = customerID;
        this.customerIDs = customerIDs;
        this.contactName = contactName;
    }
    public CustomerDTO() {
    }
    public CustomerDTO(String username, String userID, String customerID, List<String> customerIDs) {
        super(username, userID);
        this.customerID = customerID;
        this.customerIDs = customerIDs;
    }
    public CustomerDTO(String customerID, List<String> customerIDs) {
        this.customerID = customerID;
        this.customerIDs = customerIDs;
    }
    public String getCustomerID() {
        return customerID;
    }
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
    public List<String> getCustomerIDs() {
        return customerIDs;
    }
    public void setCustomerIDs(List<String> customerIDs) {
        this.customerIDs = customerIDs;
    }
    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
