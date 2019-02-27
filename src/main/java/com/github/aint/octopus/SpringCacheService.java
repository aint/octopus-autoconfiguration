package com.github.aint.octopus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpringCacheService {

    private final CacheManager cacheManager;
    private final CacheProperties cacheProperties;

    public String getCacheProviderName() {
        if (cacheProperties.getType() != null) {
            String cacheProvider = cacheProperties.getType().name();
            log.debug("{} detected", cacheProvider);
            return cacheProvider;
        }

        String cacheProvider = cacheManager.getClass().getSimpleName();
        if (cacheProvider.toLowerCase().contains("memcached")) {
            log.debug("Memcached detected");
            return "Memcached";
        } else if (cacheProvider.toLowerCase().contains("redis")) {
            log.debug("Redis detected");
            return "Redis";
        } else if (cacheProvider.toLowerCase().contains("hazelcast")) {
            log.debug("Hazelcast detected");
            return "Hazelcast";
        }

        log.debug("{} detected", cacheProvider);
        return cacheProvider;
    }

}
