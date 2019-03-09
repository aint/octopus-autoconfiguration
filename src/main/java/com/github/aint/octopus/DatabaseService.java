package com.github.aint.octopus;

import java.util.HashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseService {

    private final JdbcResolver jdbcResolver;
    private final ObjectProvider<SpringCacheService> optionalSpringCacheService;

    public Set<String> dbs() {
        log.warn("DBS");
        Set<String> dbs = new HashSet<>(jdbcResolver.getDbNames());
        log.warn("DBS: jdbc {}", dbs);
        optionalSpringCacheService.ifAvailable(springCacheService -> {
            SpringCacheService.CacheProvider cacheProvider = springCacheService.getCacheProvider();
            log.warn("DBS: cache {}", cacheProvider);
            if (cacheProvider.getType() == SpringCacheService.CacheProviderType.STANDALONE) {
                dbs.add(cacheProvider.getName());
            }
        });
        return dbs;
    }

    // add NoSQL support

}
