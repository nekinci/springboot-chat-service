package com.spotify.api.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
public class BaseModel {


    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date modifiedDate;
    private Date endDate;
}
