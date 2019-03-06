package com.github.aint.octopus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpringCacheService {

    private final CacheManager cacheManager;

    public CacheProvider getCacheProvider() {
        String cacheProvider = cacheManager.getClass().getSimpleName();
        if (cacheProvider.toLowerCase().contains("memcached")) {
            log.debug("Memcached detected");
            return new CacheProvider("Memcached", "Standalone");
        } else if (cacheProvider.toLowerCase().contains("redis")) {
            log.debug("Redis detected");
            return new CacheProvider("Redis", "Standalone");
        } else if (cacheProvider.toLowerCase().contains("hazelcast")) {
            log.debug("Hazelcast detected");
            return new CacheProvider("Hazelcast", "Standalone");
        }

        log.debug("{} detected", cacheProvider);
        String cacheProviderName = cacheProvider.replace(CacheManager.class.getSimpleName(), "");
        return new CacheProvider(cacheProviderName, resolveCacheProviderType(cacheProviderName));
    }

    private String resolveCacheProviderType(String cacheProviderName) {
        if (isEmbeddedType(cacheProviderName)) {
            return "Embedded";
        }
        if (isStandaloneType(cacheProviderName)) {
            return "Standalone";
        }
        return "Unknown";
    }

    private boolean isEmbeddedType(String cacheProviderName) {
        cacheProviderName = cacheProviderName.toUpperCase();
        return cacheProviderName.contains(CacheType.SIMPLE.name())
                || cacheProviderName.contains(CacheType.GENERIC.name())
                || cacheProviderName.contains(CacheType.CAFFEINE.name())
                || cacheProviderName.contains(CacheType.EHCACHE.name())  // check
                || cacheProviderName.contains("Map".toUpperCase());
    }

    private boolean isStandaloneType(String cacheProviderName) {
        cacheProviderName = cacheProviderName.toUpperCase();
        return cacheProviderName.contains(CacheType.REDIS.name())
                || cacheProviderName.contains(CacheType.HAZELCAST.name())  // check
                || cacheProviderName.contains(CacheType.INFINISPAN.name()) // check
                || cacheProviderName.contains(CacheType.COUCHBASE.name())  // check
                || cacheProviderName.contains("MEMCACHED");
    }

    @Data
    @AllArgsConstructor
    public static class CacheProvider {

        private String name;
        private String type; // enum?

    }

}
