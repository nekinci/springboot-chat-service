package com.spotify.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TokenDto {

    private String token;
    private String refreshToken;
    private long expiresIn;

}
