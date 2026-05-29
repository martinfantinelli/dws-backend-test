package com.dws.isobarfm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache")
public record CacheProperties(long ttlMinutes) {}
