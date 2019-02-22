package edu.hm.ba.serverless.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Module to provide dependencies for injection.
 */
@Module
public class AppModule {

    /**
     * Provides a DynamoDbClient.
     * @return the DynamoDbClient
     */
    @Provides
    DynamoDbClient dynamoDb() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();
        return client;
    }

    /**
     * Provides an object mapper.
     * @return the object mapper
     */
    @Provides
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
