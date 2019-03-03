package com.github.aint.octopus

import io.sixhours.memcached.cache.MemcachedCacheManager
import org.springframework.boot.autoconfigure.cache.CacheProperties
import org.springframework.boot.autoconfigure.cache.CacheType
import org.springframework.cache.support.SimpleCacheManager
import spock.lang.Specification
import spock.lang.Subject

import static org.assertj.core.api.Assertions.assertThat

class SpringCacheServiceTest extends Specification {

    @Subject
    SpringCacheService springCacheService

    def "getCacheProvider for simple cache"() {
        given:
        def cacheManager    = new SimpleCacheManager()
        def cacheProperties = new CacheProperties()

        springCacheService = new SpringCacheService(cacheManager, cacheProperties)

        when:
        def cacheProvider = springCacheService.getCacheProvider()

        then:
        assertThat(cacheProvider.getName()).isEqualTo("Simple")
        assertThat(cacheProvider.getType()).isEqualTo("Embedded")
    }

    def "getCacheProvider for Memcached"() {
        given:
        def cacheManager    = new MemcachedCacheManager(null)
        def cacheProperties = new CacheProperties()

        springCacheService = new SpringCacheService(cacheManager, cacheProperties)

        when:
        def cacheProvider = springCacheService.getCacheProvider()

        then:
        assertThat(cacheProvider.getName()).isEqualTo("Memcached")
        assertThat(cacheProvider.getType()).isEqualTo("Standalone")
    }

    def "getCacheProvider for Redis"() {
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
