/**
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
package com.github.aint.octopus

import io.sixhours.memcached.cache.MemcachedCacheManager
import org.springframework.cache.support.NoOpCacheManager
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import spock.lang.Specification
import spock.lang.Subject

import static org.assertj.core.api.Assertions.assertThat

class SpringCacheServiceTest extends Specification {

    @Subject
    SpringCacheService springCacheService

    def "getCacheProvider for simple cache"() {
        given:
        def cacheManager = new SimpleCacheManager()

        springCacheService = new SpringCacheService(cacheManager)

        when:
        def cacheProvider = springCacheService.getCacheProvider()

        then:
        assertThat(cacheProvider.getName()).isEqualTo("Simple")
        assertThat(cacheProvider.getType()).isEqualTo(SpringCacheService.CacheProviderType.EMBEDDED)
    }

    def "getCacheProvider for unknown cache"() {
        given:
        def cacheManager = new NoOpCacheManager()

        springCacheService = new SpringCacheService(cacheManager)

        when:
        def cacheProvider = springCacheService.getCacheProvider()

        then:
        assertThat(cacheProvider.getName()).isEqualTo("NoOp")
        assertThat(cacheProvider.getType()).isEqualTo(SpringCacheService.CacheProviderType.UNKNOWN)
    }

    def "getCacheProvider for Memcached"() {
        given:
        def cacheManager = new MemcachedCacheManager(null)

        springCacheService = new SpringCacheService(cacheManager)

        when:
        def cacheProvider = springCacheService.getCacheProvider()

        then:
        assertThat(cacheProvider.getName()).isEqualTo("Memcached")
        assertThat(cacheProvider.getType()).isEqualTo(SpringCacheService.CacheProviderType.STANDALONE)
    }

    def "getCacheProvider for Redis"() {
        given:
        def cacheManager = RedisCacheManager.builder(new LettuceConnectionFactory()).build()

        springCacheService = new SpringCacheService(cacheManager)

        when:
        def cacheProvider = springCacheService.getCacheProvider()

        then:
        assertThat(cacheProvider.getName()).isEqualTo("Redis")
        assertThat(cacheProvider.getType()).isEqualTo(SpringCacheService.CacheProviderType.STANDALONE)
    }

}
