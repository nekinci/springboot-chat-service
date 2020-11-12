package com.spotify.api.models;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.security.Permission;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "users")
@Builder(access = AccessLevel.PUBLIC)
public class User extends BaseModel {
    @Id
    private String id;
    private String name;
    private String surname;
    private String email;
    private Avatar avatar;
    private String spotifyId;
    private String token;
    private String status;
    private String description;
    private List<String> likedStyles;
    private Date birthDate;
    private String birthOfPlace;
    private List<String> hobbyList;
    private UserToken userToken;
    private Date lastSeenDate;


    //TODO: permission list

}
