package com.github.aint.octopus

import io.sixhours.memcached.cache.MemcachedCacheManager
import org.springframework.cache.support.NoOpCacheManager
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import spock.lang.Specification
import spock.lang.Subject

import static org.assertj.core.api.Assertions.assertThat

class SpringCacheServiceTest extends Specification {

    @Subject
    SpringCacheService springCacheService

    def "getCacheProvider for simple cache"() {
        given:
        def cacheManager = new SimpleCacheManager()

        springCacheService = new SpringCacheService(cacheManager)

        when:
        def cacheProvider = springCacheService.getCacheProvider()

        then:
        assertThat(cacheProvider.getName()).isEqualTo("Simple")
        assertThat(cacheProvider.getType()).isEqualTo(SpringCacheService.CacheProviderType.EMBEDDED)
    }

    def "getCacheProvider for unknown cache"() {
        given:
        def cacheManager = new NoOpCacheManager()

        springCacheService = new SpringCacheService(cacheManager)

        when:
        def cacheProvider = springCacheService.getCacheProvider()

        then:
        assertThat(cacheProvider.getName()).isEqualTo("NoOp")
        assertThat(cacheProvider.getType()).isEqualTo(SpringCacheService.CacheProviderType.UNKNOWN)
    }

    def "getCacheProvider for Memcached"() {
        given:
        def cacheManager = new MemcachedCacheManager(null)

        springCacheService = new SpringCacheService(cacheManager)

        when:
        def cacheProvider = springCacheService.getCacheProvider()

        then:
        assertThat(cacheProvider.getName()).isEqualTo("Memcached")
        assertThat(cacheProvider.getType()).isEqualTo(SpringCacheService.CacheProviderType.STANDALONE)
    }

    def "getCacheProvider for Redis"() {
        given:
        def cacheManager = RedisCacheManager.builder(new LettuceConnectionFactory()).build()

        springCacheService = new SpringCacheService(cacheManager)

        when:
        def cacheProvider = springCacheService.getCacheProvider()

        then:
        assertThat(cacheProvider.getName()).isEqualTo("Redis")
        assertThat(cacheProvider.getType()).isEqualTo(SpringCacheService.CacheProviderType.STANDALONE)
    }

}
