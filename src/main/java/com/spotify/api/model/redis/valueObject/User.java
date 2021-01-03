package com.spotify.api.model.redis.valueObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class User {
    private String id;
    private String name;
    private String surname;
    private String fullName;
    private String avatar;
    @JsonIgnore
    private String email;
}
