package com.dws.isobarfm.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(CacheProperties cacheProperties) {
        CaffeineCacheManager manager = new CaffeineCacheManager("catalog");
        manager.setCaffeine(
            Caffeine.newBuilder()
                .expireAfterWrite(cacheProperties.ttlMinutes(), TimeUnit.MINUTES)
                .recordStats()
        );
        return manager;
    }
}
