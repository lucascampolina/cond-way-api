package com.camps.condway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CondWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CondWayApplication.class, args);
    }

}
