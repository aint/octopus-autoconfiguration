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
import org.springframework.cache.CacheManager
import org.springframework.cache.support.NoOpCacheManager
import org.springframework.core.SpringVersion
import org.springframework.core.env.Environment
import spock.lang.Specification
import spock.lang.Subject

class SpringApplicationMetadataTest extends Specification {

    @Subject
    private SpringApplicationMetadata springApplicationMetadata

    private Environment environment
    private ObjectProvider<CacheManager> optionalCacheManager

    def setup() {
        environment = Mock()
        optionalCacheManager = Mock()

        springApplicationMetadata = new SpringApplicationMetadata(environment, optionalCacheManager)
    }

    def "IsCachingEnabled true"() {
        given:
        optionalCacheManager.getIfAvailable() >> new NoOpCacheManager()

        expect:
        springApplicationMetadata.isCachingEnabled()
    }

    def "IsCachingEnabled false"() {
        given:
        optionalCacheManager.getIfAvailable() >> null

        expect:
        !springApplicationMetadata.isCachingEnabled()
    }

    def "GetApplicationName should succeed"() {
        given:
        def final givenAppName = "Devaron"
        environment.getRequiredProperty("spring.application.name") >> givenAppName

        when:
        def appName = springApplicationMetadata.getApplicationName()

        then:
        appName == givenAppName
    }

    def "GetApplicationName should throw exception"() {
        given:
        environment.getRequiredProperty(_ as String) >> { throw new IllegalStateException() }

        when:
        def appName = springApplicationMetadata.getApplicationName()

        then:
        thrown IllegalStateException
    }

    def "GetJavaVersion"() {
        when:
        def javaVersion = springApplicationMetadata.getJavaVersion()

        then:
        javaVersion == 8
    }

    def "GetSpringVersion"() {
        when:
        def springVersion = springApplicationMetadata.getSpringVersion()

        then:
        SpringVersion.getVersion().startsWith(springVersion)
    }

}
