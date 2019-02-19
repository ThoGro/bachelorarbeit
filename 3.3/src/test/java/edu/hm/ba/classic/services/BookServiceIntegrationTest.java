package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Book;
import edu.hm.ba.classic.entities.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        assertEquals(book.getIsbn(), "9783942656863");
    }

    @Test
    public void testDeleteBook() {
        Book book = bookService.deleteBook("9783442151479");
        assertThat(book).isNotNull();
        assertEquals(book.getIsbn(), "9783442151479");
    }

    @Test
    public void testUpdateBook() {
        Book book = bookService.updateBook("9783806234770", new Book("9783806234770", "Neuer Titel", "Autor", Category.valueOf("SCIENCE")));
        assertThat(book).isNotNull();
        assertEquals(book.getIsbn(), "9783806234770");
        assertEquals(book.getTitle(), "Neuer Titel");
    }

    @Test
    public void testLendBook() {

    }

    @Test
    public void testReturnBook() {

    }

    @Test
    public void testGetLendings() {

    }

}
