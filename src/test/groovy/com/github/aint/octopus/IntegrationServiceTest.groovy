package com.github.aint.octopus

import spock.lang.Specification
import spock.lang.Subject

import static org.assertj.core.api.Assertions.assertThat

class IntegrationServiceTest extends Specification {

    private static final def integrationProperties = [
            "integration.services.coruscant",
            "integration.services.corellia",
            "integration.services.devaron",
            "integration.lambdas.bespin.name",
            "integration.lambdas.bespin.qualifier",
            "integration.third-party.naboo",
            "integration.third-party.tatooine"
    ]
    @Subject
    IntegrationService integrationService

    def "GetServiceNames"() {
        given:
        ApplicationPropertiesProvider propertiesProvider = Stub(ApplicationPropertiesProvider)
        propertiesProvider.getPropertyNames() >> integrationProperties

        integrationService = new IntegrationService(propertiesProvider, "integration")

        when:
        def services = integrationService.getServiceNames()

        then:
        assertThat(services).containsExactlyInAnyOrder("coruscant", "corellia", "devaron")
    }

    def "GetLambdasNames"() {
        given:
        ApplicationPropertiesProvider propertiesProvider = Stub(ApplicationPropertiesProvider)
        propertiesProvider.getPropertyNames() >> integrationProperties

        integrationService = new IntegrationService(propertiesProvider, "integration")

        when:
        def lambdas = integrationService.getLambdaNames()

        then:
        assertThat(lambdas).containsExactlyInAnyOrder("bespin")
    }

    def "GetThirdPartiesNames"() {
        given:
        ApplicationPropertiesProvider propertiesProvider = Stub(ApplicationPropertiesProvider)
        propertiesProvider.getPropertyNames() >> integrationProperties

        integrationService = new IntegrationService(propertiesProvider, "integration")

        when:
        def thirdParties = integrationService.getThirdPartyNames()

        then:
        assertThat(thirdParties).containsExactlyInAnyOrder("naboo", "tatooine")
    }
}
