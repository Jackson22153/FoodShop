package com.phucx.keycloakmanagement.service.keycloak;

import java.util.List;

import com.phucx.keycloakmanagement.exception.NotFoundException;
import com.phucx.keycloakmanagement.model.User;

public interface KeycloakService {
    public User getUser(String userID) throws NotFoundException;
    public List<User> getUsers(List<String> userIDs);
    public List<User> getUsersByRole(String roleName, Integer pageNumber, Integer pageSize);
    public List<User> searchUsersByUsernameAndRole(String username, String roleName, Integer pageNumber, Integer pageSize);
    public List<User> searchUsersByFirstNameAndRole(String firstName, String roleName, Integer pageNumber, Integer pageSize);
    public List<User> searchUsersByLastNameAndRole(String lastName, String roleName, Integer pageNumber, Integer pageSize);
    public List<User> searchUsersByEmailAndRole(String email, String roleName, Integer pageNumber, Integer pageSize);
    
}
