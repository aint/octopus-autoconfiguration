package com.github.aint.octopus;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OctopusService {

    @Autowired
    private IntegrationService integrationService;

    @Autowired
    private SpringApplicationMetadata springApplicationMetadata;

    public DependencyJson createEvent() {
        Map<DependencyJson.DependencyType, Set<String>> deps = new EnumMap<>(DependencyJson.DependencyType.class);
        deps.put(DependencyJson.DependencyType.SERVICES, integrationService.getServiceNames());
        deps.put(DependencyJson.DependencyType.LAMBDAS, integrationService.getLambdaNames());
        deps.put(DependencyJson.DependencyType.THIRD_PARTY, integrationService.getThirdPartyNames());
        deps.put(DependencyJson.DependencyType.DATABASES, JdbcResolver.getDbNames());

        String appName = springApplicationMetadata.getApplicationName();
        String appMetadata = springApplicationMetadata.getApplicationMetadata();
        return new DependencyJson(DependencyJson.EventType.CREATE, appName, appMetadata, deps);
    }

    public DependencyJson destroyEvent() {
        String appName = springApplicationMetadata.getApplicationName();
        return new DependencyJson(DependencyJson.EventType.DELETE, appName, null, null);
    }

}
