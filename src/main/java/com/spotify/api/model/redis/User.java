package com.spotify.api.model.redis;

import com.spotify.api.model.valueObject.UserToken;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@RedisHash("user_session")
@Builder
public class User implements Serializable {

    private String id;
    @Id
    private String email;
    private String spotifyId;
    private String currentRoom;
    private Date firstConnectionAt;
    private long startTime;
    private long endTime;
    private UserToken spotifyToken;
    private String userToken;
    private List<String> destinations;
}
