package com.spotify.api.controllers;

import com.spotify.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@CrossOrigin
public class AuthController {


    @Autowired
    AuthService service;
    @PostMapping
    public String authorize(@RequestParam("code") String code){
        return service.authorize(code);
    }


}