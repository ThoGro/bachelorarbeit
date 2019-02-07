package edu.hm.ba.classic.services;

import edu.hm.ba.classic.entities.Book;
import edu.hm.ba.classic.entities.User;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * This interface represents the functionalities of the book service.
 * @author Thomas Großbeck
 */
public interface BookService {

    /**
     * Returns all books.
     * @return collection with all books
     */
    Collection<Book> getBooks();

    /**
     * Adds a book to the service.
     * @param book the book to add
     * @return the added book
     */
    Book addBook(Book book);

    /**
     * Deletes a book from the service.
     * @param isbn the isbn of the book to delete
     * @return the deleted book
     */
    Book deleteBook(String isbn);

    /**
     * Updates a book in the service.
     * @param isbn the isbn of the book to update
     * @param book the book object with the new book informations
     * @return the updated book
     */
    Book updateBook(String isbn, Book book);

    /**
     * Marks a book as lent. After this the lender of the book is set.
     * @param isbn the isbn of the lent book
     * @param authentication authentication object containing the active user
     * @return the lender
     */
    User lendBook(String isbn, Authentication authentication);

    /**
     * Unmarks a lent book. After this the book is available and the lender of the book is null.
     * @param isbn the isbn of the returned book
     * @param authentication authentication object containing the active user
     * @return the returner
     */
    User returnBook(String isbn, Authentication authentication);

    /**
     * Returns all lent books for a specific user.
     * @param authentication authentication object containing the active user
     * @return collection with all lent books
     */
    Collection<Book> getLoans(Authentication authentication);

}
