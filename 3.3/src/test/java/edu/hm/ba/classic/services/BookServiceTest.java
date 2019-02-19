package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Book;
import edu.hm.ba.classic.entities.Category;
import edu.hm.ba.classic.entities.Role;
import edu.hm.ba.classic.entities.User;
import edu.hm.ba.classic.exception.BookDoesNotExistException;
import edu.hm.ba.classic.exception.CouldNotCreateBookException;
import edu.hm.ba.classic.exception.CouldNotLendBookException;
import edu.hm.ba.classic.exception.DuplicateBookException;
import edu.hm.ba.classic.persistence.BookRepository;
import edu.hm.ba.classic.persistence.UserRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BookServiceTest {

    private static final Book BOOK = new Book("9783764504458", "Blackout", "Marc Elsberg", Category.FANTASY);

    private static final Book BOOK2 = new Book("9783806234770", "Schwarze Flaggen", "Joby Warrick", Category.HISTORY);

    private static final List<Book> BOOKS = new ArrayList<>(Arrays.asList(
            new Book("9783442151479", "Bildung - Alles, was man wissen muss", "Dietrich Schwanitz", Category.SCIENCE),
            BOOK2,
            BOOK
    ));

    private static final Book NEW_BOOK = new Book("9783942656863", "Kalte Asche", "Simon Beckett", Category.FANTASY);

    private static final User LENDER = new User(1, "Admin", "admin1", Role.ADMIN);

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

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
        when(bookRepository.save(NEW_BOOK)).thenReturn(NEW_BOOK);
        when(bookRepository.findAll()).thenReturn(BOOKS);
        Book book = serviceUnderTest.addBook(NEW_BOOK);
        assertEquals(NEW_BOOK, book);
    }

    @Test
    public void testAddBookCouldNotCreateBook() {
        exception.expect(CouldNotCreateBookException.class);
        exception.expectMessage("Unable to add book with isbn 1234567890123 title Blackout and author Marc Elsberg");
        Book toCreate = BOOK;
        toCreate.setIsbn("1234567890123");
        when(bookRepository.save(toCreate)).thenReturn(toCreate);
        when(bookRepository.findAll()).thenReturn(BOOKS);
        Book book = serviceUnderTest.addBook(toCreate);
    }

    @Test
    public void testAddBookDuplicateBook() {
        exception.expect(DuplicateBookException.class);
        exception.expectMessage("The book with isbn 9783806234770 does already exist.");
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
        exception.expectMessage("The book with isbn 9783806234770 can not be found.");
        when(bookRepository.findById(BOOK2.getIsbn())).thenReturn(null);
        serviceUnderTest.deleteBook(BOOK2.getIsbn());
    }

    @Test
    public void testUpdateBook() {
        when(bookRepository.getOne(BOOK2.getIsbn())).thenReturn(BOOK2);
        Book book = new Book("9783806234770", "Neuer Titel", "TestAutor", Category.SCIENCE);
        when(bookRepository.save(book)).thenReturn(book);
        Book updated = serviceUnderTest.updateBook("9783806234770", book);
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
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(LENDER.getUsername());
        when(bookRepository.existsById(BOOK.getIsbn())).thenReturn(true);
        when(bookRepository.getOne(BOOK.getIsbn())).thenReturn(BOOK);
        when(userRepository.findUserByUsername(LENDER.getUsername())).thenReturn(LENDER);
        User lender = serviceUnderTest.lendBook("9783764504458", authentication);
        assertEquals(LENDER, lender);
    }

    @Test
    public void testLendBookCouldNotLendBookException() {
        exception.expect(CouldNotLendBookException.class);
        Authentication authentication = mock(Authentication.class);
        when(bookRepository.existsById("1234567890123")).thenReturn(false);
        User lender = serviceUnderTest.lendBook("1234567890123", authentication);
    }

    @Test
    public void testReturnBook() {
        Book lent = BOOK;
        lent.setLender(LENDER);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(LENDER.getUsername());
        when(bookRepository.existsById(lent.getIsbn())).thenReturn(true);
        when(bookRepository.getOne(lent.getIsbn())).thenReturn(lent);
        when(userRepository.findUserByUsername(LENDER.getUsername())).thenReturn(LENDER);
        User returner = serviceUnderTest.returnBook("9783764504458", authentication);
        assertEquals(LENDER, returner);
    }

    @Test
    public void testReturnBookCouldNotLendBookException() {
        exception.expect(CouldNotLendBookException.class);
        Authentication authentication = mock(Authentication.class);
        when(bookRepository.existsById("1234567890123")).thenReturn(false);
        User lender = serviceUnderTest.returnBook("1234567890123", authentication);
    }

    @Test
    public void testGetLendings() {
        Authentication authentication = mock(Authentication.class);
        LENDER.setLendings(BOOKS);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(LENDER.getUsername());
        when(userRepository.findUserByUsername(LENDER.getUsername())).thenReturn(LENDER);
        Collection<Book> lendings = serviceUnderTest.getLendings(authentication);
        assertEquals(BOOKS, lendings);
    }

}
