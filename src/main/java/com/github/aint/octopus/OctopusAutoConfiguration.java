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
