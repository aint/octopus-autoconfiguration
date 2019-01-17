package com.github.aint.octopus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;

public class SpringApplicationMetadata {

    @Autowired
    private Environment environment;

    public String getApplicationName() {
        return environment.getRequiredProperty("application.name");
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
