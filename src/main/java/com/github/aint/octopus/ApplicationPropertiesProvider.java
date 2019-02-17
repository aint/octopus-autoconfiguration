package com.github.aint.octopus;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationPropertiesProvider {

    private final ConfigurableEnvironment env;

    public Set<String> getPropertyNames() {
        return StreamSupport.stream(env.getPropertySources().spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .filter(ps -> ps.getName().contains("applicationConfig:"))
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .collect(Collectors.toSet());
    }

    public String getProperty(String name) {
        return env.getProperty(name);
    }

    public Optional<String> getOptionProperty(String name) {
        return Optional.ofNullable(env.getProperty(name));
    }

}
