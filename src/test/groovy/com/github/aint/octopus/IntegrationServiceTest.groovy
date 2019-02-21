package com.github.aint.octopus

import spock.lang.Specification
import spock.lang.Subject

import static org.assertj.core.api.Assertions.assertThat

class IntegrationServiceTest extends Specification {

    private static final def integrationProperties = [
            "integration.services.coruscant.url",
            "integration.services.coruscant.timeout",
            "integration.services.corellia.url",
            "integration.services.corellia.retry",
            "integration.services.devaron.url",
            "integration.lambdas.bespin.name",
            "integration.lambdas.bespin.qualifier",
            "integration.third-party.naboo.url",
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
