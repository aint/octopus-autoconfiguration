package com.github.aint.octopus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OctopusAutoConfiguration {

    @Bean
    public IntegrationsActuatorEndpoint integrationActuatorEndpoint() {
        return new IntegrationsActuatorEndpoint();
    }

}
