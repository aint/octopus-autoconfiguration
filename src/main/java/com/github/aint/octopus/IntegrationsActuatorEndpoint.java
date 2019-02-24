package com.github.aint.octopus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Endpoint(id = "createEvent")
@RequiredArgsConstructor
public class IntegrationsActuatorEndpoint {

    private final OctopusService octopusService;

    @ReadOperation
    public DependencyJson integrations() {
        return octopusService.createEvent();
    }

}
