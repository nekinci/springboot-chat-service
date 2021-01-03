package com.spotify.api.dto;

import lombok.Data;


@Data
public class TypingDto {
    private String destination;
    private String id;
    private boolean typing;
    private String fullName;
    private String email;
}
