package com.phucx.account.service.users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.phucx.account.config.WebConfig;
import com.phucx.account.model.Roles;
import com.phucx.account.model.UserRoles;
import com.phucx.account.model.UserRolesUtils;
import com.phucx.account.model.Users;
import com.phucx.account.repository.UserRolesRepository;
import com.phucx.account.repository.UsersRepository;

@Service
public class UsersServiceImp implements UsersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRolesRepository userRolesRepository;
    @Override
    public Users getUser(String username) {
        Users user = usersRepository.findByUsername(username);
        if(user!=null){
            return user;
        }
        return null;
    }
    @Override
    public Users getUserByID(String userID) {
        var opUser = usersRepository.findById(userID);
        if(opUser.isPresent())
            return opUser.get();
        else return null;
    }
    @Override
    public boolean createUser(Users user) {
        try {
            String username = user.getUsername();
            String password = user.getPassword();
            Users existedUser = this.getUser(username);
            if(existedUser==null){
                String hashedPassword = passwordEncoder.encode(password);
                Users newUser = new Users(user.getUserID(), username, hashedPassword);
                Users check = usersRepository.save(newUser);
                if(check!=null)
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public UserRolesUtils getUserRoles(String userID) {
        List<UserRoles> userRoles = userRolesRepository.findByUserID(userID);
        // List<UserRoles> userRoles = userRolesService.getUserRoles(userID);
        if(userRoles.size()>0){
            UserRoles firstEntity = userRoles.get(0);
            Users user = new Users(firstEntity.getUserID(), firstEntity.getUsername(), null);
            List<Roles> roles = new ArrayList<>();
            for (UserRoles userRole : userRoles) {
                roles.add(new Roles(null, userRole.getRoleName()));
            }
            return new UserRolesUtils(user, roles);
        }
        return null;
    }
    @Override
    public String getUsername(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString(WebConfig.PREFERRED_USERNAME);
        return username;
    }
    @Override
    public String getUserID(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userID = jwt.getSubject();
        return userID;
    }
}
