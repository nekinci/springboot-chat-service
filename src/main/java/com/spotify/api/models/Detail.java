package com.spotify.api.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Detail {

    private String ipAddress;
    private String agent;
    private Date loginDate;
    private String osVersion;

}
