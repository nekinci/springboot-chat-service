package com.spotify.api.service;
import com.spotify.api.models.User;
import com.spotify.api.models.UserToken;
import com.spotify.api.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    SpotifyService spotifyService;
    @Autowired
    UserService userService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    public String authorize(String code){

        UserToken result = spotifyService.getToken(code);
        if (result == null)
            return "NOT AUTHORIZED";
        com.wrapper.spotify.model_objects.specification.User user = spotifyService.me(result);
        if(user == null)
            return "NOT FOUND";
        User _user = userService.createUserOrUpdateToken(user, result);
        String token = jwtTokenUtil.generateToken(_user);
        System.out.println(result.getToken());
        System.out.println("\n\n"+token);
        System.out.println(jwtTokenUtil.getExpirationDateFromToken(token));
        return result.getToken();

    }

}
