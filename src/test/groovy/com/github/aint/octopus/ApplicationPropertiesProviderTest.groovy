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

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.ConfigurableEnvironment
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

import static org.assertj.core.api.Assertions.assertThat

@SpringBootTest(classes = TestBootApp, webEnvironment = NONE)
class ApplicationPropertiesProviderTest extends Specification {

    @Subject
    ApplicationPropertiesProvider applicationPropertiesProvider

    @Autowired
    private ConfigurableEnvironment env

    def setup() {
        applicationPropertiesProvider = new ApplicationPropertiesProvider(env)
    }

    def "GetPropertyNames"() {
        when:
        def propertyNames = applicationPropertiesProvider.getPropertyNames()

        then:
        assertThat(propertyNames).isNotEmpty()
    }

    def "GetProperty() with existing key"() {
        expect:
        applicationPropertiesProvider.getProperty("key1") == "value1"
    }

    def "GetProperty() with not existing key"() {
        expect:
        applicationPropertiesProvider.getProperty("key42") == null
    }
}
