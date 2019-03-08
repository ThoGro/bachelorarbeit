package edu.hm.ba.serverless.dao;

import edu.hm.ba.serverless.model.Book;
import edu.hm.ba.serverless.model.Category;
import edu.hm.ba.serverless.model.request.CreateBookRequest;
import org.junit.Before;
import org.junit.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class BookDaoIntegrationTest {

    private static final Book BOOK = new Book("9783868009217", "Verwesung", "Simon Beckett", Category.FANTASY, "null");

    private static final String TABLE_NAME = "books";

    private BookDao bookDao;

    @Before
    public void setUp() {
        DynamoDbClient dynamoDb = DynamoDbClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();
        bookDao = new BookDao(dynamoDb, TABLE_NAME);
    }

    @Test
    public void testGetBook() {
        Book book = bookDao.getBook("9783442151479");
        assertThat(book).isNotNull();
        assertEquals("9783442151479", book.getIsbn());
    }

    @Test
    public void testGetBooks() {
        List<Book> books = bookDao.getBooks();
        assertThat(books).isNotNull();
    }

    @Test
    public void testCreateBook() {
        Book book = bookDao.createBook(new CreateBookRequest(BOOK.getIsbn(), BOOK.getTitle(), BOOK.getAuthor(), BOOK.getCategory(), BOOK.getLender()));
        assertThat(book).isNotNull();
        assertEquals(BOOK, book);
    }

    @Test
    public void testDeleteBook() {
        Book book = bookDao.deleteBook(BOOK.getIsbn());
        assertThat(book).isNotNull();
        assertEquals(BOOK, book);
    }

    @Test
    public void testUpdateBook() {
        Book updatedBook = new Book("9783764504458", "Blackout", "Marc Elsberg", Category.SCIENCE, "null");
        Book book = bookDao.updateBook(BOOK.getIsbn(), updatedBook);
        assertThat(book).isNotNull();
        assertEquals(updatedBook, book);
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

}
