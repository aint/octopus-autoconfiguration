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

class IntegrationServiceTest extends Specification {

    private static final def integrationProperties = [
            "integration.services.coruscant.url",
            "integration.services.coruscant.timeout",
            "integration.services.coruscant.logs.enable",
            "integration.services.corellia.url",
            "integration.services.corellia.retry",
            "integration.services.devaron.url",
            "integration.lambdas.bespin.name",
            "integration.lambdas.bespin.qualifier",
            "integration.third-party.naboo.url",
            "integration.third-party.naboo.http2.enabled",
            "integration.third-party.tatooine.url"
    ]

    private ApplicationPropertiesProvider propertiesProvider

    @Subject
    IntegrationService integrationService

    def setup() {
        propertiesProvider = Stub(ApplicationPropertiesProvider)
        propertiesProvider.getPropertyNames() >> integrationProperties

        integrationService = new IntegrationService(propertiesProvider, "integration")
    }

    def "GetServiceNames"() {
        when:
        def services = integrationService.getServiceNames()

        then:
        assertThat(services).containsExactlyInAnyOrder("coruscant", "corellia", "devaron")
    }

    def "GetLambdasNames"() {
        when:
        def lambdas = integrationService.getLambdaNames()

        then:
        assertThat(lambdas).containsExactlyInAnyOrder("bespin")
    }

    def "GetThirdPartiesNames"() {
        when:
        def thirdParties = integrationService.getThirdPartyNames()

        then:
        assertThat(thirdParties).containsExactlyInAnyOrder("naboo", "tatooine")
    }
}
