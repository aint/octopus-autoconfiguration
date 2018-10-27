package com.github.aint.octopus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Endpoint(id = "integrations")
public class IntegrationsActuatorEndpoint {

    @Autowired
    private ConfigurableEnvironment env;

    @ReadOperation
    public Map<String, Object> integrations() {
        Properties props = new Properties();
        MutablePropertySources propSrcs = env.getPropertySources();
        List<String> deps = StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::<String>stream)
                .filter(s -> s.contains("base-url"))
                .map(s -> env.getProperty(s))
                .collect(Collectors.toList());

        String serviceName = StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::<String>stream)
                .filter(s -> s.contains("application.name"))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);


        Map<String, Object> map = new HashMap<>();
        map.put(serviceName, deps);

        return map;
    }

}
