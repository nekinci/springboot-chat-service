package com.spotify.api.repositories;

import com.spotify.api.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


public interface UserRepository extends MongoRepository<User, UUID> {
    User findBySpotifyId(String id);
}
