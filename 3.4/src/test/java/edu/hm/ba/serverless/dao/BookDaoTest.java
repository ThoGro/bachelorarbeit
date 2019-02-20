package edu.hm.ba.serverless.dao;

import edu.hm.ba.serverless.exception.CouldNotCreateBookException;
import edu.hm.ba.serverless.exception.CouldNotUpdateBookException;
import edu.hm.ba.serverless.model.Book;
import edu.hm.ba.serverless.model.Category;
import edu.hm.ba.serverless.model.request.CreateBookRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BookDaoTest {

    private static final Book BOOK = new Book("9783764504458", "Blackout", "Marc Elsberg", Category.FANTASY, "null");

    private static final String TABLE_NAME = "books";

    private BookDao bookDao;

    @Mock
    private DynamoDbClient dynamoDb;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        bookDao = new BookDao(dynamoDb, TABLE_NAME);
    }

    @Test
    public void testGetBook() {
        GetItemResponse getItemResponse = GetItemResponse.builder().item(createResultMap()).build();
        when(dynamoDb.getItem(any(GetItemRequest.class))).thenReturn(getItemResponse);
        Book book = bookDao.getBook(BOOK.getIsbn());
        assertEquals(BOOK, book);
    }

    @Test
    public void testGetBooks() {
        ScanResponse result = ScanResponse.builder().items(createResultMap()).build();
        when(dynamoDb.scan(any(ScanRequest.class))).thenReturn(result);
        List<Book> books = bookDao.getBooks();
        assertEquals(Arrays.asList(BOOK), books);
    }

    @Test
    public void testCreateBook() {
        when(dynamoDb.putItem(any(PutItemRequest.class))).thenReturn(any(PutItemResponse.class));
        Book book = bookDao.createBook(new CreateBookRequest(BOOK.getIsbn(), BOOK.getTitle(), BOOK.getAuthor(), BOOK.getCategory(), BOOK.getLender()));
        assertEquals(BOOK, book);
    }

    @Test
    public void testCreateBookCouldNotCreateBookException() {
        exception.expect(CouldNotCreateBookException.class);
        bookDao.createBook(new CreateBookRequest("1234567890123", BOOK.getTitle(), BOOK.getAuthor(), BOOK.getCategory(), BOOK.getLender()));
    }

    @Test
    public void testDeleteBook() {
        DeleteItemResponse deleteItemResponse = DeleteItemResponse.builder().attributes(createResultMap()).build();
        when(dynamoDb.deleteItem(any(DeleteItemRequest.class))).thenReturn(deleteItemResponse);
        Book book = bookDao.deleteBook(BOOK.getIsbn());
        assertEquals(BOOK, book);
    }

    @Test
    public void testUpdateBook() {
        Map<String, AttributeValue> resultMap = createResultMap();
        resultMap.replace("category", AttributeValue.builder().s(Category.SCIENCE.toString()).build());
        Book updatedBook = new Book("9783764504458", "Blackout", "Marc Elsberg", Category.SCIENCE, "null");
        UpdateItemResponse updateItemResponse = UpdateItemResponse.builder().attributes(resultMap).build();
        when(dynamoDb.updateItem(any(UpdateItemRequest.class))).thenReturn(updateItemResponse);
        Book book = bookDao.updateBook(BOOK.getIsbn(), updatedBook);
        assertEquals(updatedBook, book);
    }

    @Test
    public void testUpdateBookCouldNotUpdateBookException() {
        exception.expect(CouldNotUpdateBookException.class);
        Book updatedBook = new Book("9783764504458", "Blackout", "", Category.SCIENCE, "null");
        bookDao.updateBook(BOOK.getIsbn(), updatedBook);
    }

    @Test
    public void testLendBook()  {

    }

    @Test
    public void testReturnBook() {

    }

    @Test
    public void testGetLendings() {

    }

    private Map<String, AttributeValue> createResultMap() {
        Map<String, AttributeValue> resultMap = new HashMap<>();
        resultMap.put("isbn", AttributeValue.builder().s(BOOK.getIsbn()).build());
        resultMap.put("title", AttributeValue.builder().s(BOOK.getTitle()).build());
        resultMap.put("author", AttributeValue.builder().s(BOOK.getAuthor()).build());
        resultMap.put("category", AttributeValue.builder().s(BOOK.getCategory().toString()).build());
        resultMap.put("lender", AttributeValue.builder().s(BOOK.getLender()).build());
        return resultMap;
    }

}
