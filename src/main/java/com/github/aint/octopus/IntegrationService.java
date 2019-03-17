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

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

public class IntegrationService {

    private final Set<String> propertyNames;
    private final String integrationPropertyPrefix;

    @Autowired
    public IntegrationService(ApplicationPropertiesProvider propertiesProvider, String integrationPropertyPrefix) {
        propertyNames = propertiesProvider.getPropertyNames();
        this.integrationPropertyPrefix = integrationPropertyPrefix;
    }

    public Set<String> getServiceNames() {
        return parseEntityNames("services");
    }

    public Set<String> getLambdaNames() {
        return parseEntityNames("lambdas");
    }

    public Set<String> getThirdPartyNames() {
        return parseEntityNames("third-party");
    }

    private Set<String> parseEntityNames(String entity) {
        Pattern pattern = Pattern.compile(integrationPropertyPrefix + "\\." + entity + "\\.([^.]+)\\..+");

        return propertyNames.stream()
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(m -> m.group(1))
                .collect(Collectors.toSet());
    }

}
