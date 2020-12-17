package com.spotify.api.model.valueObject;


import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class UserToken {

    private String token;
    private String refreshToken;
    private int expiresIn;
    @LastModifiedDate
    private LocalDateTime lastChangeAt;
}
