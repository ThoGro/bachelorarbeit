package edu.hm.ba.serverless.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the Response from a function.
 */
@Getter
@JsonAutoDetect
public class GatewayResponse {

    /**
     * Body of the response.
     */
    private final String body;

    /**
     * Headers of the response.
     */
    private final Map<String, String> headers;

    /**
     * Status code of the response.
     */
    private final int statusCode;

    /**
     * Constructs a Response with body, headers and statusCode.
     * @param body the body of the response
     * @param headers the headers of the response
     * @param statusCode the status code of the response
     */
    public GatewayResponse(final String body, final Map<String, String> headers, final int statusCode) {
        this.body = body;
        this.headers = Collections.unmodifiableMap(new HashMap<>(headers));
        this.statusCode = statusCode;
    }

}
