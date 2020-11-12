package com.spotify.api.service;

import com.spotify.api.models.User;
import com.spotify.api.models.UserToken;
import com.spotify.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public void createOrUpdate(com.wrapper.spotify.model_objects.specification.User user, UserToken token){
        User _user = userRepository.findBySpotifyId(user.getId());
        if(_user == null){
            String[] fullName = user.getDisplayName().split(" ");
            fullName[0] = fullName[0] == null ? "" : fullName[0];
            fullName[1] = fullName[1] == null ? "" : fullName[1];
            _user = User.builder()
                    .name(fullName[0])
                    .surname(fullName[1])
                    .email(user.getEmail())
                    .spotifyId(user.getId())
                    .userToken(token)
                    .build();

        }
        else {
            UserToken userToken = _user.getUserToken();
            userToken.setToken(token.getToken());
            userToken.setRefreshToken(token.getRefreshToken());
            _user.setUserToken(userToken);
        }

        userRepository.save(_user);
    }


}
