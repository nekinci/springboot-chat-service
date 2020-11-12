package com.spotify.api.service;
import com.spotify.api.models.User;
import com.spotify.api.models.UserToken;
import com.spotify.api.repositories.UserRepository;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;
import com.spotify.api.constants.SpotifyAPI;

import java.io.IOException;
import java.net.URI;

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
        userService.createOrUpdate(user, result);
        System.out.println(result.getToken());
        return result.getToken();

    }

}
