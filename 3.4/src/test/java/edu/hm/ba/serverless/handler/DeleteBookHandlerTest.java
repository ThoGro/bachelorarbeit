package edu.hm.ba.serverless.handler;

import edu.hm.ba.serverless.dao.BookDao;
import edu.hm.ba.serverless.model.Book;
import edu.hm.ba.serverless.model.request.CreateBookRequest;
import edu.hm.ba.serverless.model.response.GatewayResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DeleteBookHandlerTest {

    private Book book;
    private BookDao bookDao;

    @Before
    public void setUp() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();
        bookDao = new BookDao(client, "books");
        book = bookDao.getBook("9783442151479");
    }

    @After
    public void cleanUp() {
        bookDao.createBook(new CreateBookRequest(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getCategory(), book.getLender()));
    }

    @Test
    public void successfulResponse() {
        DeleteBookHandler deleteBookHandler = new DeleteBookHandler();
        Map<String, Object> input = new HashMap<>();
        input.put("pathParameters", "{isbn=9783442151479}");
        GatewayResponse response = deleteBookHandler.handleRequest(input, null);
        assertEquals(200, response.getStatusCode());
        assertEquals("{\"isbn\":\"9783442151479\",\"title\":\"Bildung - Alles, was man wissen muss\",\"author\":\"Dietrich Schwanitz\",\"category\":\"SCIENCE\",\"lender\":\"null\"}",
                response.getBody());

    }

}
