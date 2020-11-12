package com.spotify.api.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class Room extends BaseModel{

    private String roomName;
    private String songId;
    private Date refreshDate;
}
