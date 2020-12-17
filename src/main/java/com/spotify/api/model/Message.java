package com.spotify.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class Message {

    private String id;
    private String message;
    private String from;
    private boolean isOwn;
    private Date date;
    private MessageType type;

    public enum MessageType{
        MESSAGE,
        JOIN,
        LEAVE
    }
}
