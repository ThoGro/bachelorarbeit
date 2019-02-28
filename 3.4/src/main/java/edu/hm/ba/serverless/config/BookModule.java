package edu.hm.ba.serverless.config;

import dagger.Module;
import dagger.Provides;
import edu.hm.ba.serverless.dao.BookDao;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.inject.Named;
import java.util.Optional;

/**
 * Module to provide dependencies for the BookDao.
 */
@Module
public class BookModule {

    /**
     * Provides the table name as part of the environment variables.
     * @return the table name
     */
    @Provides
    @Named("bookTableName")
    public String bookTableName() {
        return Optional.ofNullable(System.getenv("TABLE_NAME")).orElse("books");
    }

    /**
     * Provides the BookDao with the database client and the table name.
     * @param dynamoDb the DynamoDbClient
     * @param tableName the table name
     * @return the BookDao
     */
    @Provides
    public BookDao bookDao(DynamoDbClient dynamoDb, @Named("bookTableName") String tableName) {
        return new BookDao(dynamoDb, tableName);
    }

}
