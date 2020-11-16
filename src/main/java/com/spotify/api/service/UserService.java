package com.spotify.api.service;

import com.spotify.api.models.User;
import com.spotify.api.models.UserToken;
import com.spotify.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public User createUserOrUpdateToken(com.wrapper.spotify.model_objects.specification.User user, UserToken token){
        User _user = userRepository.findBySpotifyId(user.getId());
        if(_user == null){
            List<String> fullName = new ArrayList<>(3);
            fullName.addAll(Arrays.asList(user.getDisplayName().split(" ")));
            fullName.set(0, fullName.get(0) == null ? "" : fullName.get(0));
            fullName.set(1, fullName.get(1) == null ? "" : fullName.get(1));
            token.setLastChangeAt(LocalDateTime.now());
            _user = User.builder()
                    .name(fullName.get(0))
                    .surname(fullName.get(1))
                    .email(user.getEmail())
                    .spotifyId(user.getId())
                    .userToken(token)
                    .createDate(new Date())
                    .build();

        }
        else {
            UserToken userToken = _user.getUserToken();
            userToken.setToken(token.getToken());
            userToken.setRefreshToken(token.getRefreshToken());
            userToken.setLastChangeAt(LocalDateTime.now());
            _user.setUserToken(userToken);
        }

        userRepository.save(_user);
        return _user;
    }

    public User updateUser(User user){
        if(user == null) return null;
        Optional<User> userData = userRepository.findById(user.getId());
        if (userData.isPresent()){
            User _user = userData.get();
            _user.setAvatar(user.getAvatar());
            _user.setBirthDate(user.getBirthDate());
            _user.setBirthOfPlace(user.getBirthOfPlace());
            _user.setDescription(user.getDescription());
            _user.setHobbyList(user.getHobbyList());
            _user.setLikedStyles(user.getLikedStyles());
            _user.setModifiedDate(new Date());
            userRepository.save(_user);
            return _user;
        }
        return null;
    }

    public User me(){
        return null;
    }


    public User getUserByEmail(String email){
        if(email == null) return null;
        return userRepository.findByEmailAndEndDateNull(email);
    }
}
