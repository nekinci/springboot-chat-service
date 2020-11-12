package com.spotify.api.service;
import com.spotify.api.models.UserToken;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spotify.api.constants.SpotifyAPI;

import java.io.IOException;
import java.net.URI;

@Service
public class AuthService {

    @Autowired
    SpotifyService spotifyService;
    public String authorize(String code){

        UserToken result = spotifyService.getToken(code);
        System.out.println(result.getToken());
        return result.getToken();

    }

}
