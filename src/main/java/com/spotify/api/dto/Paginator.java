package com.spotify.api.dto;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Paginator<T> {
    private int size;
    private long totalSize;
    private int page;
    private int totalPage;
    private List<T> data;

}
