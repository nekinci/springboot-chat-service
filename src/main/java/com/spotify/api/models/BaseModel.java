package com.spotify.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseModel {


    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date modifiedDate;
    private Date endDate;

    @Version
    private int version;
}
