package com.github.aint.octopus;

import java.util.EnumMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.core.SpringVersion;

@Slf4j
@Endpoint(id = "integrations")
public class IntegrationsActuatorEndpoint {

    @Autowired
    private ApplicationPropertiesProvider propertiesProvider;

    @Autowired
    private IntegrationService integrationService;

    @ReadOperation
    public DependencyJson integrations() {



        Set<String> strings = propertiesProvider.getPropertyNames();

        Map<DependencyJson.DependencyType, Set<String>> deps = new EnumMap<>(DependencyJson.DependencyType.class);
        deps.put(DependencyJson.DependencyType.SERVICES, integrationService.getServiceNames());
        deps.put(DependencyJson.DependencyType.LAMBDAS, integrationService.getLambdaNames());
        deps.put(DependencyJson.DependencyType.THIRD_PARTY, integrationService.getThirdPartyNames());
        deps.put(DependencyJson.DependencyType.DATABASES, JdbcResolver.getDbNames());

        String serviceName = getPropertyValue(strings, "application.name")
                .orElseThrow(() -> new NoSuchElementException("application.name property not found"));

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
