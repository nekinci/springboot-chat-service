package com.spotify.api.repository.redis;

import com.spotify.api.model.redis.Room;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRoomRepository extends CrudRepository<Room, String> {

}
