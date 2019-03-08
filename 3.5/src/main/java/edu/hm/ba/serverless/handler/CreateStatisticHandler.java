package edu.hm.ba.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.ba.serverless.config.AppComponent;
import edu.hm.ba.serverless.dao.StatisticDao;
import edu.hm.ba.serverless.exception.CouldNotCreateBookException;
import edu.hm.ba.serverless.model.Category;
import edu.hm.ba.serverless.model.Statistic;
import edu.hm.ba.serverless.model.response.GatewayResponse;
import edu.hm.ba.serverless.config.DaggerAppComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;

/**
 * Handler for request to CreateStatistic Lambda function.
 */
public class CreateStatisticHandler implements RequestHandler<Map<String, Object>, GatewayResponse>, ConstantRequestHandler {

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
    public CreateStatisticHandler() {
        appComponent = DaggerAppComponent.builder().build();
        appComponent.inject(this);
    }

    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            String body = input.get("body").toString();
            Map<String, Object> bodyMap = objectMapper.readValue(body, new TypeReference<Map<String, String>>() {});
            Statistic toCreate = new Statistic(Integer.valueOf(bodyMap.get("statisticCount").toString()),
                    Category.valueOf(bodyMap.get("category").toString()));
            final Statistic statistic = statisticDao.createStatistic(toCreate);
            return new GatewayResponse(objectMapper.writeValueAsString(statistic), HEADER, SC_CREATED);
        } catch (CouldNotCreateBookException | IOException e) {
            return new GatewayResponse(e.getMessage(), HEADER, SC_INTERNAL_SERVER_ERROR);
        }
    }
}
