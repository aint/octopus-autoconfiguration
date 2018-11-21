package com.github.aint.octopus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Endpoint(id = "integrations")
public class IntegrationsActuatorEndpoint {

    @Autowired
    private ConfigurableEnvironment env;

    @ReadOperation
    public DependencyJson integrations() {
        Set<String> strings = StreamSupport.stream(env.getPropertySources().spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .filter(ps -> ps.getName().contains("applicationConfig:"))
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .collect(Collectors.toSet());

        String integrationPrefix = strings.stream()
                .filter(s -> s.equals("octopus.integration.prefix"))
                .map(s -> env.getProperty(s))
                .findFirst()
                .orElse("integration.base-url");

        Map<String, String> deps = strings.stream()
                .filter(s -> s.startsWith(integrationPrefix))
                .map(s -> s.substring(integrationPrefix.length() + 1))
                .collect(Collectors.toMap(serviceName -> serviceName, x -> "service"));

        String serviceName = strings.stream()
                .filter(s -> s.contains("application.name"))
                .map(s -> env.getProperty(s))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);


        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            String jdbcDriverName = drivers.nextElement().getClass().getName();
            log.info(jdbcDriverName);
            deps.put(DbDependencyResolver.resolveDbName(jdbcDriverName), "database");
        }

        return new DependencyJson(serviceName, deps);
    }

    static class DbDependencyResolver {
        public static String resolveDbName(String jdbcDriverClassName) {
            if (jdbcDriverClassName.contains("mysql")) {
                return "MySQL";
            } else if (jdbcDriverClassName.contains("oracle")) {
                return "Oracle";
            } else if (jdbcDriverClassName.contains("db2")) {
                return "DB2";
            } else if (jdbcDriverClassName.contains("sybase")) {
                return "Sybase";
            } else if (jdbcDriverClassName.contains("teradata")) {
                return "Teradata";
            } else if (jdbcDriverClassName.contains("sqlserver")) {
                return "SQLServer";
            } else if (jdbcDriverClassName.contains("postgresql")) {
                return "PostgreSql";
            } else {
                return "Unknown DB";
            }
        }


    }

}
