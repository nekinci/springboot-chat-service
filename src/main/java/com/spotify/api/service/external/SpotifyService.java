package com.spotify.api.service.external;

import com.spotify.api.core.constants.SpotifyAPI;
import com.spotify.api.model.valueObject.UserToken;
import com.spotify.api.util.JwtTokenUtil;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import com.wrapper.spotify.model_objects.specification.User;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

@Service
public class SpotifyService{

    private final SpotifyApi spotifyApi;
    private AuthorizationCodeRequest authorizationCodeRequest;

    SpotifyService(){
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(SpotifyAPI.CLIENT_ID)
                .setClientSecret(SpotifyAPI.CLIENT_SECRET)
                .setRedirectUri(URI.create(SpotifyAPI.REDIRECT_URI))
                .build();

    }

    public Track currentPlaying(UserToken token){
        token = ifRequireRefreshToken(token);
        spotifyApi.setAccessToken(token.getToken());
        GetUsersCurrentlyPlayingTrackRequest currentlyPlaying = spotifyApi.getUsersCurrentlyPlayingTrack().build();
        try {

            Track track = (Track) currentlyPlaying.execute().getItem();
            return track;

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
            return null;
        }
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

    public UserToken refreshToken(UserToken token){
        AuthorizationCodeRefreshRequest refreshRequest = spotifyApi.authorizationCodeRefresh(SpotifyAPI.CLIENT_ID, SpotifyAPI.CLIENT_SECRET, token.getRefreshToken()).build();
        try {
            AuthorizationCodeCredentials credentials = refreshRequest.execute();
            UserToken _token = new UserToken();
            _token.setToken(credentials.getAccessToken());
            _token.setExpiresIn(credentials.getExpiresIn());
            _token.setLastChangeAt(LocalDateTime.now());
            return _token;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserToken ifRequireRefreshToken(UserToken token){
        if(!JwtTokenUtil.isAlive(token)) return refreshToken(token);
        return token;
    }

    public User me(UserToken token){
        spotifyApi.setAccessToken(token.getToken());
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


