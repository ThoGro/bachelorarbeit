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
import java.util.List;
import java.util.Map;

public class GetBooksHandler implements RequestHandler<Map<String, Object>, GatewayResponse>, BookRequestHandler {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    BookDao bookDao;

    private final BookComponent bookComponent;

    public GetBooksHandler() {
        bookComponent = DaggerBookComponent.builder().build();
        bookComponent.inject(this);
    }

    @Override
    public GatewayResponse handleRequest(Map<String, Object> stringObjectMap, Context context) {
        List<Book> books = bookDao.getBooks();
        try {
            return new GatewayResponse(objectMapper.writeValueAsString(books), HEADER, SC_OK);
        } catch (JsonProcessingException e) {
            return new GatewayResponse(e.getMessage(), HEADER, SC_INTERNAL_SERVER_ERROR);
        }
    }
}