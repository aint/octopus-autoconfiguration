package com.github.aint.octopus;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

//@ConditionalOnBean(CacheManager.class)
@Component
@RequiredArgsConstructor
public class SpringCacheService {

    private final ObjectProvider<CacheManager> optionalCacheManager;
//    private final ObjectProvider<CacheProperties> optionalCacheProperties;

    public Optional<String> getCacheProviderName() {
        return Optional.ofNullable(optionalCacheManager.getIfAvailable())
                .map(c -> c.getClass().getSimpleName())
                .map(c -> {
                    if (c.toLowerCase().contains("memcached")) {
                        System.out.println("It's Memcached");
                        return "Memcached";
                    } else if (c.toLowerCase().contains("redis")) {
                        System.out.println("It's Redis");
                        return "Redis";
                    } else if (c.toLowerCase().contains("hazelcast")) {
                        System.out.println("It's Hazelcast");
                        return "Hazelcast";
                    }

                    System.out.println(c);
                    return c;
                });
    }

}
