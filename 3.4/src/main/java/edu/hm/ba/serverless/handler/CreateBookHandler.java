package edu.hm.ba.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.ba.serverless.config.DaggerBookComponent;
import edu.hm.ba.serverless.config.BookComponent;
import edu.hm.ba.serverless.dao.BookDao;
import edu.hm.ba.serverless.exception.CouldNotCreateBookException;
import edu.hm.ba.serverless.model.Book;
import edu.hm.ba.serverless.model.request.CreateBookRequest;
import edu.hm.ba.serverless.model.response.GatewayResponse;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;

public class CreateBookHandler implements RequestHandler<Map<String, Object>, GatewayResponse>, ConstantRequestHandler {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    BookDao bookDao;

    private final BookComponent bookComponent;

    public CreateBookHandler() {
        bookComponent = DaggerBookComponent.builder().build();
        bookComponent.inject(this);
    }

    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            String body = input.get("body").toString();
            Map<String, Object> bodyMap = objectMapper.readValue(body, new TypeReference<Map<String, String>>(){});
            CreateBookRequest  createBookRequest = new CreateBookRequest(
                    bodyMap.get("isbn").toString(), bodyMap.get("title").toString(), bodyMap.get("author").toString());
            final Book book = bookDao.createBook(createBookRequest);
            return new GatewayResponse(objectMapper.writeValueAsString(book), HEADER, SC_CREATED);
        } catch (CouldNotCreateBookException e) {
            return new GatewayResponse(e.getMessage(), HEADER, SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            return new GatewayResponse(e.getMessage(), HEADER, SC_INTERNAL_SERVER_ERROR);
        }
    }
}
