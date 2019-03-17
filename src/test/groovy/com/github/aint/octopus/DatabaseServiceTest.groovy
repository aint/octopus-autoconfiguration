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

import org.springframework.beans.factory.ObjectProvider
import spock.lang.Specification
import spock.lang.Subject

import static com.github.aint.octopus.SpringCacheService.CacheProviderType.*
import static org.assertj.core.api.Assertions.assertThat

class DatabaseServiceTest extends Specification {

    @Subject
    private DatabaseService databaseService

    private JdbcResolver jdbcResolver
    private SpringCacheService springCacheService
    private ObjectProvider<SpringCacheService> optionalSpringCacheService

    def setup() {
        jdbcResolver = Mock()
        springCacheService = Mock()
        optionalSpringCacheService = Spy()

        databaseService = new DatabaseService(jdbcResolver, optionalSpringCacheService)
    }

    def "dbs with only jdbc"() {
        given:
        final def dbNames =  [ "MySQL" ]
        jdbcResolver.dbNames >> dbNames

        and:
        optionalSpringCacheService.getIfAvailable() >> null

        when:
        def dbs = databaseService.dbs()

        then:
        assertThat(dbs).containsExactlyInAnyOrderElementsOf(dbNames)
    }

    def "dbs with only embedded cache"() {
        given:
        final def cacheProvider = new SpringCacheService.CacheProvider("Caffeine", EMBEDDED)
        springCacheService.getCacheProvider() >> cacheProvider
        optionalSpringCacheService.getIfAvailable() >> springCacheService

        and:
        jdbcResolver.dbNames >> []

        when:
        def dbs = databaseService.dbs()

        then:
        assertThat(dbs).isEmpty()
    }

    def "dbs with jdbc and embedded cache"() {
        given:
        final def dbNames =  [ "MySQL" ]
        jdbcResolver.dbNames >> dbNames

        and:
        final def cacheProvider = new SpringCacheService.CacheProvider("Caffeine", EMBEDDED)
        springCacheService.getCacheProvider() >> cacheProvider
        optionalSpringCacheService.getIfAvailable() >> springCacheService

        when:
        def dbs = databaseService.dbs()

        then:
        assertThat(dbs).containsExactlyInAnyOrderElementsOf(dbNames)
    }

    def "dbs with jdbc and stadalone cache"() {
        given:
        final def dbNames =  [ "PostgreSQL" ]
        jdbcResolver.dbNames >> dbNames

        and:
        final def cacheProvider = new SpringCacheService.CacheProvider("Redis", STANDALONE)
        springCacheService.getCacheProvider() >> cacheProvider
        optionalSpringCacheService.getIfAvailable() >> springCacheService

        when:
        def dbs = databaseService.dbs()

        then:
        assertThat(dbs).containsExactlyInAnyOrderElementsOf(dbNames + cacheProvider.name)
    }

}
