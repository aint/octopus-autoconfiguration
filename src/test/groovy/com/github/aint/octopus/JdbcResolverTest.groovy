package com.github.aint.octopus

import spock.lang.Specification
import spock.lang.Unroll

class JdbcResolverTest extends Specification {

    @Unroll
    def "jdbc driver #driverName is #dbName db"() {
        expect:
        JdbcResolver.getDbName(driverName) == dbName

        where:
        driverName                        || dbName
        "org.postgresql.Driver"           || "PostgreSql"
        "oracle.jdbc.driver.OracleDriver" || "Oracle"
        "com.mysql.cj.jdbc.Driver"        || "MySQL"
        ""                                || "Unknown DB"
    }
}

