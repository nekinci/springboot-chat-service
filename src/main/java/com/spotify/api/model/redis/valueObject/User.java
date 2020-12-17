package com.spotify.api.model.redis.valueObject;

import lombok.Data;

@Data
public class User {
    private String id;
    private String name;
    private String surname;
    private String fullName;
    private String avatar;
}
