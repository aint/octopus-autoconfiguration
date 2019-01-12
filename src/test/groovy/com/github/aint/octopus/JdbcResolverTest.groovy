package com.github.aint.octopus

import spock.lang.Specification
import spock.lang.Unroll

import java.sql.Driver
import java.sql.DriverManager

class JdbcResolverTest extends Specification {

    @Unroll
    def "getDbNames jdbc drivers #driverName is #dbName"() {
        given:
        DriverManager.getDrivers().each { DriverManager.deregisterDriver(it); }
        and:
        DriverManager.registerDriver(Class.forName(driverName).newInstance() as Driver)

        when:
        def dbNames = JdbcResolver.getDbNames()

        then:
        dbNames.size() == 1
        and:
        dbNames[0] == dbName

        where:
        driverName                                     || dbName
        "org.h2.Driver"                                || "Unknown DB"
        "org.postgresql.Driver"                        || "PostgreSql"
        "com.mysql.cj.jdbc.Driver"                     || "MySQL"
        "com.microsoft.sqlserver.jdbc.SQLServerDriver" || "SQLServer"
    }

    def "getDbNames with no jdbc drivers"() {
        given:
        DriverManager.getDrivers().each { DriverManager.deregisterDriver(it); }

        when:
        def dbNames = JdbcResolver.getDbNames()

        then:
        dbNames.isEmpty()
    }

}

