package com.spotify.api.core;

import lombok.Data;

@Data
public class ResponseObject<T> {

    private String message;
    private int statusCode;
    private String lang;
    private String error;
    private boolean success;
    private T response;
}
