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

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackageClasses = OctopusAutoConfiguration.class)
public class OctopusAutoConfiguration {

    @Bean
    @ConditionalOnBean(CacheManager.class)
    public SpringCacheService springCacheService(CacheManager cacheManager) {
        return new SpringCacheService(cacheManager);
    }

    @Bean
    public IntegrationService integrationService(ApplicationPropertiesProvider propertiesProvider) {
        String integrationPrefix = propertiesProvider
                .getOptionProperty("octopus.integration.prefix")
                .orElseGet(() -> "integration");
        return new IntegrationService(propertiesProvider, integrationPrefix);
    }

    @Bean
    public ApplicationListenerBean applicationListenerBean(OctopusService octopusService, Environment environment, RestTemplate restTemplate) {
        String url = environment.getRequiredProperty("octopus-server.url");
        return new ApplicationListenerBean(url, octopusService, restTemplate);
    }

}
