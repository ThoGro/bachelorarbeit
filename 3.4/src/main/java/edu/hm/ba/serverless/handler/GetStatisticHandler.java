package edu.hm.ba.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
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

public class GetStatisticHandler implements RequestHandler<Map<String, Object>, GatewayResponse>, ConstantRequestHandler {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    StatisticDao statisticDao;

    private final AppComponent appComponent;

    public GetStatisticHandler() {
        appComponent = DaggerAppComponent.builder().build();
        appComponent.inject(this);
    }

    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        String pathParameter = input.get("pathParameters").toString();
        String category = pathParameter.substring(10, pathParameter.length()-1);
        Statistic statistic = statisticDao.getStatistic(Category.valueOf(category));
        try {
            return new GatewayResponse(objectMapper.writeValueAsString(statistic), HEADER, SC_OK);
        } catch (JsonProcessingException e) {
            return new GatewayResponse(e.getMessage(), HEADER, SC_INTERNAL_SERVER_ERROR);
        }
    }
}