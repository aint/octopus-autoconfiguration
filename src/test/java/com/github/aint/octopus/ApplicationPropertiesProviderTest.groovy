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
