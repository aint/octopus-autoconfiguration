package com.github.aint.octopus

import groovy.transform.CompileStatic
import org.springframework.boot.autoconfigure.cache.CacheProperties
import org.springframework.boot.autoconfigure.cache.CacheType
import org.springframework.cache.support.SimpleCacheManager
import spock.lang.Specification
import spock.lang.Subject

import static org.assertj.core.api.Assertions.assertThat

//@CompileStatic
class SpringCacheServiceTest extends Specification {

    @Subject
    SpringCacheService springCacheService

    def "getCacheProviderName"() {
        given:
        def cacheManager    = new SimpleCacheManager()
        def cacheProperties = new CacheProperties()

        springCacheService = new SpringCacheService(cacheManager, cacheProperties)

        when:
        def cacheProvider = springCacheService.getCacheProvider()

        then:
        assertThat(cacheProvider.getName()).isEqualTo(SimpleCacheManager.class.getSimpleName())
        assertThat(cacheProvider.getType()).isEqualTo("Embedded")
    }

    def "getCacheProviderName with set cacheProperties type"() {
        given:
        def cacheManager    = null
        def cacheProperties = new CacheProperties()
        cacheProperties.setType(CacheType.REDIS)

        springCacheService = new SpringCacheService(cacheManager, cacheProperties)

        when:
        def cacheProvider = springCacheService.getCacheProvider()

        then:
        assertThat(cacheProvider.getName()).isEqualTo(CacheType.REDIS.name())
        assertThat(cacheProvider.getType()).isEqualTo("Standalone")
    }

}
