package edu.hm.ba.classic.controller;

import edu.hm.ba.classic.entities.Book;
import edu.hm.ba.classic.entities.User;
import edu.hm.ba.classic.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
     * @return response with the status and the added book
     */
    @PostMapping(path = "/books", consumes = "application/json")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.addBook(book));
    }

    /**
     * Deletes a book in the service.
     * @param isbn the book to delete
     * @return response with the status and the deleted book
     */
    @DeleteMapping(path = "/books/{isbn}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    public ResponseEntity<Book> deleteBook(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.deleteBook(isbn));
    }

    /**
     * Updates the attributes of a book.
     * @param book the book to update
     * @return response with the status and the updated book
     */
    @PutMapping(path = "/books/{isbn}", consumes = "application/json")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    public ResponseEntity<Book> updateBook(@PathVariable String isbn, @RequestBody Book book) {
        return ResponseEntity.ok(bookService.updateBook(isbn, book));
    }

    /**
     * Sets the lender for a book.
     * @param isbn the isbn of the lent book
     * @param authentication authentication object containing the active user
     * @return response with the status and the lender
     */
    @PutMapping(path = "/lend/{isbn}")
    public ResponseEntity<User> lendBook(@PathVariable String isbn, Authentication authentication) {
        return ResponseEntity.ok(bookService.lendBook(isbn, authentication));
    }

    /**
     * Resets the lender of a book.
     * @param isbn the isbn of the returned book
     * @param authentication authentication object containing the active user
     * @return response with the status and the returner
     */
    @PutMapping(path = "/return/{isbn}")
    public ResponseEntity<User> returnBook(@PathVariable String isbn, Authentication authentication) {
        return ResponseEntity.ok(bookService.returnBook(isbn, authentication));
    }

    /**
     * Returns all lent books for a specific user.
     * @param authentication authentication object containing the active user
     * @return response with the status and a collection with the lent books
     */
    @GetMapping(path = "/lend")
    public ResponseEntity<Collection<Book>> getLoans(Authentication authentication) {
        return ResponseEntity.ok(bookService.getLoans(authentication));
    }

}
