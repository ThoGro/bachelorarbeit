package edu.hm.ba.serverless.handler;

import edu.hm.ba.serverless.model.response.GatewayResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GetStatisticHandlerTest {

    @Test
    public void successfulResponse() {
        GetStatisticHandler getStatisticHandler = new GetStatisticHandler();
        Map<String, Object> input = new HashMap<>();
        input.put("pathParameters", "{category=SCIENCE}");
        GatewayResponse response = getStatisticHandler.handleRequest(input, null);
        assertEquals(200, response.getStatusCode());
        assertEquals("{\"statisticCount\":11,\"category\":\"SCIENCE\"}",
                response.getBody());
    }

}
