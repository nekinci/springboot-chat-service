package com.spotify.api.dto;

import com.spotify.api.model.redis.valueObject.User;
import lombok.Data;

import java.util.List;

@Data
public class OnlineUserDto {
    private String destination;
    private int count;
    private String type;
    private List<User> userList;
}

