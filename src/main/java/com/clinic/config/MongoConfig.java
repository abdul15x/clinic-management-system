package com.clinic.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Value("${MONGO_HOST:localhost}")
    private String mongoHost;

    @Value("${MONGO_PORT:27017}")
    private int mongoPort;

    @Value("${MONGO_DB:clinic_db}")
    private String databaseName;

    @Bean
    public MongoTemplate mongoTemplate() {
        String connectionString = "mongodb://" + mongoHost + ":" + mongoPort;
        MongoClient mongoClient = MongoClients.create(connectionString);
        return new MongoTemplate(mongoClient, databaseName);
    }
}