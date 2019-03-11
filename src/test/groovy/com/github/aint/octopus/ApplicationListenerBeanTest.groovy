package com.github.aint.octopus

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.support.GenericApplicationContext
import org.springframework.web.client.RestTemplate
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

class ApplicationListenerBeanTest extends Specification {

    @Subject
    private ApplicationListenerBean applicationListenerBean

    private OctopusService octopusService
    private RestTemplate restTemplate

    @Shared
    private WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(8383))

    private static final String octopusUrl = "http://localhost:8383/"

    def setupSpec() {
        wireMockServer.start()
    }

    def setup() {
        restTemplate = Spy()
        octopusService = Mock()

        applicationListenerBean = new ApplicationListenerBean(octopusUrl, octopusService, restTemplate)
    }

    def cleanup() {
        wireMockServer.resetAll()
    }

    def "OnApplicationEvent with context refreshed event"() {
        given:
        def event = new ContextRefreshedEvent(new GenericApplicationContext())

        and:
        def json = DependencyJson.builder()
                .serviceName("devaron")
                .serviceMetadata("Java 11, Spring 5")
                .eventType(DependencyJson.EventType.CREATE)
                .build()
        octopusService.createEvent() >> json

        and:
        wireMockServer.stubFor(
                post("/").willReturn(aResponse().withStatus(201))
        )

        when:
        applicationListenerBean.onApplicationEvent(event)

        then:
        1 * octopusService.createEvent()
        0 * octopusService.destroyEvent()
        1 * restTemplate.postForEntity(octopusUrl, _, _)
        wireMockServer.verify(
                postRequestedFor(urlEqualTo("/"))
        )
        //                        .withRequestBody(equalToJson("{\"eventType\":\"CREATE\",\"serviceName\":\"devaron\",\"serviceMetadata\":\"Java 11, Spring 5\",\"dependencies\":null}"))
    }
}
