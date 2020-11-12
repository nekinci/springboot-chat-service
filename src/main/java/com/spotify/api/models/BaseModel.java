package com.spotify.api.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class BaseModel {


    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date modifiedDate;
    private Date endDate;

    @Version
    private int version;
}
