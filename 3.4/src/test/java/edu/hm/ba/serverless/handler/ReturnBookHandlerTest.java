package edu.hm.ba.serverless.handler;

import edu.hm.ba.serverless.dao.BookDao;
import edu.hm.ba.serverless.model.Book;
import edu.hm.ba.serverless.model.Category;
import edu.hm.ba.serverless.model.request.CreateBookRequest;
import edu.hm.ba.serverless.model.response.GatewayResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ReturnBookHandlerTest {

    private static final Book BOOK = new Book("9783868009217", "Verwesung", "Simon Beckett", Category.FANTASY, "null");

    private BookDao bookDao;

    @Before
    public void setUp() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();
        bookDao = new BookDao(client, "books");
        bookDao.createBook(new CreateBookRequest(BOOK.getIsbn(), BOOK.getTitle(), BOOK.getAuthor(), BOOK.getCategory(), BOOK.getLender()));
        bookDao.lendBook(BOOK.getIsbn());
    }

    @After
    public void cleanUp() {
        bookDao.deleteBook(BOOK.getIsbn());
    }

    @Test
    public void successfulResponse() {
        ReturnBookHandler returnBookHandler = new ReturnBookHandler();
        Map<String, Object> input = new HashMap<>();
        input.put("pathParameters", "{isbn=9783868009217}");
        GatewayResponse response = returnBookHandler.handleRequest(input, null);
        assertEquals(201, response.getStatusCode());
        assertEquals("\"TestUser\"", response.getBody());
    }

}
