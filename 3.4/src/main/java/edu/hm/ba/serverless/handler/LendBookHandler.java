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

public class LendBookHandler implements RequestHandler<Map<String, Object>, GatewayResponse>, ConstantRequestHandler {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    BookDao bookDao;

    private final AppComponent appComponent;

    public LendBookHandler() {
        appComponent = DaggerAppComponent.builder().build();
        appComponent.inject(this);
    }

    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        String pathParameter = input.get("pathParameters").toString();
        String isbn = pathParameter.substring(6, pathParameter.length()-1);
        System.out.println("1: " + context.toString());
        System.out.println("2: " + context.getIdentity());
        System.out.println("2: " + context.getIdentity().toString());
        System.out.println("3: " + context.getIdentity().getIdentityId());
        System.out.println("4: " + context.getIdentity().getIdentityPoolId());
        String lender = bookDao.lendBook(isbn);
        try {
            return new GatewayResponse(objectMapper.writeValueAsString(lender), HEADER, SC_CREATED);
        } catch (CouldNotLendBookException | IOException e) {
            return new GatewayResponse(e.getMessage(), HEADER, SC_INTERNAL_SERVER_ERROR);
        }
    }
}
