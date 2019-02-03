package edu.hm.ba.serverless.dao;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import edu.hm.ba.serverless.exception.CouldNotCreateStatisticException;
import edu.hm.ba.serverless.exception.TableDoesNotExistException;
import edu.hm.ba.serverless.model.Category;
import edu.hm.ba.serverless.model.Statistic;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticDao {

    private static final String STATISTIC_ID = "category";

    private final String tableName;
    private final DynamoDbClient dynamoDb;

    public StatisticDao (final DynamoDbClient dynamoDb, final String tableName) {
        this.dynamoDb = dynamoDb;
        this.tableName = tableName;
    }

    public Statistic getStatistic(final Category category) {
        try {
            return Optional.ofNullable(
                    dynamoDb.getItem(GetItemRequest.builder()
                            .tableName(tableName)
                            .key(Collections.singletonMap(STATISTIC_ID,
                                    AttributeValue.builder().s(category.toString()).build()))
                            .build()))
                    .map(GetItemResponse::item)
                    .map(this::convert)
                    .orElse(null);
        } catch (software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException e) {
            throw new TableDoesNotExistException("Statistic table " + tableName + " does not exist.");
        }
    }

    public List<Statistic> getStatistics() {
        final ScanResponse result;
        try {
            ScanRequest.Builder scanBuilder = ScanRequest.builder()
                    .tableName(tableName);
            result = dynamoDb.scan(scanBuilder.build());
        } catch (software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException e) {
            throw new TableDoesNotExistException("Statistic table " + tableName + " does not exist.");
        }
        final List<Statistic> statistics = result.items().stream()
                .map(this::convert)
                .collect(Collectors.toList());
        return statistics;
    }

    public Statistic createStatistic(final Statistic statistic) {
        if (statistic == null) {
            throw new IllegalArgumentException("Statistic was null.");
        }
        int tries = 0;
        while (tries < 10) {
            try {
                Map<String, AttributeValue> item = createStatisticItem(statistic);
                dynamoDb.putItem(PutItemRequest.builder()
                        .tableName(tableName)
                        .item(item)
                        .conditionExpression("attribute_not_exists(statisticID)")
                        .build());
                return Statistic.builder()
                        .category(Category.valueOf(item.get(STATISTIC_ID).s()))
                        .count(Integer.valueOf(item.get("count").s()))
                        .build();
            } catch (ConditionalCheckFailedException e) {
                tries++;
            } catch (ResourceNotFoundException e) {
                throw new TableDoesNotExistException("Statistic table " + tableName + " does not exist.");
            }
        }
        throw new CouldNotCreateStatisticException("Unable to generate unique book id after 10 tries");
    }

    private Map<String, AttributeValue> createStatisticItem(final Statistic statistic) {
        Map<String, AttributeValue> item = new HashMap<>();
        try {
            item.put(STATISTIC_ID, AttributeValue.builder().s(statistic.getCategory().toString()).build());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("category was null");
        }
        try {
            item.put("count", AttributeValue.builder().s(Integer.toString(statistic.getCount())).build());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("count was null");
        }
        return item;
    }

    private Statistic convert(final Map<String, AttributeValue> item) {
        if (item == null || item.isEmpty()) {
            return null;
        }
        Statistic.StatisticBuilder builder = Statistic.builder();
        try {
            builder.category(Category.valueOf(item.get(STATISTIC_ID).s()));
        } catch (NullPointerException e) {
            throw new IllegalStateException("item did not have a category attribute or it was not a enum");
        }
        try {
            builder.count(Integer.valueOf(item.get("count").s()));
        } catch (NullPointerException e) {
            throw new IllegalStateException("item did not have a count attribute or it was not a integer");
        }
        return builder.build();
    }

}
