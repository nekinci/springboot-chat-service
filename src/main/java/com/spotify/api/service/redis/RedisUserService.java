package com.spotify.api.service.redis;

import com.spotify.api.model.redis.User;
import com.spotify.api.repository.UserRepository;
import com.spotify.api.repository.redis.RedisSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RedisUserService {
    @Autowired
    private RedisSessionRepository userRepository;
    @Autowired private UserRepository mongoUserRepository;
    public void add(String email) {
        User _user = userRepository.findById(email).orElse(null);
        com.spotify.api.model.User dbUser = mongoUserRepository.findByEmailAndEndDateNull(email);
        if(Objects.nonNull(_user) || Objects.isNull(dbUser)){
            return;
        }
        User user = User.builder()
                .firstConnectionAt(new Date())
                .email(email)
                .spotifyId(dbUser.getSpotifyId())
                .userToken(dbUser.getToken())
                .id(dbUser.getId())
                .spotifyToken(dbUser.getUserToken())
                .build();
        userRepository.save(user);
    }

    public boolean addDestination(String email, String destination){
        User user = userRepository.findById(email).orElse(null);
        if(Objects.nonNull(user)){
            List<String> destinations = user.getDestinations();
            if(Objects.isNull(destinations)){
                destinations = new ArrayList<>();
            }
            destinations.add(destination);
            user.setDestinations(destinations);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void remove(String email){
        userRepository.deleteById(email);
    }

    public void removeDestination(String email){
        User user = userRepository.findById(email).get();
        List<String> destinationList = user.getDestinations();
        if(Objects.nonNull(destinationList)){
            destinationList = destinationList.stream().filter((x) -> !x.startsWith("/topic")).collect(Collectors.toList());
            user.setDestinations(destinationList);
            userRepository.save(user);
        }
    }

    public com.spotify.api.model.User getByEmail(String email){
        User user = userRepository.findById(email).get();
        if (Objects.isNull(user)){
            return null;
        }
        com.spotify.api.model.User _user = com.spotify.api.model.User.builder()
                .userToken(user.getSpotifyToken())
                .email(email)
                .spotifyId(user.getSpotifyId())
                .token(user.getUserToken())
                .build();
        return _user;
    }

}
