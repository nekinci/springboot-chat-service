package com.spotify.api.models;


import lombok.Data;

import java.util.UUID;

@Data
public class UserToken {

    private String token;
    private String refreshToken;
    private int expiresIn;
}
