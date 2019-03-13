package com.github.aint.octopus;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringApplicationMetadata {

    private static final Pattern SPRING_VERSION_PATTERN = Pattern.compile("(\\d\\.\\d)\\.\\d+\\.[\\w]+");
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

    public String getSpringVersion() {
        Matcher matcher = SPRING_VERSION_PATTERN.matcher(Objects.requireNonNull(SpringVersion.getVersion()));
        if (matcher.matches()) {
            return matcher.group(1);
        }
        throw new RuntimeException("Unknown Spring version");
    }

}
