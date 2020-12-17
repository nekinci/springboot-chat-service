package com.spotify.api.repository.redis;


import com.spotify.api.model.redis.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedisSessionRepository extends CrudRepository<User, String> {
    Optional<User> findById(String email);
    @Override
    void deleteById(String s);
}
