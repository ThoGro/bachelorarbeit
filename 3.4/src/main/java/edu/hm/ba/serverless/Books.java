package edu.hm.ba.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Books implements RequestHandler<Object, Object> {

    private static final ArrayList<String> books = new ArrayList<>(Arrays.asList("Buch1", "Buch2", "TestBuch", "Test10"));

    @Override
    public Object handleRequest(Object o, Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        String output = String.format("{ \"books\": \"%s\" }", books);
        return new GatewayResponse(output, headers, 200);
    }
}
