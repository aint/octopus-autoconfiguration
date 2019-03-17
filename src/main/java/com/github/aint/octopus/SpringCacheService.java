/*
 * Copyright (C) 2018 - 2019 Oleksandr Tyshkovets <olexandr.tyshkovets@gmail.com>
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.github.aint.octopus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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
            return new CacheProvider("Memcached", CacheProviderType.STANDALONE);
        } else if (cacheProvider.toLowerCase().contains("redis")) {
            log.debug("Redis detected");
            return new CacheProvider("Redis", CacheProviderType.STANDALONE);
        } else if (cacheProvider.toLowerCase().contains("hazelcast")) {
            log.debug("Hazelcast detected");
            return new CacheProvider("Hazelcast", CacheProviderType.STANDALONE);
        }

        log.debug("{} detected", cacheProvider);
        String cacheProviderName = cacheProvider.replace(CacheManager.class.getSimpleName(), "");
        return new CacheProvider(cacheProviderName, resolveCacheProviderType(cacheProviderName));
    }

    private CacheProviderType resolveCacheProviderType(String cacheProviderName) {
        if (isEmbeddedType(cacheProviderName)) {
            return CacheProviderType.EMBEDDED;
        }
        if (isStandaloneType(cacheProviderName)) {
            return CacheProviderType.STANDALONE;
        }
        return CacheProviderType.UNKNOWN;
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
        private CacheProviderType type;

    }

    @Getter
    @RequiredArgsConstructor
    public enum CacheProviderType {
        EMBEDDED("Embedded"), STANDALONE("Standalone"), UNKNOWN("Unknown");

        private final String type;
    }

}
