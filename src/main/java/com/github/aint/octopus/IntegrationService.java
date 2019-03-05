package com.github.aint.octopus;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

public class IntegrationService {

    private final Set<String> propertyNames;
    private final String integrationPropertyPrefix;

    @Autowired
    public IntegrationService(ApplicationPropertiesProvider propertiesProvider, String integrationPropertyPrefix) {
        propertyNames = propertiesProvider.getPropertyNames();
        this.integrationPropertyPrefix = integrationPropertyPrefix;
    }

    public Set<String> getServiceNames() {
        return parseEntityNames("services");
    }

    public Set<String> getLambdaNames() {
        return parseEntityNames("lambdas");
    }

    public Set<String> getThirdPartyNames() {
        return parseEntityNames("third-party");
    }

    private Set<String> parseEntityNames(String entity) {
        Pattern pattern = Pattern.compile(integrationPropertyPrefix + "\\." + entity + "\\.([^.]+)\\..+");

        return propertyNames.stream()
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(m -> m.group(1))
                .collect(Collectors.toSet());
    }

}
