package com.spotify.api.core.abstraction;

public interface GenericRepository<T, V> {

    V getById(T id);
    
}
