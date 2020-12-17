package com.spotify.api.model.redis;

import com.spotify.api.model.redis.valueObject.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@RedisHash("room_users")
@Data
public class Room {
    private String id;
    @Id
    private String destination;
    private List<User> userList;
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private int userCount;

    public int getUserCount(){
        return userList.size();
    }
}

