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

@SpringBootTest
@RunWith(SpringRunner.class)
public class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Test
    public void testGetBooks() {
        Collection<Book> books = bookService.getBooks();
        assertThat(books).isNotNull().isNotEmpty();
    }

    @Test
    public void testAddBook() {
        Book book = bookService.addBook(new Book("9783257069709", "Titel des Buches", "Autor", Category.valueOf("FANTASY")));
        assertThat(book).isNotNull();
    }

    @Test
    public void testDeleteBook() {
        Book book = bookService.deleteBook("9783866809876");
        assertThat(book).isNotNull();
    }

    @Test
    public void testUpdateBook() {
        Book book = bookService.updateBook("9783866801234", new Book("9783866801234", "Neuer Titel", "Autor", Category.valueOf("SCIENCE")));
        assertThat(book).isNotNull();
    }

    @Test
    public void testLendBook() {

    }

    @Test
    public void testReturnBook() {

    }

    @Test
    public void testGetLoans() {

    }

}
