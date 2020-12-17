package com.spotify.api.model.valueObject;

import lombok.Data;

@Data
public class Avatar {

    private String fileName;
    private String pyhsicalPath;
    private String url;
    private String cdnUrl;
    private boolean isVisible;
}
