package com.github.aint.octopus

import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Configuration

@Configuration // we don't need component scan here
class TestBootApp {

    static void main(String[] args) throws Exception {
        SpringApplication.run(TestBootApp, args);
    }

}
