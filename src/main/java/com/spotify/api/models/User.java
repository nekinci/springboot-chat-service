package com.spotify.api.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "users")
public class User extends BaseModel {

    private UUID _id;
    private String name;
    private String surname;
    private String email;
    private String avatarUrl;
    private String spotifyId;
    private String token;
    private Date birthDate;
    private String birthOfPlace;
    private List<String> hobbyList;
    private UserToken userToken;

}
