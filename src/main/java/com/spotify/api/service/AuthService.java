package com.spotify.api.service;
import com.spotify.api.models.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    SpotifyService spotifyService;
    @Autowired
    UserService userService;
    public String authorize(String code){

        UserToken result = spotifyService.getToken(code);
        if (result == null)
            return "NOT AUTHORIZED";
        com.wrapper.spotify.model_objects.specification.User user = spotifyService.me(result);
        if(user == null)
            return "NOT FOUND";
        userService.createUserOrUpdateToken(user, result);
        System.out.println(result.getToken());
        return result.getToken();

    }

}
