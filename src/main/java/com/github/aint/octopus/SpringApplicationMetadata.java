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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cache.CacheManager;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringApplicationMetadata {

    private static final Pattern SPRING_VERSION_PATTERN = Pattern.compile("(\\d\\.\\d)\\.\\d+\\.[\\w]+");

    private final Environment environment;
    private final ObjectProvider<CacheManager> optionalCacheManager;

    public String getApplicationName() {
        return environment.getRequiredProperty("spring.application.name");
    }

    public boolean isCachingEnabled() {
        return optionalCacheManager.getIfAvailable() != null;
    }

    public int getJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2);
        }

        int dotPos = version.indexOf('.');
        int dashPos = version.indexOf('-');
        return Integer.parseInt(version.substring(0, dotPos > -1 ? dotPos : dashPos > -1 ? dashPos : 1));
    }

    public String getSpringVersion() {
        Matcher matcher = SPRING_VERSION_PATTERN.matcher(Objects.requireNonNull(SpringVersion.getVersion()));
        if (matcher.matches()) {
            return matcher.group(1);
        }
        throw new RuntimeException("Unknown Spring version");
    }

}
