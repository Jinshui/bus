package com.bus.services.config;

import com.bus.services.exceptions.ApplicationException;
import com.bus.services.util.MongoUtils;
import com.mongodb.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;

import java.util.List;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(MongoConfig.class);

    @Value("${mongo.host}")
    private String replicaSet = null;
    @Value("${mongo.database}")
    private String databaseName = null;
    @Value("${mongo.auth.username}")
    private String username = null;
    @Value("${mongo.auth.password}")
    private String password = null;

    @Override
    public String getDatabaseName() {
        return this.databaseName;
    }

    @Override
    public UserCredentials getUserCredentials() {
        UserCredentials credentials = null;
        if (StringUtils.isNotBlank(this.username) && StringUtils.isNotBlank(this.password)) {
            credentials = new UserCredentials(this.username, this.password);
        }
        return credentials;
    }

    @Override
    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        try {
            MongoDbFactory mongoDbFactory = super.mongoDbFactory();
            DB db = mongoDbFactory.getDb(this.getDatabaseName());
            db.getCollectionNames();
            return mongoDbFactory;
        }
        catch (Exception e) {
            throw new ApplicationException("Error initializing MongoDbFactory", e);
        }
    }

    @Override
    @Bean
    public MongoClient mongo() throws Exception {
        MongoClientOptions options = new MongoClientOptions.Builder()
                .autoConnectRetry(true)
                .connectionsPerHost(40)
                .description("MetaMore MongoClient")
                .readPreference(ReadPreference.secondaryPreferred())
                .threadsAllowedToBlockForConnectionMultiplier(1500)
                .build();

        List<ServerAddress> serverAddresses = MongoUtils.getServerAddresses(replicaSet);

        MongoClient mongoClient = new MongoClient(serverAddresses, options);
        DB db = mongoClient.getDB(databaseName);

        if (StringUtils.isNotBlank(this.username) && StringUtils.isNotBlank(this.password)) {
            if (!db.authenticate(username, password.toCharArray())) {
                throw new ApplicationException(String.format("Authentication to database [%s] failed.  Tried %s/%s", databaseName, username, password));
            }
        }

        return mongoClient;
    }

    @Override
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate template = super.mongoTemplate();
        template.setWriteConcern(WriteConcern.SAFE);
        template.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        return template;
    }
}
