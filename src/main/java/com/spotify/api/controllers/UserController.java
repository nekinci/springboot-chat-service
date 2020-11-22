package com.spotify.api.controllers;

import com.spotify.api.config.JwtRequestFilter;
import com.spotify.api.models.User;
import com.spotify.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/users")
@RestController
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
