/*
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
package com.github.aint.octopus;

import java.sql.DriverManager;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class JdbcResolver {

    private static final String MY_SQL = "MySQL";
    private static final String MARIA_DB = "MariaDB";
    private static final String ORACLE = "Oracle";
    private static final String DB_2 = "DB2";
    private static final String SYBASE = "Sybase";
    private static final String TERADATA = "Teradata";
    private static final String SQL_SERVER = "SQLServer";
    private static final String POSTGRE_SQL = "PostgreSql";

    public Set<String> getDbNames() {
        return Collections.list(DriverManager.getDrivers()).stream()
                .map(driver -> driver.getClass().getName())
                .map(this::getDbName)
                .collect(Collectors.toSet());
    }

    private String getDbName(String jdbcDriverClassName) {
        if (jdbcDriverClassName.contains(MY_SQL.toLowerCase())) {
            return MY_SQL;
        } else if (jdbcDriverClassName.contains(MARIA_DB.toLowerCase())) {
            return MARIA_DB;
        } else if (jdbcDriverClassName.contains(ORACLE.toLowerCase())) {
            return ORACLE;
        } else if (jdbcDriverClassName.contains(DB_2.toLowerCase())) {
            return DB_2;
        } else if (jdbcDriverClassName.contains(SYBASE.toLowerCase())) {
            return SYBASE;
        } else if (jdbcDriverClassName.contains(TERADATA.toLowerCase())) {
            return TERADATA;
        } else if (jdbcDriverClassName.contains(SQL_SERVER.toLowerCase())) {
            return SQL_SERVER;
        } else if (jdbcDriverClassName.contains(POSTGRE_SQL.toLowerCase())) {
            return POSTGRE_SQL;
        } else {
            return "Unknown DB";
        }
    }

}
