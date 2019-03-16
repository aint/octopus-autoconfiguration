package com.github.aint.octopus

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.ContextStoppedEvent
import org.springframework.context.support.GenericApplicationContext
import org.springframework.web.client.RestTemplate
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

class ApplicationListenerBeanTest extends Specification {

    private static final Integer PORT = 8383
    private static final String OCTOPUS_URL = "http://localhost:${PORT}/"

    @Subject
    private ApplicationListenerBean applicationListenerBean

    private OctopusService octopusService
    private RestTemplate restTemplate

    @Shared
    private WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(PORT))

    def setupSpec() {
        wireMockServer.start()
    }

    def setup() {
        wireMockServer.resetAll()

        restTemplate = Spy()
        octopusService = Stub()

        applicationListenerBean = new ApplicationListenerBean(OCTOPUS_URL, octopusService, restTemplate)
    }

    def cleanupSpec() {
        wireMockServer.shutdown()
    }

    def "OnApplicationEvent with context refreshed event"() {
        given:
        def event = new ContextRefreshedEvent(new GenericApplicationContext())

        and:
        def dependencyJson = DependencyJson.builder()
                .eventType(DependencyJson.EventType.CREATE)
                .serviceName("devaron")
                .serviceType("svc")
                .serviceMetadata("Java 11, Spring 5")
                .build()
        octopusService.createEvent() >> dependencyJson

        and:
        wireMockServer.stubFor(post("/").willReturn(ok()))

        when:
        applicationListenerBean.onApplicationEvent(event)

        then:
        1 * restTemplate.postForEntity(OCTOPUS_URL, { it == dependencyJson }, Void.class)

        and:
        def json = """
                   {
                     "eventType": "${dependencyJson.eventType.name()}",
                     "serviceName": "${dependencyJson.serviceName}",
                     "serviceType": "${dependencyJson.serviceType}",
                     "serviceMetadata": "${dependencyJson.serviceMetadata}",
                     "dependencies": null
                   }
                   """
        wireMockServer.verify(
                postRequestedFor(urlEqualTo("/"))
                        .withHeader("Content-Type", equalTo("application/json;charset=UTF-8"))
                        .withRequestBody(equalToJson(json))
        )
    }

    def "OnApplicationEvent with context stopped event"() {
        given:
        def event = new ContextStoppedEvent(new GenericApplicationContext())

        and:
        def dependencyJson = DependencyJson.builder()
                .eventType(DependencyJson.EventType.DELETE)
                .serviceName("devaron")
                .serviceType("svc")
                .serviceMetadata("Java 11, Spring 5")
                .build()
        octopusService.destroyEvent() >> dependencyJson

        and:
        wireMockServer.stubFor(post("/").willReturn(ok()))

        when:
        applicationListenerBean.onApplicationEvent(event)

        then:
        1 * restTemplate.postForEntity(OCTOPUS_URL, { it == dependencyJson }, Void.class)

        and:
        def json = """
                   {
                     "eventType": "${dependencyJson.eventType.name()}",
                     "serviceName": "${dependencyJson.serviceName}",
                     "serviceType": "${dependencyJson.serviceType}",
                     "serviceMetadata": "${dependencyJson.serviceMetadata}",
                     "dependencies": null
                   }
                   """
        wireMockServer.verify(
                postRequestedFor(urlEqualTo("/"))
                        .withHeader("Content-Type", equalTo("application/json;charset=UTF-8"))
                        .withRequestBody(equalToJson(json))
        )
    }

}
