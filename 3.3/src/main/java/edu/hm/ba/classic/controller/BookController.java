package edu.hm.ba.classic.controller;

import edu.hm.ba.classic.entities.Book;
import edu.hm.ba.classic.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<Collection<Book>> getBooks() {
        return ResponseEntity.ok(bookService.getBooks());
    }

    @PostMapping(path = "/book", consumes = "application/json")
    public void addBook(@RequestBody Book book) {
        bookService.addBook(book);
    }

    @DeleteMapping(path = "/book/{isbn}")
    public void deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
    }

    @PutMapping(path = "/book", consumes = "application/json")
    public void updateBook(@RequestBody Book book) {
        bookService.updateBook(book);
    }

    @PutMapping(path = "/lend/{userid}/{isbn}")
    public void lendBook(@PathVariable int userid, @PathVariable String isbn) {
       bookService.lendBook(userid, isbn);
    }

    @PutMapping(path = "/return/{userid}/{isbn}")
    public void returnBook(@PathVariable int userid, @PathVariable String isbn) {
        bookService.returnBook(userid, isbn);
    }

}
