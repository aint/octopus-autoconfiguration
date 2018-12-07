package com.github.aint.octopus;

import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DependencyJson {

    private EventType eventType;

    private String serviceName;

    private Map<DependencyType, Set<String>> dependencies;

    public enum EventType {
        CREATE, DELETE
    }

    public enum DependencyType {
        SERVICES, LAMBDAS, DATABASES, THIRD_PARTY
    }

}
