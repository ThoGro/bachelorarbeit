package edu.hm.ba.serverless.dao;

import edu.hm.ba.serverless.model.Category;
import edu.hm.ba.serverless.model.Statistic;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class StatisticDaoTest {

    private static final Statistic STATISTIC = new Statistic(12, Category.HISTORY);

    private static final String TABLE_NAME = "statistics";

    private StatisticDao statisticDao;

    @Mock
    private DynamoDbClient dynamoDb;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        statisticDao = new StatisticDao(dynamoDb, TABLE_NAME);
    }

    @Test
    public void testGetStatistic() {
        GetItemResponse getItemResponse = GetItemResponse.builder().item(createResultMap()).build();
        when(dynamoDb.getItem(any(GetItemRequest.class))).thenReturn(getItemResponse);
        Statistic statistic = statisticDao.getStatistic(STATISTIC.getCategory());
        assertEquals(STATISTIC, statistic);
    }

    @Test
    public void testGetStatistics(){
        ScanResponse result = ScanResponse.builder().items(createResultMap()).build();
        when(dynamoDb.scan(any(ScanRequest.class))).thenReturn(result);
        List<Statistic> statistics = statisticDao.getStatistics();
        assertEquals(Arrays.asList(STATISTIC), statistics);
    }

    @Test
    public void testCreateStatistic() {
        when(dynamoDb.putItem(any(PutItemRequest.class))).thenReturn(any(PutItemResponse.class));
        Statistic statistic = statisticDao.createStatistic(new Statistic(STATISTIC.getStatisticCount(), STATISTIC.getCategory()));
        assertEquals(STATISTIC, statistic);
    }

    @Test
    public void testCreateStatisticCouldNotCreateStatisticException() {
        exception.expect(IllegalArgumentException.class);
        statisticDao.createStatistic(null);
    }

    @Test
    public void testCount() {
        Map<String, AttributeValue> resultMap = createResultMap();
        resultMap.replace("statisticCount", AttributeValue.builder().s(Integer.toString(STATISTIC.getStatisticCount() + 1)).build());
        Statistic updatedStatistic = new Statistic(13, Category.HISTORY);
        UpdateItemResponse updateItemResponse = UpdateItemResponse.builder().attributes(resultMap).build();
        when(dynamoDb.updateItem(any(UpdateItemRequest.class))).thenReturn(updateItemResponse);
        //Aufruf von getStatistic() in count()
        GetItemResponse getItemResponse = GetItemResponse.builder().item(createResultMap()).build();
        when(dynamoDb.getItem(any(GetItemRequest.class))).thenReturn(getItemResponse);
        Statistic statistic = statisticDao.count(STATISTIC.getCategory());
        assertEquals(updatedStatistic, statistic);
    }

    private Map<String, AttributeValue> createResultMap() {
        Map<String, AttributeValue> resultMap = new HashMap<>();
        resultMap.put("category", AttributeValue.builder().s(STATISTIC.getCategory().toString()).build());
        resultMap.put("statisticCount", AttributeValue.builder().s(Integer.toString(STATISTIC.getStatisticCount())).build());
        return resultMap;
    }

}
