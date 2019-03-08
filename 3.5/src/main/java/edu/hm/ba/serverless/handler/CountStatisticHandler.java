package edu.hm.ba.serverless.handler;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.ba.serverless.config.AppComponent;
import edu.hm.ba.serverless.config.DaggerAppComponent;
import edu.hm.ba.serverless.dao.StatisticDao;
import edu.hm.ba.serverless.model.Category;
import edu.hm.ba.serverless.model.Statistic;
import edu.hm.ba.serverless.model.response.GatewayResponse;

import javax.inject.Inject;
import java.util.Map;

/**
 * Handler for request to CountStatistic Lambda function.
 */
public class CountStatisticHandler implements RequestHandler<DynamodbEvent, GatewayResponse>, ConstantRequestHandler {

    /**
     * Object mapper for serialization.
     */
    @Inject
    ObjectMapper objectMapper;

    /**
     * Data Access Object.
     */
    @Inject
    StatisticDao statisticDao;

    /**
     * Dagger component for dependency injection.
     */
    private final AppComponent appComponent;

    /**
     * Constructor to inject object mapper and dao.
     */
    public CountStatisticHandler() {
        appComponent = DaggerAppComponent.builder().build();
        appComponent.inject(this);
    }

    @Override
    public GatewayResponse handleRequest(DynamodbEvent input, Context context) {
        for (DynamodbEvent.DynamodbStreamRecord record : input.getRecords()) {
            Map<String, AttributeValue> newImage = record.getDynamodb().getNewImage();
            Map<String, AttributeValue> oldImage = record.getDynamodb().getOldImage();
            if (newImage != null && oldImage != null) {
                String newLender = newImage.get("lender").getS();
                String oldLender = oldImage.get("lender").getS();
                if (!newLender.equals("null") && oldLender.equals("null")) {
                    String category = newImage.get("category").getS();
                    Statistic statistic = statisticDao.count(Category.valueOf(category));
                    try {
                        return new GatewayResponse(objectMapper.writeValueAsString(statistic), HEADER, SC_OK);
                    } catch (JsonProcessingException e) {
                        return new GatewayResponse(e.getMessage(), HEADER, SC_INTERNAL_SERVER_ERROR);
                    }
                }
            }
        }
        return new GatewayResponse(null, HEADER, SC_INTERNAL_SERVER_ERROR);
    }
}
