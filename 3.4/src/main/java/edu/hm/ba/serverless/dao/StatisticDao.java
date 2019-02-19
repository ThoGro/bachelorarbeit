package edu.hm.ba.serverless.dao;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import edu.hm.ba.serverless.exception.CouldNotCreateStatisticException;
import edu.hm.ba.serverless.exception.TableDoesNotExistException;
import edu.hm.ba.serverless.exception.UnableToUpdateException;
import edu.hm.ba.serverless.model.Category;
import edu.hm.ba.serverless.model.Statistic;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticDao {

    private static final String STATISTIC_ID = "category";
    private static final String UPDATE_EXPRESSION = "SET statisticCount = :c";

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
                        .statisticCount(Integer.valueOf(item.get("statisticCount").s()))
                        .build();
            } catch (ConditionalCheckFailedException e) {
                tries++;
            } catch (ResourceNotFoundException e) {
                throw new TableDoesNotExistException("Statistic table " + tableName + " does not exist.");
            }
        }
        throw new CouldNotCreateStatisticException("Unable to generate unique book id after 10 tries");
    }

    public Statistic count(final Category category) {
        Statistic toCount = getStatistic(category);
        toCount.setStatisticCount(toCount.getStatisticCount() + 1);

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":c", AttributeValue.builder().s(Integer.toString(toCount.getStatisticCount())).build());
        final UpdateItemResponse result;
        try {
            result = dynamoDb.updateItem(UpdateItemRequest.builder()
                    .tableName(tableName)
                    .key(Collections.singletonMap(STATISTIC_ID,
                            AttributeValue.builder().s(toCount.getCategory().toString()).build()))
                    .returnValues(ReturnValue.ALL_NEW)
                    .updateExpression(UPDATE_EXPRESSION)
                    .conditionExpression("attribute_exists(category)")
                    .expressionAttributeValues(expressionAttributeValues)
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new UnableToUpdateException("the statistic does not exist");
        } catch (ResourceNotFoundException e) {
            throw new TableDoesNotExistException("Statistic table " + tableName
                    + " does not exist and was deleted after reading the statistic");
        }
        return convert(result.attributes());
    }

    private Map<String, AttributeValue> createStatisticItem(final Statistic statistic) {
        Map<String, AttributeValue> item = new HashMap<>();
        try {
            item.put(STATISTIC_ID, AttributeValue.builder().s(statistic.getCategory().toString()).build());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("category was null");
        }
        try {
            item.put("statisticCount", AttributeValue.builder().s(Integer.toString(statistic.getStatisticCount())).build());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("statisticCount was null");
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
            builder.statisticCount(Integer.valueOf(item.get("statisticCount").s()));
        } catch (NullPointerException e) {
            throw new IllegalStateException("item did not have a statisticCount attribute or it was not a integer");
        }
        return builder.build();
    }

}
