package com.sm.journalApp.controllers;

import com.sm.journalApp.entities.User;
import com.sm.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/get-all-users")
    public List<User> getAllUsers(){
        return userService.getAll();
    }

    @PostMapping("/create-user")
    public void createUser(@RequestBody User user){
        userService.saveUser(user);
    }

    @PutMapping("/update-user/{userName}")
    public ResponseEntity<?> updateUser(@RequestBody User user , @PathVariable(value = "userName") String userName){
        User userInDb = userService.findByUserName(userName);
        if (userInDb != null){
            userInDb.setUserName(user.getUserName());
            userInDb.setPassWord(user.getPassWord());
            userService.saveUser(userInDb);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
