package com.spotify.api.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
public class Room extends BaseModel{

    private String roomName;
    private String songId;
    private Date refreshDate;
}
