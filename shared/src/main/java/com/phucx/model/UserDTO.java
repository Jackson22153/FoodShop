package com.phucx.model;

import java.util.List;

public class UserDTO extends DataDTO{
    private String username;
    private String userID;
    private List<String> userIDs;
    public UserDTO(String username, String userID, List<String> userIDs) {
        this.username = username;
        this.userID = userID;
        this.userIDs = userIDs;
    }
    public UserDTO(String username, String userID) {
        this.username = username;
        this.userID = userID;
    }
    public UserDTO() {
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public List<String> getUserIDs() {
        return userIDs;
    }
    public void setUserIDs(List<String> userIDs) {
        this.userIDs = userIDs;
    }
}
