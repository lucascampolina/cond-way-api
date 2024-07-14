package com.camps.condway.config;

import brave.Tracing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracerConfig {

    @Bean
    public Tracing tracing() {
        return Tracing.newBuilder()
                .localServiceName("cond-way")
                .build();
    }

    @Bean
    public brave.Tracer tracer(Tracing tracing) {
        return tracing.tracer();
    }
}