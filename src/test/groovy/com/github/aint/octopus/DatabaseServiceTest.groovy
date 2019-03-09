package com.github.aint.octopus

import org.springframework.beans.factory.ObjectProvider
import spock.lang.Specification
import spock.lang.Subject

import static com.github.aint.octopus.SpringCacheService.CacheProviderType.*
import static org.assertj.core.api.Assertions.assertThat

class DatabaseServiceTest extends Specification {

    @Subject
    private DatabaseService databaseService

    private JdbcResolver jdbcResolver
    private SpringCacheService springCacheService
    private ObjectProvider<SpringCacheService> optionalSpringCacheService

    def setup() {
        jdbcResolver = Mock()
        springCacheService = Mock()
        optionalSpringCacheService = Spy()

        databaseService = new DatabaseService(jdbcResolver, optionalSpringCacheService)
    }

    def "dbs with only jdbc"() {
        given:
        final def dbNames =  [ "MySQL" ]
        jdbcResolver.dbNames >> dbNames

        and:
        optionalSpringCacheService.getIfAvailable() >> null

        when:
        def dbs = databaseService.dbs()

        then:
        assertThat(dbs).containsExactlyInAnyOrderElementsOf(dbNames)
    }

    def "dbs with only embedded cache"() {
        given:
        final def cacheProvider = new SpringCacheService.CacheProvider("Caffeine", EMBEDDED)
        springCacheService.getCacheProvider() >> cacheProvider
        optionalSpringCacheService.getIfAvailable() >> springCacheService

        and:
        jdbcResolver.dbNames >> []

        when:
        def dbs = databaseService.dbs()

        then:
        assertThat(dbs).isEmpty()
    }

    def "dbs with jdbc and embedded cache"() {
        given:
        final def dbNames =  [ "MySQL" ]
        jdbcResolver.dbNames >> dbNames

        and:
        final def cacheProvider = new SpringCacheService.CacheProvider("Caffeine", EMBEDDED)
        springCacheService.getCacheProvider() >> cacheProvider
        optionalSpringCacheService.getIfAvailable() >> springCacheService

        when:
        def dbs = databaseService.dbs()

        then:
        assertThat(dbs).containsExactlyInAnyOrderElementsOf(dbNames)
    }

    def "dbs with jdbc and stadalone cache"() {
        given:
        final def dbNames =  [ "PostgreSQL" ]
        jdbcResolver.dbNames >> dbNames

        and:
        final def cacheProvider = new SpringCacheService.CacheProvider("Redis", STANDALONE)
        springCacheService.getCacheProvider() >> cacheProvider
        optionalSpringCacheService.getIfAvailable() >> springCacheService

        when:
        def dbs = databaseService.dbs()

        then:
        assertThat(dbs).containsExactlyInAnyOrderElementsOf(dbNames + cacheProvider.name)
    }

}
