package com.github.aint.octopus;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.web.client.RestTemplate;

public class ApplicationListenerBean implements ApplicationListener {

    private final String octopusUrl;
    private final OctopusService octopusService;
    private final RestTemplate restTemplate;

    public ApplicationListenerBean(String octopusUrl, OctopusService octopusService, RestTemplate restTemplate) {
        this.octopusUrl = octopusUrl;
        this.octopusService = octopusService;
        this.restTemplate = restTemplate;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            DependencyJson json = octopusService.createEvent();
            sendPostRequest(json);
        } else if (event instanceof ContextStoppedEvent) {
            DependencyJson json = octopusService.destroyEvent();
            sendPostRequest(json);
        }
    }

    private void sendPostRequest(DependencyJson json) {
        restTemplate.postForEntity(octopusUrl, json, Void.class);
    }

}
