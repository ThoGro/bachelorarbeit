package edu.hm.ba.serverless.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import edu.hm.ba.serverless.dao.BookDao;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Module
public class BookModule {

    @Singleton
    @Provides
    @Named("tableName")
    String tableName() {
        return Optional.ofNullable(System.getenv("TABLE_NAME")).orElse("books");
    }

    @Singleton
    @Provides
    DynamoDbClient dynamoDb() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();
        return client;
    }

    @Singleton
    @Provides
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Singleton
    @Provides
    public BookDao bookDao(DynamoDbClient dynamoDb, @Named("tableName") String tableName) {
        return new BookDao(dynamoDb, tableName);
    }

}
