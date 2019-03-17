/**
 * Copyright (C) 2018 - 2019 Oleksandr Tyshkovets <olexandr.tyshkovets@gmail.com>
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.github.aint.octopus

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.sql.Driver
import java.sql.DriverManager

class JdbcResolverTest extends Specification {

    @Subject
    JdbcResolver jdbcResolver = new JdbcResolver()

    @Unroll
    def "getDbNames jdbc drivers #driverName is #dbName"() {
        given:
        DriverManager.getDrivers().each { DriverManager.deregisterDriver(it) }
        and:
        DriverManager.registerDriver(Class.forName(driverName).newInstance() as Driver)

        when:
        def dbNames = jdbcResolver.getDbNames()

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
        DriverManager.getDrivers().each { DriverManager.deregisterDriver(it) }

        when:
        def dbNames = jdbcResolver.getDbNames()

        then:
        dbNames.isEmpty()
    }

}

