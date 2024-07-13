package com.phucx.keycloakmanagement.service.keycloak;

import java.util.List;
import java.util.stream.Collectors;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.keycloakmanagement.constant.KeycloakConstant;
import com.phucx.keycloakmanagement.exception.NotFoundException;
import com.phucx.keycloakmanagement.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KeycloakServiceImp implements KeycloakService{
    @Autowired
    private RealmResource realmResource;

    @Override
    public List<User> getUsers(List<String> userIDs){
        log.info("getUsers({})", userIDs);
        List<User> users = userIDs.stream().flatMap(userID -> realmResource.users()
            .searchByAttributes(KeycloakConstant.USER_ID + ":"+userID).stream())
            .map(user -> new User(user.getAttributes().get(KeycloakConstant.USER_ID).get(0), 
                user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName()))
            .collect(Collectors.toList());
        return users;
    }

    @Override
    public User getUser(String userID) throws NotFoundException {
        log.info("getUser({})", userID);
        List<UserRepresentation> users =realmResource.users().searchByAttributes(KeycloakConstant.USER_ID + ":" + userID);
        if(users!=null && users.isEmpty()) throw new NotFoundException("User " + userID + " does not found");
        UserRepresentation user = users.get(0);
        return new User(user.getAttributes().get(KeycloakConstant.USER_ID).get(0), 
            user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName());
    }

    @Override
    public List<User> getUsersByRole(String roleName, Integer pageNumber, Integer pageSize) {
        log.info("getUsersByRole(rolename={})", roleName);
        List<User> users = realmResource.roles().get(roleName)
            .getUserMembers(pageNumber, pageSize).stream()
            .map(user -> new User(user.getAttributes().get(KeycloakConstant.USER_ID).get(0), 
                user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName()))
            .collect(Collectors.toList());

        

        return users;
    }

    private List<User> searchUsersByAttributeAndRole(String attributeName, String attributeValue, String roleName, Integer pageNumber, Integer pageSize){
        log.info("searchUsersByAttributeAndRole(attributeName={}, attributeValue={}, roleName={}, pageNumber={}, pageSize={})", 
            attributeName, attributeValue, roleName, pageNumber, pageSize);    
        List<User> users = realmResource.users().searchByAttributes(pageNumber, pageSize, true, false, "role:" +roleName+" " +  attributeName + ":" + attributeValue).stream()
            .map(user -> new User(user.getAttributes().get(KeycloakConstant.USER_ID).get(0), 
                user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName()))
            .collect(Collectors.toList());
        return users;
    }

    @Override
    public List<User> searchUsersByUsernameAndRole(String username, String roleName, Integer pageNumber, Integer pageSize) {
        log.info("searchUsersByUsernameAndRole(username={}, roleName={}, pageNumber={}, pageSize={})", 
            username, roleName, pageNumber, pageSize);    
        List<User> users = this.searchUsersByAttributeAndRole(KeycloakConstant.USERNAME, username, roleName, pageNumber, pageSize);
        return users;
    }

    @Override
    public List<User> searchUsersByFirstNameAndRole(String firstName, String roleName, Integer pageNumber,
            Integer pageSize) {
        log.info("searchUsersByFirstNameAndRole(firstName={}, roleName={}, pageNumber={}, pageSize={})", 
            firstName, roleName, pageNumber, pageSize);    
        List<User> users = this.searchUsersByAttributeAndRole(KeycloakConstant.FIRST_NAME, firstName, roleName, pageNumber, pageSize);
        return users;
    }

    @Override
    public List<User> searchUsersByLastNameAndRole(String lastName, String roleName, Integer pageNumber,
            Integer pageSize) {
        log.info("searchUsersByLastNameAndRole(lastName={}, roleName={}, pageNumber={}, pageSize={})", 
            lastName, roleName, pageNumber, pageSize);    
        List<User> users = this.searchUsersByAttributeAndRole(KeycloakConstant.LAST_NAME, lastName, roleName, pageNumber, pageSize);
        return users;
    }

    @Override
    public List<User> searchUsersByEmailAndRole(String email, String roleName, Integer pageNumber, Integer pageSize) {
        log.info("searchUsersByEmailAndRole(email={}, roleName={}, pageNumber={}, pageSize={})", 
            email, roleName, pageNumber, pageSize);    
        List<User> users = this.searchUsersByAttributeAndRole(KeycloakConstant.EMAIL, email, roleName, pageNumber, pageSize);
        return users;
    }
}
