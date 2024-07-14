package com.camps.condway.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    //    @Bean
//    public DataSource dataSource(DataSourceProperties properties) {
//        return DataSourceBuilder.create()
//                .driverClassName(properties.getDriverClassName())
//                .url(properties.getUrl())
//                .username(properties.getUsername())
//                .password(properties.getPassword())
//                .build();
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .username(System.getenv("SPRING_DATASOURCE_USERNAME"))
                .password(System.getenv("SPRING_DATASOURCE_PASSWORD"))
                .url(System.getenv("SPRING_DATASOURCE_URL"))
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
