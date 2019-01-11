package com.github.aint.octopus

import spock.lang.Specification
import spock.lang.Unroll

import java.sql.Driver
import java.sql.DriverManager
import java.sql.SQLException

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

    @Unroll
    def "getDbNames jdbc drivers #driverName"() {
        given:
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver d = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(d);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

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

}

