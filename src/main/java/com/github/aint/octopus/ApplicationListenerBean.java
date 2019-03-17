/**
 * Copyright (C) 2018 - 2019 Oleksandr Tyshkovets <olexandr.tyshkovets@gmail.com>
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
