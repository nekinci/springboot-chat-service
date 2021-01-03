package com.spotify.api.model.valueObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Detail implements Serializable {

    private String ipAddress;
    private String agent;
    private Date loginDate;
    private String osVersion;

}
