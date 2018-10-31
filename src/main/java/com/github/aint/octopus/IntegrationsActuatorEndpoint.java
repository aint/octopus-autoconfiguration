package com.github.aint.octopus;

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

@Endpoint(id = "integrations")
public class IntegrationsActuatorEndpoint {

    @Autowired
    private ConfigurableEnvironment env;

    @ReadOperation
    public Map<String, Object> integrations() {
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
                .findFirst()
                .orElseThrow(NoSuchElementException::new);


        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            String jdbcDriverName = drivers.nextElement().getClass().getName();
            deps.put(jdbcDriverName, "database");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("name", serviceName);
        map.put("dependencies", deps);

        return map;
    }

}
