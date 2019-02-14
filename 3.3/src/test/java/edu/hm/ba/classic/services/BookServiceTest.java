package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Book;
import edu.hm.ba.classic.entities.Category;
import edu.hm.ba.classic.exception.BookDoesNotExistException;
import edu.hm.ba.classic.exception.CouldNotCreateBookException;
import edu.hm.ba.classic.exception.DuplicateBookException;
import edu.hm.ba.classic.persistence.BookRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BookServiceTest {

    private static final List<Book> BOOKS = new ArrayList<>(Arrays.asList(
            new Book("1234567890000", "TestBuch1", "TestAutor", Category.SCIENCE),
            new Book("1234567890001", "TestBuch2", "TestAutor", Category.FANTASY),
            new Book("1234567890002", "TestBuch3", "Autor", Category.HISTORY)
    ));

    private static final Book BOOK = new Book("9783868028072", "TestBuch1", "TestAutor", Category.SCIENCE);

    private static final Book BOOK2 = new Book("9783831032709", "TestBuch2", "TestAutor", Category.HISTORY);

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookService serviceUnderTest;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetBooks() {
        when(bookRepository.findAll()).thenReturn(BOOKS);
        Collection<Book> books = serviceUnderTest.getBooks();
        assertEquals(BOOKS, books);
    }

    @Test
    public void testAddBook() {
        when(bookRepository.save(BOOK)).thenReturn(BOOK);
        when(bookRepository.findAll()).thenReturn(BOOKS);
        Book book = serviceUnderTest.addBook(BOOK);
        assertEquals(BOOK, book);
    }

    @Test
    public void testAddBookCouldNotCreateBook() {
        exception.expect(CouldNotCreateBookException.class);
        exception.expectMessage("Unable to add book with isbn 1234567890123 title TestBuch1 and author TestAutor");
        Book toCreate = BOOK;
        toCreate.setIsbn("1234567890123");
        when(bookRepository.save(toCreate)).thenReturn(toCreate);
        when(bookRepository.findAll()).thenReturn(BOOKS);
        Book book = serviceUnderTest.addBook(toCreate);
    }

    @Test
    public void testAddBookDuplicateBook() {
        exception.expect(DuplicateBookException.class);
        exception.expectMessage("The book with isbn 9783831032709 does already exist.");
        when(bookRepository.save(BOOK2)).thenReturn(BOOK2);
        BOOKS.add(BOOK2);
        when(bookRepository.findAll()).thenReturn(BOOKS);
        serviceUnderTest.addBook(BOOK2);
        serviceUnderTest.addBook(BOOK2);
    }

    @Test
    public void testDeleteBook() {
        when(bookRepository.findById(BOOK.getIsbn())).thenReturn(Optional.of(BOOK));
        Book deleted = serviceUnderTest.deleteBook(BOOK.getIsbn());
        verify(bookRepository, times(1)).deleteById(eq(BOOK.getIsbn()));
        assertEquals(BOOK, deleted);
    }

    @Test
    public void testDeleteBookBookDoesNotExist() {
        exception.expect(BookDoesNotExistException.class);
        exception.expectMessage("The book with isbn 9783831032709 can not be found.");
        when(bookRepository.findById(BOOK2.getIsbn())).thenReturn(null);
        serviceUnderTest.deleteBook(BOOK2.getIsbn());
    }

    @Test
    public void testUpdateBook() {
        when(bookRepository.getOne(BOOK2.getIsbn())).thenReturn(BOOK2);
        Book book = new Book("9783831032709", "Neuer Titel", "TestAutor", Category.SCIENCE);
        when(bookRepository.save(book)).thenReturn(book);
        Book updated = serviceUnderTest.updateBook("9783831032709", book);
        assertEquals(book, updated);
    }

    @Test
    public void testUpdateBookCouldNotCreateBook() {
        exception.expect(CouldNotCreateBookException.class);
        exception.expectMessage("Unable to update book with isbn 1234567890123 title Neuer Titel and author ");
        when(bookRepository.getOne(BOOK.getIsbn())).thenReturn(BOOK);
        Book book = new Book("1234567890123", "Neuer Titel", "", Category.SCIENCE);
        when(bookRepository.save(book)).thenReturn(book);
        serviceUnderTest.updateBook("9783868028072", book);
    }

    @Test
    public void testLendBook() {

    }

    @Test
    public void testReturnBok() {

    }

    @Test
    public void testGetLendings() {

    }

}
