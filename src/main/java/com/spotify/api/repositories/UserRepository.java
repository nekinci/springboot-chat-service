package com.spotify.api.repositories;

import com.spotify.api.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;


public interface UserRepository extends MongoRepository<User, UUID> {
}
