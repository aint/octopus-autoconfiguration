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

        Set<String> services = strings.stream()
                .filter(s -> s.startsWith(integrationPrefix + ".services"))
                .map(s -> s.substring(integrationPrefix.length() + ".services".length() + 1))
                .collect(Collectors.toSet());

        Map<DependencyJson.DependencyType, Set<String>> deps = new EnumMap<>(DependencyJson.DependencyType.class);
        deps.put(DependencyJson.DependencyType.SERVICES, services);

        Set<String> lambdas = strings.stream()
                .filter(s -> s.startsWith(integrationPrefix + ".lambdas"))
                .map(s -> {
                    int beginIndex = integrationPrefix.length() + ".lambdas".length() + 1;
                    int endIndex = s.length() - 5;
                    return s.substring(beginIndex, endIndex);
                })
                .collect(Collectors.toSet());

        deps.put(DependencyJson.DependencyType.LAMBDAS, lambdas);

        String serviceName = strings.stream()
                .filter(s -> s.contains("application.name"))
                .map(s -> env.getProperty(s))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);


        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Set<String> databases = new HashSet<>();
        while (drivers.hasMoreElements()) {
            String jdbcDriverName = drivers.nextElement().getClass().getName();
            log.info(jdbcDriverName);
            databases.add(DbDependencyResolver.resolveDbName(jdbcDriverName));
        }
        deps.put(DependencyJson.DependencyType.DATABASES, databases);

        return new DependencyJson(DependencyJson.EventType.CREATE, serviceName, deps);
    }

    static class DbDependencyResolver {

        private static final String MY_SQL = "MySQL";
        private static final String ORACLE = "Oracle";
        private static final String DB_2 = "DB2";
        private static final String SYBASE = "Sybase";
        private static final String TERADATA = "Teradata";
        private static final String SQL_SERVER = "SQLServer";
        private static final String POSTGRE_SQL = "PostgreSql";

        public static String resolveDbName(String jdbcDriverClassName) {
            if (jdbcDriverClassName.contains(MY_SQL.toLowerCase())) {
                return MY_SQL;
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

}
