package com.phucx.model;

import java.util.List;

public class EmployeeDTO extends UserDTO{
    private String employeeID;
    private List<String> employeeIDs;

    public EmployeeDTO(){
        
    }


    public EmployeeDTO(String username, String userID, String employeeID, List<String> employeeIDs) {
        super(username, userID);
        this.employeeID = employeeID;
        this.employeeIDs = employeeIDs;
    }
    public EmployeeDTO(String employeeID, List<String> employeeIDs) {
        this.employeeID = employeeID;
        this.employeeIDs = employeeIDs;
    }
    public String getEmployeeID() {
        return employeeID;
    }
    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }
    public List<String> getEmployeeIDs() {
        return employeeIDs;
    }
    public void setEmployeeIDs(List<String> employeeIDs) {
        this.employeeIDs = employeeIDs;
    }
}
