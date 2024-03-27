package com.phucx.account.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.account.model.Users;
import com.phucx.account.repository.UsersRepository;

@Service
public class UsersServiceImp implements UsersService {
    @Autowired
    private UsersRepository usersRepository;
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

    
}
