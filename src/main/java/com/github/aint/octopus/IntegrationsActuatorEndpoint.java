package com.github.aint.octopus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.core.SpringVersion;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Endpoint(id = "integrations")
public class IntegrationsActuatorEndpoint {

    @Autowired
    private ApplicationPropertiesProvider propertiesProvider;

    @ReadOperation
    public DependencyJson integrations() {
        Set<String> strings = propertiesProvider.getPropertyNames();

        String integrationPrefix = getPropertyValue(strings,"octopus.integration.prefix").orElse("integration.base-url");

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

        String serviceName = getPropertyValue(strings, "application.name")
                .orElseThrow(() -> new NoSuchElementException("application.name property not found"));

        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Set<String> databases = new HashSet<>();
        while (drivers.hasMoreElements()) {
            String jdbcDriverName = drivers.nextElement().getClass().getName();
            log.info(jdbcDriverName);
            databases.add(JdbcResolver.getDbName(jdbcDriverName));
        }
        deps.put(DependencyJson.DependencyType.DATABASES, databases);

        String serviceMetadata = String.format("%s %s", getJavaVersion(), getSpringVersion());
        return new DependencyJson(DependencyJson.EventType.CREATE, serviceName, serviceMetadata, deps);
    }

    private Optional<String> getPropertyValue(Set<String> properties, String propertyName) {
        return properties.stream()
                .filter(s -> s.equals(propertyName))
                .map(s -> propertiesProvider.getProperty(s))
                .findFirst();
    }


    private static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    private static String getSpringVersion() {
        return SpringVersion.getVersion();
    }

}
