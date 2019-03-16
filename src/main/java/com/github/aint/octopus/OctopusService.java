package com.github.aint.octopus;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OctopusService {

    private final DatabaseService databaseService;
    private final IntegrationService integrationService;
    private final SpringApplicationMetadata springAppMetadata;

    public DependencyJson createEvent() {
        Map<DependencyJson.DependencyType, Set<String>> deps = new EnumMap<>(DependencyJson.DependencyType.class);
        deps.put(DependencyJson.DependencyType.SERVICES, integrationService.getServiceNames());
        deps.put(DependencyJson.DependencyType.LAMBDAS, integrationService.getLambdaNames());
        deps.put(DependencyJson.DependencyType.THIRD_PARTY, integrationService.getThirdPartyNames());
        deps.put(DependencyJson.DependencyType.DATABASES, databaseService.dbs());

        String appName = springAppMetadata.getApplicationName();
        String appMetadata = String.format("Java %d, Spring %s %s",
                springAppMetadata.getJavaVersion(),
                springAppMetadata.getSpringVersion(),
                springAppMetadata.isCachingEnabled() ? "| :cache:" : "");
        return new DependencyJson(DependencyJson.EventType.CREATE, appName, "svc", appMetadata, deps);
    }

    public DependencyJson destroyEvent() {
        String appName = springAppMetadata.getApplicationName();
        return new DependencyJson(DependencyJson.EventType.DELETE, appName, null, null, null);
    }

}
