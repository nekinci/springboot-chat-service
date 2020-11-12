package com.spotify.api.models;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document
public class UserToken {

    private String token;
    private String refreshToken;
    private int expiresIn;
    @LastModifiedDate
    private LocalDateTime lastChangeAt;
}
