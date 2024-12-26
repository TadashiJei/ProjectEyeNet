package com.eyenet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories(basePackages = "com.eyenet.repository.mongodb")
@EnableJpaRepositories(basePackages = "com.eyenet.repository.jpa")
public class EyeNetApplication {
    public static void main(String[] args) {
        SpringApplication.run(EyeNetApplication.class, args);
    }
}
