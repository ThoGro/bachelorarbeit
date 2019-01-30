package edu.hm.ba.serverless.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@JsonAutoDetect
public class GatewayResponse {

    private final String body;
    private final Map<String, String> headers;
    private final int statusCode;

    public GatewayResponse(final String body, final Map<String, String> headers, final int statusCode) {
        this.body = body;
        this.headers = Collections.unmodifiableMap(new HashMap<>(headers));
        this.statusCode = statusCode;
    }

}
