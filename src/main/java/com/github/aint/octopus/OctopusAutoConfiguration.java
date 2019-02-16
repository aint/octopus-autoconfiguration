package com.github.aint.octopus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackageClasses = OctopusAutoConfiguration.class)
public class OctopusAutoConfiguration {

    @Bean
    public IntegrationService integrationService(ApplicationPropertiesProvider propertiesProvider) {
        String integrationPrefix = propertiesProvider.getProperty("octopus.integration.prefix");
        if (integrationPrefix == null) {
            integrationPrefix = propertiesProvider.getProperty("integration.base-url");
        }
        return new IntegrationService(propertiesProvider, integrationPrefix);
    }

    @Bean
    public ApplicationListenerBean applicationListenerBean(OctopusService octopusService, Environment environment) {
        String url = environment.getRequiredProperty("octopus-server.url");
        return new ApplicationListenerBean(url, octopusService);
    }

}
