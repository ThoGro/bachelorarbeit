package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Book;
import edu.hm.ba.classic.entities.Category;
import edu.hm.ba.classic.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Test
    public void testGetBooks() {
        Collection<Book> books = bookService.getBooks();
        assertThat(books).isNotNull().isNotEmpty();
        assertEquals(3, books.size());
    }

    @Test
    public void testAddBook() {
        Book book = bookService.addBook(new Book("9783942656863", "Kalte Asche", "Simon Beckett", Category.FANTASY));
        assertThat(book).isNotNull();
        assertEquals("9783942656863", book.getIsbn());
    }

    @Test
    public void testDeleteBook() {
        Book book = bookService.deleteBook("9783442151479");
        assertThat(book).isNotNull();
        assertEquals("9783442151479", book.getIsbn());
    }

    @Test
    public void testUpdateBook() {
        Book book = bookService.updateBook("9783806234770", new Book("9783806234770", "Neuer Titel", "Autor", Category.valueOf("SCIENCE")));
        assertThat(book).isNotNull();
        assertEquals("9783806234770", book.getIsbn());
        assertEquals("Neuer Titel", book.getTitle());
    }

    @Test
    public void testLendBook() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("Mitarbeiter", "{noop}mitarbeiter",
                AuthorityUtils.createAuthorityList("EMPLOYEE"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "mitarbeiter", userDetails.getAuthorities());
        User user = bookService.lendBook("9783806234770", authentication);
        assertThat(user).isNotNull();
        assertEquals("Mitarbeiter", user.getUsername());
        assertEquals("mitarbeiter", user.getPassword());
    }

    @Test
    public void testReturnBook() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("Mitarbeiter", "{noop}mitarbeiter",
                AuthorityUtils.createAuthorityList("EMPLOYEE"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "mitarbeiter", userDetails.getAuthorities());
        bookService.lendBook("9783806234770", authentication);
        User user = bookService.returnBook("9783806234770", authentication);
        assertThat(user).isNotNull();
        assertEquals("Mitarbeiter", user.getUsername());
        assertEquals("mitarbeiter", user.getPassword());
    }

    @Test
    public void testGetLendings() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("Mitarbeiter", "{noop}mitarbeiter",
                AuthorityUtils.createAuthorityList("EMPLOYEE"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "mitarbeiter", userDetails.getAuthorities());
        bookService.lendBook("9783806234770", authentication);
        Collection<Book> lendings = bookService.getLendings(authentication);
        assertThat(lendings).isNotNull();
        assertEquals(1, lendings.size());
    }

}
