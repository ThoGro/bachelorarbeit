package edu.hm.ba.serverless.config;

import dagger.Module;
import dagger.Provides;
import edu.hm.ba.serverless.dao.StatisticDao;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.inject.Named;
import java.util.Optional;

/**
 * Module to provide dependencies for the StatisticDao.
 */
@Module
public class StatisticModule {

    /**
     * Provides the table name as part of the environment variables.
     * @return the table name
     */
    @Provides
    @Named("statisticTableName")
    String statisticTableName() {
        return Optional.ofNullable(System.getenv("TABLE_NAME")).orElse("statistics");
    }

    /**
     * Provides the StatisticDao with the database client and the table name.
     * @param dynamoDb the DynamoDbClient
     * @param tableName the table name
     * @return the StatisticDao
     */
    @Provides
    public StatisticDao statisticDao(DynamoDbClient dynamoDb, @Named("statisticTableName") String tableName) {
        return new StatisticDao(dynamoDb, tableName);
    }

}
