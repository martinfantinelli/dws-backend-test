package com.dws.isobarfm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

@ConfigurationProperties(prefix = "bands.api")
public record ApiProperties(
    String baseUrl,
    @DefaultValue("2s") Duration connectTimeout,
    @DefaultValue("5s") Duration readTimeout
) {}
