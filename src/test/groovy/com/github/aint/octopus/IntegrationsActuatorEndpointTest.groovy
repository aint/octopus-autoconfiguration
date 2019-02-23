package com.github.aint.octopus

import spock.lang.Specification
import spock.lang.Subject

import static org.assertj.core.api.Assertions.assertThat

class IntegrationsActuatorEndpointTest extends Specification {

    private OctopusService octopusService = Stub(OctopusService)

    @Subject
    IntegrationsActuatorEndpoint actuatorEndpoint = new IntegrationsActuatorEndpoint(octopusService)

    def "integrations"() {
        def json = DependencyJson.builder()
                .eventType(DependencyJson.EventType.CREATE)
                .serviceMetadata("Java, Spring")
                .serviceName("App")
                .build()
        given:
        octopusService.createEvent() >> json

        when:
        def actualJson = actuatorEndpoint.integrations()

        then:
        assertThat(actualJson).isEqualTo(json)
    }
}
