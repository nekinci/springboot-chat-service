package com.spotify.api.controllers;

import com.spotify.api.models.User;
import com.spotify.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    UserService service;
    @PutMapping
    public User update(@RequestBody() User user){
        return service.updateUser(user);
    }
}
