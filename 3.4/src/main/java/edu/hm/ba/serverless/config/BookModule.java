package edu.hm.ba.serverless.config;

import dagger.Module;
import dagger.Provides;
import edu.hm.ba.serverless.dao.BookDao;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Module
public class BookModule {

    @Singleton
    @Provides
    @Named("bookTableName")
    String bookTableName() {
        return Optional.ofNullable(System.getenv("TABLE_NAME")).orElse("books");
    }

    @Singleton
    @Provides
    public BookDao bookDao(DynamoDbClient dynamoDb, @Named("bookTableName") String tableName) {
        return new BookDao(dynamoDb, tableName);
    }

}
