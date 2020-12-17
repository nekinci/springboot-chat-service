package com.spotify.api.controller;

import com.spotify.api.dto.TokenDto;
import com.spotify.api.service.authentication.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@CrossOrigin
public class AuthController {


    @Autowired
    AuthService service;
    @PostMapping
    public TokenDto authorize(@RequestParam("code") String code){
        return service.authorize(code);
    }


}
