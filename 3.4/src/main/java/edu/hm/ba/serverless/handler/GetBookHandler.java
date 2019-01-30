package edu.hm.ba.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.ba.serverless.config.BookComponent;
import edu.hm.ba.serverless.config.DaggerBookComponent;
import edu.hm.ba.serverless.dao.BookDao;
import edu.hm.ba.serverless.model.Book;
import edu.hm.ba.serverless.model.response.GatewayResponse;

import javax.inject.Inject;
import java.util.Map;

public class GetBookHandler implements RequestHandler<Map<String, String>, GatewayResponse>, BookRequestHandler {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    BookDao bookDao;

    private final BookComponent bookComponent;

    public GetBookHandler() {
        bookComponent = DaggerBookComponent.builder().build();
        bookComponent.inject(this);
    }

    @Override
    public GatewayResponse handleRequest(Map<String, String> input, Context context) {
        String isbn = input.get("pathParam");
        Book book = bookDao.getBook(isbn);
        try {
            String json = objectMapper.writeValueAsString(book);
            return new GatewayResponse(json, HEADER, SC_OK);
        } catch (JsonProcessingException e) {
            return new GatewayResponse(e.getMessage(), HEADER, SC_INTERNAL_SERVER_ERROR);
        }
    }
}
