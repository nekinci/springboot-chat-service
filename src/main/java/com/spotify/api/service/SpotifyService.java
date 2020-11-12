package com.spotify.api.service;

import com.spotify.api.constants.SpotifyAPI;
import com.spotify.api.models.UserToken;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;

@Service
public class SpotifyService{

    private final SpotifyApi spotifyApi;
    private AuthorizationCodeRequest authorizationCodeRequest;

    SpotifyService(){
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(SpotifyAPI.CLIENT_ID)
                .setClientSecret(SpotifyAPI.CLIENT_SECRET)
                .setRedirectUri(URI.create("http://localhost:4200/login"))
                .build();

    }
    public UserToken getToken(String code){
        this.authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
        try {
            AuthorizationCodeCredentials credentials = this.authorizationCodeRequest.execute();
            spotifyApi.setAccessToken(credentials.getAccessToken());
            spotifyApi.setRefreshToken(credentials.getRefreshToken());
            return _mapping(credentials);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean validateToken(){
        return false;
    }
    private UserToken _mapping(AuthorizationCodeCredentials credentials){
        UserToken userToken = new UserToken();
        userToken.setToken(credentials.getAccessToken());
        userToken.setRefreshToken(credentials.getRefreshToken());
        userToken.setExpiresIn(credentials.getExpiresIn());
        return userToken;

    }
}
