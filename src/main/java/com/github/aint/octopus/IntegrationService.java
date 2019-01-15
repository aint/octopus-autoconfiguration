package com.github.aint.octopus;

import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
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
        String servicesEntity = ".services";
        UnaryOperator<String> parseFn = str -> str.substring(integrationPropertyPrefix.length() + servicesEntity.length() + 1);
        return parseEntityNames(propertyNames, integrationPropertyPrefix, servicesEntity, parseFn);
    }

    public Set<String> getLambdaNames() {
        String lambdasEntity = ".lambdas";
        UnaryOperator<String> parseFn = (String s) -> {
            int beginIndex = integrationPropertyPrefix.length() + lambdasEntity.length() + 1;
            int endIndex = s.length() - 5;
            return s.substring(beginIndex, endIndex);
        };
        return parseEntityNames(propertyNames, integrationPropertyPrefix, lambdasEntity, parseFn);
    }

    public Set<String> getThirdPartyNames() {
        String thirdPartyEntity = ".third-party";
        UnaryOperator<String> parseFn = str -> str.substring(integrationPropertyPrefix.length() + thirdPartyEntity.length() + 1);
        return parseEntityNames(propertyNames, integrationPropertyPrefix, thirdPartyEntity, parseFn);
    }

    private Set<String> parseEntityNames(Set<String> strings,
                                         String integrationPrefix,
                                         String entity,
                                         Function<String, String> parseFn) {
        return strings.stream()
                .filter(s -> s.startsWith(integrationPrefix + entity))
                .map(parseFn)
                .collect(Collectors.toSet());
    }

}
