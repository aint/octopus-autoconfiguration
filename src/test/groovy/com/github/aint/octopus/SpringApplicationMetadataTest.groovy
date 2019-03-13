package com.github.aint.octopus

import org.springframework.beans.factory.ObjectProvider
import org.springframework.cache.CacheManager
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

    def "GetSpringVersion"() {
        when:
        def springVersion = springApplicationMetadata.getSpringVersion()

        then:
        SpringVersion.getVersion().startsWith(springVersion)
    }

}
