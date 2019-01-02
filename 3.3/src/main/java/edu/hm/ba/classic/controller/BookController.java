package edu.hm.ba.classic.controller;

import edu.hm.ba.classic.entities.Book;
import edu.hm.ba.classic.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * The class provides endpoints for the communication with the book service.
 * @author Thomas Großbeck
 */
@RestController
@CrossOrigin
public class BookController {

    @Autowired
    BookService bookService;

    /**
     * Lists all books.
     * @return response with the status and a collection with all books
     */
    @GetMapping("/books")
    public ResponseEntity<Collection<Book>> getBooks() {
        return ResponseEntity.ok(bookService.getBooks());
    }

    /**
     * Creates a new book in the service.
     * @param book the new book
     */
    @PostMapping(path = "/book", consumes = "application/json")
    public void addBook(@RequestBody Book book) {
        bookService.addBook(book);
    }

    /**
     * Deletes a book in the service.
     * @param isbn the book to delete
     */
    @DeleteMapping(path = "/book/{isbn}")
    public void deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
    }

    /**
     * Updates the attributes of a book.
     * @param book the book to update
     */
    @PutMapping(path = "/book", consumes = "application/json")
    public void updateBook(@RequestBody Book book) {
        bookService.updateBook(book);
    }

    /**
     * Sets the lender for a book.
     * @param userid the id of the user who lends the book
     * @param isbn the isbn of the lended book
     */
    @PutMapping(path = "/lend/{userid}/{isbn}")
    public void lendBook(@PathVariable int userid, @PathVariable String isbn) {
       bookService.lendBook(userid, isbn);
    }

    /**
     * Resets the lender of a book.
     * @param userid the id of the user who returns the book
     * @param isbn the isbn of the returned book
     */
    @PutMapping(path = "/return/{userid}/{isbn}")
    public void returnBook(@PathVariable int userid, @PathVariable String isbn) {
        bookService.returnBook(userid, isbn);
    }

}
