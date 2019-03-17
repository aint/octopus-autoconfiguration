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

import spock.lang.Specification
import spock.lang.Subject

import static org.assertj.core.api.Assertions.assertThat

class OctopusServiceTest extends Specification {

    private DatabaseService databaseService
    private IntegrationService integrationService
    private SpringApplicationMetadata springApplicationMetadata

    @Subject
    OctopusService octopusService

    def setup() {
        databaseService = Stub()
        integrationService = Stub()
        springApplicationMetadata = Stub()

        octopusService = new OctopusService(databaseService, integrationService, springApplicationMetadata)
    }

    def "CreateEvent"() {
        given:
        final def services = [ "devaron", "bespin" ]
        final def lambdas = [ "geonosis", "kessel" ]
        final def thirdParties = [ "corellia", "coruscant" ]
        integrationService.getServiceNames() >> services
        integrationService.getLambdaNames() >> lambdas
        integrationService.getThirdPartyNames() >> thirdParties

        and:
        final def databases = [ "MySQL", "Redis" ]
        databaseService.dbs() >> databases

        and:
        final def appName = "myapp"
        final def springVersion = "5.2"
        final def javaVersion = 10
        springApplicationMetadata.getApplicationName() >> appName
        springApplicationMetadata.getSpringVersion() >> springVersion
        springApplicationMetadata.getJavaVersion() >> javaVersion
        springApplicationMetadata.isCachingEnabled() >> true


        when:
        def json = octopusService.createEvent()


        then:
        assertThat(json).isNotNull()
        println json.serviceMetadata
        assertThat(json).satisfies {
            assertThat(it.getEventType()).isEqualTo(DependencyJson.EventType.CREATE)
            assertThat(it.getServiceName()).isEqualTo(appName)
            assertThat(it.getServiceMetadata()).isEqualTo("Java ${javaVersion}, Spring ${springVersion} | :cache:".toString())
        }
        assertThat(json.getDependencies()).hasSize(4)
        assertThat(json.getDependencies()).satisfies {
            assertThat(it.get(DependencyJson.DependencyType.SERVICES)).containsExactlyInAnyOrderElementsOf(services)
            assertThat(it.get(DependencyJson.DependencyType.LAMBDAS)).containsExactlyInAnyOrderElementsOf(lambdas)
            assertThat(it.get(DependencyJson.DependencyType.THIRD_PARTY)).containsExactlyInAnyOrderElementsOf(thirdParties)
            assertThat(it.get(DependencyJson.DependencyType.DATABASES)).containsExactlyInAnyOrderElementsOf(databases)
        }
    }

    def "DestroyEvent"() {
        given:
        final def appName = "myapp"
        springApplicationMetadata.getApplicationName() >> appName


        when:
        def json = octopusService.destroyEvent()


        then:
        assertThat(json).isNotNull()
        assertThat(json).satisfies {
            assertThat(it.getEventType()).isEqualTo(DependencyJson.EventType.DELETE)
            assertThat(it.getServiceName()).isEqualTo(appName)
            assertThat(it.getServiceMetadata()).isNull()
            assertThat(it.getDependencies()).isNull()
        }
    }

}
