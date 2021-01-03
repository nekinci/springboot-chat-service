package com.spotify.api.model.valueObject;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Avatar implements Serializable {

    private String fileName;
    private String pyhsicalPath;
    private String url;
    private String cdnUrl;
    private boolean isVisible;
}
