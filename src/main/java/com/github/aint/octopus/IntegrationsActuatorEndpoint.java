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

        Set<String> services = parseEntityNames(strings, integrationPrefix, ".services");

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

        Set<String> thirdParty = parseEntityNames(strings, integrationPrefix, ".third-party");

        deps.put(DependencyJson.DependencyType.THIRD_PARTY, thirdParty);

        String serviceName = getPropertyValue(strings, "application.name")
                .orElseThrow(() -> new NoSuchElementException("application.name property not found"));

        deps.put(DependencyJson.DependencyType.DATABASES, JdbcResolver.getDbNames());

        String serviceMetadata = String.format("%s %s", getJavaVersion(), getSpringVersion());
        return new DependencyJson(DependencyJson.EventType.CREATE, serviceName, serviceMetadata, deps);
    }

    private Set<String> parseEntityNames(Set<String> strings, String integrationPrefix, String entity) {
        return strings.stream()
                .filter(s -> s.startsWith(integrationPrefix + entity))
                .map(s -> s.substring(integrationPrefix.length() + entity.length() + 1))
                .collect(Collectors.toSet());
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
