package com.spotify.api.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "users")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
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
