package com.github.aint.octopus;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringApplicationMetadata {

    private final Environment environment;

    public String getApplicationName() {
        return environment.getRequiredProperty("spring.application.name");
    }

    public String getApplicationMetadata() {
        return String.format("%s %s", getJavaVersion(), getSpringVersion());
    }

    private static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    private static String getSpringVersion() {
        return SpringVersion.getVersion();
    }

}
