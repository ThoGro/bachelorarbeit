package edu.hm.ba.serverless.handler;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import edu.hm.ba.serverless.dao.StatisticDao;
import edu.hm.ba.serverless.model.Category;
import edu.hm.ba.serverless.model.Statistic;
import edu.hm.ba.serverless.model.response.GatewayResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CreateStatisticHandlerTest {

    private static final Statistic STATISTIC = new Statistic(12, Category.HISTORY);

    private Statistic statistic;

    private StatisticDao statisticDao;

    @Before
    public void setUp() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();
        statisticDao = new StatisticDao(client, "statistics");
        statistic = statisticDao.getStatistic(Category.HISTORY);
    }

    @After
    public void cleanUp() {
        statisticDao.createStatistic(statistic);
    }

    @Test
    public void successfulResponse() throws JSONException {
        CreateStatisticHandler createStatisticHandler = new CreateStatisticHandler();
        Map<String, Object> input = new HashMap<>();
        JSONObject json  = new JSONObject();
        json.put("category", STATISTIC.getCategory());
        json.put("statisticCount", STATISTIC.getStatisticCount());
        input.put("body", json);
        GatewayResponse response = createStatisticHandler.handleRequest(input, null);
        assertEquals(201, response.getStatusCode());
        assertEquals("{\"statisticCount\":12,\"category\":\"HISTORY\"}",
                response.getBody());
    }

}
