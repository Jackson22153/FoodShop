package com.phucx.account.service.userRole;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.account.model.Role;
import com.phucx.account.model.UserRole;
import com.phucx.account.model.User;
import com.phucx.account.model.UserInfo;
import com.phucx.account.repository.UserRoleRepository;
import com.phucx.account.service.role.RoleService;
import com.phucx.account.service.user.UserService;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserRoleerviceImp implements UserRoleService{
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

	@Override
	public List<UserRole> getUserRole(String userID) {
		List<UserRole> userRole = userRoleRepository.findByUserID(userID);
        if(userRole!=null) return userRole;
        return new ArrayList<>();
	}
	@Override
	public boolean assignUserRoles(UserInfo user) {
        log.info("assignUserRoles({})", user.toString());
        // check input data 
        if(user.getRoles().size()>0){
            User fetchedUser = userService.getUser(user.getUser().getUsername());
            List<Integer> roleIDs = user.getRoles().stream()
                .map(Role::getRoleID)
                .collect(Collectors.toList());
            List<Role> fetchedRoles = roleService.getRoles(roleIDs);
            // convert roleID to string
            List<String> roleIDsStr = fetchedRoles.stream()
                .map(role -> String.valueOf(role.getRoleID()))
                .collect(Collectors.toList());
            // execute procedure 
            Boolean status = userRoleRepository.assignUserRoles(
                fetchedUser.getUsername(), 
                String.join(",", roleIDsStr));
            return status;
        }
        throw new NotFoundException("Does not found any roles for user " + user.getUser().getUserID());
	}
    
}
