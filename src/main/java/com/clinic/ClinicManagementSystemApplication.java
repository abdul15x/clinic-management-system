package com.clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ClinicManagementSystemApplication {
    public static void main(String[] args) {

        String mongoUri = System.getenv("SPRING_DATA_MONGODB_URI");

        System.out.println("========================================");
        System.out.println("MONGO URI EXISTS: " + (mongoUri != null));
        System.out.println("MONGO URI PREFIX: " +
                (mongoUri != null ? mongoUri.substring(0, Math.min(15, mongoUri.length())) : "NULL"));
        System.out.println("========================================");

        SpringApplication.run(ClinicManagementSystemApplication.class, args);
    }

}