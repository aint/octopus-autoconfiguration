package com.github.aint.octopus;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cache.CacheManager;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringApplicationMetadata {

    private static final Pattern SPRING_VERSION_PATTERN = Pattern.compile("(\\d\\.\\d)\\.\\d+\\.[\\w]+");

    private final Environment environment;
    private final ObjectProvider<CacheManager> optionalCacheManager;

    public String getApplicationName() {
        return environment.getRequiredProperty("spring.application.name");
    }

    public boolean isCachingEnabled() {
        return optionalCacheManager.getIfAvailable() != null;
    }

    public int getJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2);
        }

        int dotPos = version.indexOf('.');
        int dashPos = version.indexOf('-');
        return Integer.parseInt(version.substring(0, dotPos > -1 ? dotPos : dashPos > -1 ? dashPos : 1));
    }

    public String getSpringVersion() {
        Matcher matcher = SPRING_VERSION_PATTERN.matcher(Objects.requireNonNull(SpringVersion.getVersion()));
        if (matcher.matches()) {
            return matcher.group(1);
        }
        throw new RuntimeException("Unknown Spring version");
    }

}
