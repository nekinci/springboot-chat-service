package com.spotify.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Date date;
    private MessageType type;

    private String senderId;
    private String roomId;
    public enum MessageType{
        MESSAGE,
        JOIN,
        LEAVE
    }
}
