package com.github.aint.octopus

import org.springframework.boot.autoconfigure.cache.CacheProperties
import org.springframework.boot.autoconfigure.cache.CacheType
import org.springframework.cache.support.SimpleCacheManager
import spock.lang.Specification
import spock.lang.Subject

import static org.assertj.core.api.Assertions.assertThat

class SpringCacheServiceTest extends Specification {

    @Subject
    SpringCacheService springCacheService

    def "getCacheProviderName"() {
        given:
        def cacheManager    = new SimpleCacheManager()
        def cacheProperties = new CacheProperties()

        springCacheService = new SpringCacheService(cacheManager, cacheProperties)

        when:
        def cacheProviderName = springCacheService.getCacheProviderName()

        then:
        assertThat(cacheProviderName).isEqualTo(SimpleCacheManager.class.getSimpleName())
    }

    def "getCacheProviderName with set cacheProperties type"() {
        given:
        def cacheManager    = null
        def cacheProperties = new CacheProperties()
        cacheProperties.setType(CacheType.REDIS)

        springCacheService = new SpringCacheService(cacheManager, cacheProperties)

        when:
        def cacheProviderName = springCacheService.getCacheProviderName()

        then:
        assertThat(cacheProviderName).isEqualTo(CacheType.REDIS.name())
    }

}
