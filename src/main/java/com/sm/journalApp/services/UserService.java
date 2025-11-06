package com.sm.journalApp.services;
//Controller -> Service -> Repository -> Database

import com.sm.journalApp.entities.User;
import com.sm.journalApp.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public void saveUser(User user){
        userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public Optional<User> getById(ObjectId myId){

        return  userRepository.findById(myId);
    }

    public void deleteById(ObjectId myId){

        userRepository.deleteById(myId);
    }

    public User findByUserName(String userName){
        return userRepository.findUserByUserName(userName);
    }

}


