package com.github.aint.octopus;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

@Slf4j
@Endpoint(id = "integrations")
public class IntegrationsActuatorEndpoint {

    @Autowired
    private IntegrationService integrationService;

    @Autowired
    private SpringApplicationMetadata springApplicationMetadata;

    @ReadOperation
    public DependencyJson integrations() {
        Map<DependencyJson.DependencyType, Set<String>> deps = new EnumMap<>(DependencyJson.DependencyType.class);
        deps.put(DependencyJson.DependencyType.SERVICES, integrationService.getServiceNames());
        deps.put(DependencyJson.DependencyType.LAMBDAS, integrationService.getLambdaNames());
        deps.put(DependencyJson.DependencyType.THIRD_PARTY, integrationService.getThirdPartyNames());
        deps.put(DependencyJson.DependencyType.DATABASES, JdbcResolver.getDbNames());

        String appName = springApplicationMetadata.getApplicationName();
        String appMetadata = springApplicationMetadata.getApplicationMetadata();
        return new DependencyJson(DependencyJson.EventType.CREATE, appName, appMetadata, deps);
    }

}
