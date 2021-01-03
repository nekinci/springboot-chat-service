package com.spotify.api.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties("cache")
@Data
public class CacheProps {
    Map<String, Long> expirations = new HashMap<>();
}
