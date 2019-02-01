package edu.hm.ba.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.ba.serverless.config.BookComponent;
import edu.hm.ba.serverless.config.DaggerBookComponent;
import edu.hm.ba.serverless.dao.BookDao;
import edu.hm.ba.serverless.model.Book;
import edu.hm.ba.serverless.model.response.GatewayResponse;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;

public class UpdateBookHandler implements RequestHandler<Map<String, Object>, GatewayResponse>, BookRequestHandler {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    BookDao bookDao;

    private final BookComponent bookComponent;

    public UpdateBookHandler() {
        bookComponent = DaggerBookComponent.builder().build();
        bookComponent.inject(this);
    }

    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        String pathParameter = input.get("pathParameters").toString();
        String isbn = pathParameter.substring(6, pathParameter.length()-1);
        String body = input.get("body").toString();
        try {
            Map<String, Object> bodyMap = objectMapper.readValue(body, new TypeReference<Map<String, String>>(){});
            Book toUpdate = new Book(
                    bodyMap.get("isbn").toString(), bodyMap.get("title").toString(), bodyMap.get("author").toString());
            Book updated = bookDao.updateBook(isbn, toUpdate);
            return new GatewayResponse(objectMapper.writeValueAsString(updated), HEADER, SC_CREATED);
        } catch (IOException e) {
            return new GatewayResponse(e.getMessage(), HEADER, SC_INTERNAL_SERVER_ERROR);
        }
    }
}
