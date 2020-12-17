package com.spotify.api.repository;

import com.spotify.api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface UserRepository extends MongoRepository<User, String> {
    User findBySpotifyId(String id);
    User findByEmailAndEndDateNull(String email);

}
