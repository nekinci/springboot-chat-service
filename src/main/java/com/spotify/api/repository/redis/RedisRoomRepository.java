package com.spotify.api.repository.redis;

import com.spotify.api.model.redis.Room;
import com.spotify.api.model.redis.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRoomRepository extends QueryByExampleExecutor<Room>, CrudRepository<Room, String> {
}
