package edu.hm.ba.serverless.config;

import dagger.Module;
import dagger.Provides;
import edu.hm.ba.serverless.dao.StatisticDao;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Module
public class StatisticModule {

    @Singleton
    @Provides
    @Named("statisticTableName")
    String statisticTableName() {
        return Optional.ofNullable(System.getenv("TABLE_NAME")).orElse("statistics");
    }

    @Singleton
    @Provides
    public StatisticDao statisticDao(DynamoDbClient dynamoDb, @Named("statisticTableName") String tableName) {
        return new StatisticDao(dynamoDb, tableName);
    }

}
