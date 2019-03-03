package edu.hm.ba.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.ba.serverless.config.AppComponent;
import edu.hm.ba.serverless.config.DaggerAppComponent;
import edu.hm.ba.serverless.dao.BookDao;
import edu.hm.ba.serverless.exception.CouldNotLendBookException;
import edu.hm.ba.serverless.model.response.GatewayResponse;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;

/**
 * Handler for request to LendBook Lambda function.
 */
public class LendBookHandler implements RequestHandler<Map<String, Object>, GatewayResponse>, ConstantRequestHandler {

    /**
     * Object mapper for serialization.
     */
    @Inject
    ObjectMapper objectMapper;

    /**
     * Data Access Object.
     */
    @Inject
    BookDao bookDao;

    /**
     * Dagger component for dependency injection.
     */
    private final AppComponent appComponent;

    /**
     * Constructor to inject object mapper and dao.
     */
    public LendBookHandler() {
        appComponent = DaggerAppComponent.builder().build();
        appComponent.inject(this);
    }

    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        String pathParameter = input.get("pathParameters").toString();
        String isbn = pathParameter.substring(6, pathParameter.length()-1);
        String lender = bookDao.lendBook(isbn);
        try {
            return new GatewayResponse(objectMapper.writeValueAsString(lender), HEADER, SC_CREATED);
        } catch (CouldNotLendBookException | IOException e) {
            return new GatewayResponse(e.getMessage(), HEADER, SC_INTERNAL_SERVER_ERROR);
        }
    }
}
