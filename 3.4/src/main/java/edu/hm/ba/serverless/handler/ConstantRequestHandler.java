package edu.hm.ba.serverless.handler;

import java.util.HashMap;
import java.util.Map;

public interface ConstantRequestHandler {

    int SC_OK = 200;
    int SC_CREATED = 201;
    int SC_BAD_REQUEST = 400;
    int SC_NOT_FOUND = 404;
    int SC_CONFLICT = 409;
    int SC_INTERNAL_SERVER_ERROR = 500;
    Map<String, String> HEADER = new HashMap<String, String>() {{
        put("Content-Type", "application/json");
        put("X-Custom-Header", "application/json");
        put("Access-Control-Allow-Origin", "*");
        put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,x-requested-with");
        put("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,OPTIONS");
    }};

}
