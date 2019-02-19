package edu.hm.ba.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.ba.serverless.config.AppComponent;
import edu.hm.ba.serverless.config.DaggerAppComponent;
import edu.hm.ba.serverless.dao.BookDao;
import edu.hm.ba.serverless.model.Book;
import edu.hm.ba.serverless.model.response.GatewayResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class GetLendingsHandler implements RequestHandler<Map<String, Object>, GatewayResponse>, ConstantRequestHandler {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    BookDao bookDao;

    private final AppComponent appComponent;

    public GetLendingsHandler() {
        appComponent = DaggerAppComponent.builder().build();
        appComponent.inject(this);
    }

    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        List<Book> lendings = bookDao.getLendings();
        try {
            return new GatewayResponse(objectMapper.writeValueAsString(lendings), HEADER, SC_OK);
        } catch (JsonProcessingException e) {
            return new GatewayResponse(e.getMessage(), HEADER, SC_INTERNAL_SERVER_ERROR);
        }
    }
}
