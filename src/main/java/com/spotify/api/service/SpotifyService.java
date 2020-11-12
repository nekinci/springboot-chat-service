package com.spotify.api.service;

import com.spotify.api.constants.SpotifyAPI;
import com.spotify.api.models.UserToken;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import com.wrapper.spotify.model_objects.specification.User;
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
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User me(UserToken token){
        spotifyApi
                .setAccessToken(token.getToken());
        GetCurrentUsersProfileRequest _meReq = spotifyApi.getCurrentUsersProfile().build();
        try {
            return _meReq.execute();

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    private UserToken _mapping(AuthorizationCodeCredentials credentials){
        UserToken userToken = new UserToken();
        userToken.setToken(credentials.getAccessToken());
        userToken.setRefreshToken(credentials.getRefreshToken());
        userToken.setExpiresIn(credentials.getExpiresIn());
        return userToken;

    }
}
