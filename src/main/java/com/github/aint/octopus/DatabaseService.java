/*
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

import java.util.HashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseService {

    private final JdbcResolver jdbcResolver;
    private final ObjectProvider<SpringCacheService> optionalSpringCacheService;

    public Set<String> dbs() {
        log.warn("DBS");
        Set<String> dbs = new HashSet<>(jdbcResolver.getDbNames());
        log.warn("DBS: jdbc {}", dbs);
        optionalSpringCacheService.ifAvailable(springCacheService -> {
            SpringCacheService.CacheProvider cacheProvider = springCacheService.getCacheProvider();
            log.warn("DBS: cache {}", cacheProvider);
            if (cacheProvider.getType() == SpringCacheService.CacheProviderType.STANDALONE) {
                dbs.add(cacheProvider.getName());
            }
        });
        return dbs;
    }

    // add NoSQL support

}
