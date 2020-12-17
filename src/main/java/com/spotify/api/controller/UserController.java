package com.spotify.api.controller;

import com.spotify.api.model.User;
import com.spotify.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
@CrossOrigin
public class UserController {

    @Autowired
    UserService service;
    @PutMapping
    public User update(@RequestBody() User user){
        return service.updateUser(user);
    }

    @GetMapping("/me")
    public User me(){
        return service.me();
    }

    @PostMapping("/lastSeen")
    public boolean setLastSeen(){
        service.setLastSeen();
        return true;
    }
}
