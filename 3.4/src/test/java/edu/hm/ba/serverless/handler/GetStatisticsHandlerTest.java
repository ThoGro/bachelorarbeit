package edu.hm.ba.serverless.handler;

import edu.hm.ba.serverless.model.response.GatewayResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetStatisticsHandlerTest {

    @Test
    public void successfulResponse() {
        GetStatisticsHandler getStatisticsHandler = new GetStatisticsHandler();
        GatewayResponse response = getStatisticsHandler.handleRequest(null, null);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}
