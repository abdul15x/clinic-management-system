package com.clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ClinicManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClinicManagementSystemApplication.class, args);
    }
}