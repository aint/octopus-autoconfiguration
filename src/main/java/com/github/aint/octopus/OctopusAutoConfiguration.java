package com.github.aint.octopus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class OctopusAutoConfiguration {

    @Bean
    public IntegrationsActuatorEndpoint integrationActuatorEndpoint() {
        return new IntegrationsActuatorEndpoint();
    }

    @Bean
    public ApplicationListenerBean applicationListenerBean(IntegrationsActuatorEndpoint integrationsActuatorEndpoint,
                                                           Environment environment) {
        DependencyJson json = integrationsActuatorEndpoint.integrations();
        String url = environment.getRequiredProperty("octopus-server.url");
        return new ApplicationListenerBean(url, json);
    }

}
