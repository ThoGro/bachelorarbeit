package edu.hm.ba.serverless.handler;

import edu.hm.ba.serverless.dao.BookDao;
import edu.hm.ba.serverless.model.Book;
import edu.hm.ba.serverless.model.Category;
import edu.hm.ba.serverless.model.request.CreateBookRequest;
import edu.hm.ba.serverless.model.response.GatewayResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static org.junit.Assert.assertEquals;

public class GetLendingsHandlerTest {

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
        bookDao.returnBook(BOOK.getIsbn());
        bookDao.deleteBook(BOOK.getIsbn());
    }

    @Test
    public void successfulResponse() {
        GetLendingsHandler getLendingsHandler = new GetLendingsHandler();
        GatewayResponse response = getLendingsHandler.handleRequest(null, null);
        assertEquals(200, response.getStatusCode());
        assertEquals("[{\"isbn\":\"9783868009217\",\"title\":\"Verwesung\",\"author\":\"Simon Beckett\",\"category\":\"FANTASY\",\"lender\":\"TestUser\"}]",
                response.getBody());
    }

}
