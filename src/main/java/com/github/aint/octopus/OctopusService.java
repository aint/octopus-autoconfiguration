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
