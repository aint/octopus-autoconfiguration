package com.github.aint.octopus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;

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

        List<String> deps = strings.stream()
                .filter(s -> s.startsWith(integrationPrefix))
                .map(s -> env.getProperty(s))
                .collect(Collectors.toList());

        String serviceName = strings.stream()
                .filter(s -> s.contains("application.name"))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);


        Map<String, Object> map = new HashMap<>();
        map.put(serviceName, deps);

        return map;
    }

}
