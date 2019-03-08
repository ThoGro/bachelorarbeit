package edu.hm.ba.serverless.handler;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import edu.hm.ba.serverless.dao.BookDao;
import edu.hm.ba.serverless.model.Book;
import edu.hm.ba.serverless.model.Category;
import edu.hm.ba.serverless.model.response.GatewayResponse;
import org.junit.After;
import org.junit.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CreateBookHandlerTest {

    private static final Book BOOK = new Book("9783868009217", "Verwesung", "Simon Beckett", Category.FANTASY, "null");

    @After
    public void cleanUp() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();
        BookDao bookDao = new BookDao(client, "books");
        bookDao.deleteBook(BOOK.getIsbn());
    }

    @Test
    public void successfulResponse() throws JSONException {
        CreateBookHandler createBookHandler = new CreateBookHandler();
        Map<String, Object> input = new HashMap<>();
        JSONObject json  = new JSONObject();
        json.put("isbn", BOOK.getIsbn());
        json.put("title", BOOK.getTitle());
        json.put("author", BOOK.getAuthor());
        json.put("category", BOOK.getCategory().toString());
        input.put("body", json);
        GatewayResponse response = createBookHandler.handleRequest(input, null);
        assertEquals(201, response.getStatusCode());
        assertEquals("{\"isbn\":\"9783868009217\",\"title\":\"Verwesung\",\"author\":\"Simon Beckett\",\"category\":\"FANTASY\",\"lender\":\"null\"}",
                response.getBody());

    }

}
